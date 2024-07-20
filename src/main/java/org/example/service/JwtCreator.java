package org.example.service;

import io.jsonwebtoken.Jwts;
import lombok.SneakyThrows;
import org.example.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Map;

@Service
public class JwtCreator {
    private final PrivateKey privateKey;

    @SneakyThrows
    public JwtCreator(@Value("${msa.auth.private}") String privateKey) {
        byte[] keyBytes = Base64Utils.decodeFromString(
                privateKey
                        .replace("-----BEGIN PRIVATE KEY-----", "")
                        .replace("-----END PRIVATE KEY-----", "")
                        .replaceAll("\\s+","")
        );
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory fact = KeyFactory.getInstance("RSA");
        this.privateKey = fact.generatePrivate(keySpec);
    }

    public String createJwt(User user) {
        return Jwts.builder()
                .setSubject(user.getName())
                .addClaims(Map.of(
                        "player", user.isPlayer()))
                .signWith(privateKey)
                .compact();
    }
}
