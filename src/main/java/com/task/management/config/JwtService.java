package com.task.management.config;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    private SecretKey generateSecretKey() {
        try {
            // Generate a SHA-256 hash of the secret key
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(secretKey.getBytes());

            // Use the first 256 bits (32 bytes) as the key
            byte[] key = new byte[32];
            System.arraycopy(hash, 0, key, 0, key.length);

            return new SecretKeySpec(key, "HmacSHA256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error generating secret key", e);
        }
    }

    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        try {
            // Prepare JWT claims
            JWTClaimsSet.Builder claimsBuilder = new JWTClaimsSet.Builder()
                .subject(userDetails.getUsername())
                .issueTime(new Date())
                .expirationTime(new Date(System.currentTimeMillis() + jwtExpiration));

            // Add extra claims
            extraClaims.forEach(claimsBuilder::claim);

            JWTClaimsSet claims = claimsBuilder.build();

            // Create Signed JWT
            JWSSigner signer = new MACSigner(generateSecretKey());
            SignedJWT signedJWT = new SignedJWT(
                new JWSHeader(JWSAlgorithm.HS256),
                claims
            );
            signedJWT.sign(signer);

            // Serialize to token
            return signedJWT.serialize();
        } catch (JOSEException e) {
            throw new RuntimeException("Error generating JWT", e);
        }
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        try {
            // Parse the token
            SignedJWT signedJWT = SignedJWT.parse(token);

            // Verify the signature
            JWSVerifier verifier = new MACVerifier(generateSecretKey());
            if (!signedJWT.verify(verifier)) {
                return false;
            }

            // Get claims
            JWTClaimsSet claims = signedJWT.getJWTClaimsSet();

            // Check expiration and username
            return !isTokenExpired(claims) &&
                   claims.getSubject().equals(userDetails.getUsername());
        } catch (ParseException | JOSEException e) {
            return false;
        }
    }

    public String extractUsername(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            return signedJWT.getJWTClaimsSet().getSubject();
        } catch (ParseException e) {
            throw new RuntimeException("Error extracting username", e);
        }
    }

    private boolean isTokenExpired(JWTClaimsSet claims) {
        return claims.getExpirationTime().before(new Date());
    }

    public String refreshToken(String token) {
        try {
            // Parse existing token
            SignedJWT signedJWT = SignedJWT.parse(token);
            JWTClaimsSet oldClaims = signedJWT.getJWTClaimsSet();

            // Create new claims with updated expiration
            JWTClaimsSet newClaims = new JWTClaimsSet.Builder()
                .subject(oldClaims.getSubject())
                .issueTime(new Date())
                .expirationTime(new Date(System.currentTimeMillis() + jwtExpiration))
                .build();

            // Sign new token
            JWSSigner signer = new MACSigner(generateSecretKey());
            SignedJWT newSignedJWT = new SignedJWT(
                new JWSHeader(JWSAlgorithm.HS256),
                newClaims
            );
            newSignedJWT.sign(signer);

            return newSignedJWT.serialize();
        } catch (ParseException | JOSEException e) {
            throw new RuntimeException("Error refreshing token", e);
        }
    }
}
