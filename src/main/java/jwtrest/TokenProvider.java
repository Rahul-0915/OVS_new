package jwtrest;

//import com.sun.xml.ws.security.impl.policy.Claims;
//import static com.sun.xml.ws.security.impl.policy.Constants.SignatureAlgorithm;
import static jwtrest.Constants.REMEMBERME_VALIDITY_SECONDS;
import io.jsonwebtoken.*;
import java.io.Serializable;
import java.security.PrivateKey;
import java.security.PublicKey;
//import java.security.SignatureException;
import java.util.Arrays;
import java.util.Date;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import static java.util.stream.Collectors.joining;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Named;
import java.security.KeyFactory;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

//@SessionScoped
@Named
public class TokenProvider implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(TokenProvider.class.getName());

    private static final String AUTHORITIES_KEY = "auth";

    private String secretKey;
    private String privateKey;
    private String publicKey;
    private PrivateKey myprivateKey;
    private PublicKey mypublicKey;
    private long tokenValidity;

    private long tokenValidityForRememberMe;

    @PostConstruct
    public void init() {
        // load from config
        this.secretKey = "mysecretjwtkeysasavsadvhgsavdgasvdhgagsasaahgahacaSCASAscACAcACAdaHGAcsASASDVSADVSADSAGCSAGDCSACSAFDSAjdsjfdshfdshfgdshvdsfvdsfdsgfhdssvdsjfdsfjsdgdsfdshgfhfdggffghggdg";
        this.privateKey = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDBe+pg8m/jkWQC\n"
                + "gkTdJJDihWAWmc5SyJ25TgIO791q/OYbAJLvO/vmn80svEwxAZ07s58lZlFn5Ozp\n"
                + "q+EuoBclICJ5tvPLyJMdIop4pta7BnDBENEKztiJ7LIe3wlinPH9TfV7/ZTUfUGp\n"
                + "D/F0OvnKodXrl+PVJ2r6AzEpYS0LQ/ZAmCzZfK6IJEoZYvLwPLcPVfv5s/MCVE/u\n"
                + "j4tRUl0IY4V1FS16SdANUpnU8U+RuTjWONomf8d3wvlAM2WUW09mFpbWLV6BFkwV\n"
                + "TCHeElbFvZpN7XlrO7G5NhBGB9lqNxZP9/Qvux4qlwqAwrLSeh1nsAOVWRJ2w9GE\n"
                + "xJVBTgVlAgMBAAECggEAAdD/PgpAnScOBI0DBv1zI8FDSesHOhU6j9UI5WmAj2LQ\n"
                + "6TN77aWHQ56/7xnUcEhW2MripVf8zyghxj7QFh84IGfZEwHx73mSUf1zRdcxIF/a\n"
                + "2qElCUAwXbkcYfhPjv6wseNTuOaESWtknKjy6BeupSWYS8YpBCUC1taFVWFdaiPk\n"
                + "w8nJYPbai1MvzW3nZnKr31JMgTHAGgG4VNArFCTMmCCdfO+78WTiIr/j+yul5Vl5\n"
                + "6vgSLd/o/dkB6KzE/YFaYqkEqR0bX8pSypGfYRksZGkJJQH4K6YMpVjaBLLGyiS6\n"
                + "BOijbYMNKlx2Aw8UEF6tTqallVvH7sxlr44daN3d8QKBgQDbB2IA+/qrjhuy44pM\n"
                + "qZThLJpgiIaH/YRE4YLZwiclnMDvFXRYYR1mcziaWkkK573btm0qTsjrmZiPKQCL\n"
                + "HmnArs7OyXBMDbs/Mk+WKh12S5K8QmNVz7bzLkPD9C4X7u4z5tu56qZNC57z6ds1\n"
                + "EQkLZtxFGsL/9nPc/DCWxExdcQKBgQDiJLKd+lRXk67eus4200O8gdEBEz4lh1TX\n"
                + "9kTOjkiOT1uYiVpa7BmSbXcAmRUMY4H2RLp2IfMKJ5ssHwe4H3Y7Y1/H/rAtpK8r\n"
                + "uOSkXlLq2DvnfjqyEc4P094R1pCh/viC3IKMzSbX0JZcEYJvHCc+mLgvPL/EqL5v\n"
                + "F5ab/Ib9NQKBgGJ3nDijD5uGpK80ml1Cs9rTaYfSeOC0OX2aAHCTV3QSV65kb8y3\n"
                + "xDblv+Gsiz/q1TDsf4FQsAUzJSHfJg5lGtfz/qd6ahDW74JGxP7Wai5fZVVbZzsR\n"
                + "ycbj2rVClmJOGSqeM9QOSLtEaS5wyQq/YNiOYqJymI3oJ0iG2/U7xLURAoGALdr1\n"
                + "IRWGjq+SkPVeJT3XiVzlbYtiWafEa3ozX5L4YWr0Ds0jNjaTxN5PeB6SZw2yZ6Zu\n"
                + "DNA7gP6g92RfY1V12vr+jAY34Tl4j6wRKMc7lwU7uGgfLMZxe0Ih0Ioqj76s05Q1\n"
                + "IKnky3QvWQHv6enSh13eUy3FUPJKkyo8Tur40FUCgYEApwdwanLrnoTmNJnjXR1V\n"
                + "Zw7BAuAsPBwjo3kQrdKpmCIbAETRmMaclN3ChUGzaXo54jt9k9EQt2oC0UWSsr2o\n"
                + "09Q976oJ1mmWiG0et1bVXhNpxcoHlug0qlb7PzUVh0ct6b8rkH+xvWM7QABzH7/a\n"
                + "QpUQWPdntclzaeQjYm/NNsI=";

        this.publicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAwXvqYPJv45FkAoJE3SSQ\n"
                + "4oVgFpnOUsiduU4CDu/davzmGwCS7zv75p/NLLxMMQGdO7OfJWZRZ+Ts6avhLqAX\n"
                + "JSAiebbzy8iTHSKKeKbWuwZwwRDRCs7YieyyHt8JYpzx/U31e/2U1H1BqQ/xdDr5\n"
                + "yqHV65fj1Sdq+gMxKWEtC0P2QJgs2XyuiCRKGWLy8Dy3D1X7+bPzAlRP7o+LUVJd\n"
                + "CGOFdRUteknQDVKZ1PFPkbk41jjaJn/Hd8L5QDNllFtPZhaW1i1egRZMFUwh3hJW\n"
                + "xb2aTe15azuxuTYQRgfZajcWT/f0L7seKpcKgMKy0nodZ7ADlVkSdsPRhMSVQU4F\n"
                + "ZQIDAQAB";

        privateKey = privateKey.replaceAll("\\n", "")
                .replaceAll("\\r", "")
                .replaceAll("\\s", "")
                .replaceAll("-----.*?-----", "").trim();
        int pad = 4 - (privateKey.length() % 4);
        if (pad < 4) {
            privateKey += "=".repeat(pad);
        }

        publicKey = publicKey.replaceAll("\\n", "")
                .replaceAll("\\r", "")
                .replaceAll("\\s", "")
                .replaceAll("-----.*?-----", "").trim();
        int pad1 = 4 - (publicKey.length() % 4);
        if (pad1 < 4) {
            publicKey += "=".repeat(pad);
        }

        byte[] privateKeyBytes = Base64.getDecoder().decode(privateKey);
        byte[] publicKeyBytes = Base64.getDecoder().decode(publicKey);
        try {
            KeyFactory kf = KeyFactory.getInstance("RSA"); // or "EC" or whatever
            myprivateKey = kf.generatePrivate(new PKCS8EncodedKeySpec(privateKeyBytes));
            mypublicKey = kf.generatePublic(new X509EncodedKeySpec(publicKeyBytes));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        this.tokenValidity = TimeUnit.HOURS.toMillis(10);   //10 hours
        this.tokenValidityForRememberMe = TimeUnit.SECONDS.toMillis(REMEMBERME_VALIDITY_SECONDS);   //24 hours
    }

    public String createToken(String username, Set<String> authorities, Boolean rememberMe) {
        long now = (new Date()).getTime();
        long validity = rememberMe ? tokenValidityForRememberMe : tokenValidity;
        System.out.println("TokenProvider - In create Token");
        return Jwts.builder()
                .setSubject(username)
                .setIssuer("localhost")
                .claim(AUTHORITIES_KEY, authorities.stream().collect(joining(",")))
                //  .signWith(SignatureAlgorithm.HS512, secretKey)
                .signWith(SignatureAlgorithm.RS256, myprivateKey)
                .setExpiration(new Date(now + validity))
                .compact();
    }

    public JWTCredential getCredential(String token) {
        Claims claims = Jwts.parser()
                //.setSigningKey(secretKey)

                .setSigningKey(mypublicKey)
                .build().parseSignedClaims(token)
                .getBody();
        System.out.println("TokenProvider - Token Provider - In Get Credential");
        Set<String> authorities
                = Arrays.asList(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .stream()
                        .collect(Collectors.toSet());

        return new JWTCredential(claims.getSubject(), authorities);
    }

    public boolean validateToken(String authToken) {
        try {
            System.out.println("TokenProvider - TokenProvider - validate token");
            //    Jwts.parser().setSigningKey(secretKey).build().parseSignedClaims(authToken);
            Jwts.parser().setSigningKey(mypublicKey).build().parseSignedClaims(authToken);
            return true;
        } catch (SignatureException e) {
            LOGGER.log(Level.INFO, "Invalid JWT signature: {0}", e.getMessage());
            return false;
        }
    }
}