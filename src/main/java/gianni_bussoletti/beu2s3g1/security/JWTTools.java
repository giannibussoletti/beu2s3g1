package gianni_bussoletti.beu2s3g1.security;

import gianni_bussoletti.beu2s3g1.entities.User;
import gianni_bussoletti.beu2s3g1.exceptions.UnauthorizedException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JWTTools {

    private final String secret;

    public JWTTools(@Value("${jwt.secret}") String secret) {
        this.secret = secret;
    }

    //    La libreria di JWT mette a disposizione due metodi, builder che ci servirà per crearli e builder per leggerli e verificare che non ci siano problemi
    public String generatedToken(User user) {
        return Jwts.builder()
                .issuedAt(new Date(System.currentTimeMillis())) // Data di emissione - in millisecondi
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)) // Data di scadenza - in millisecondi
                .subject(String.valueOf(user.getId())) // Il proprietario del token, Inseriremo l'id dell'utente. MAI METTERE DATI SENSIBILI ALL'ITERNO DEL PAYLOAD
                .signWith(Keys.hmacShaKeyFor(secret.getBytes())) // firma del token per poi verificare che quando il token ritorna al Server non sia stato manomesso
                .compact();
    }

    public void verifyToken(String token) {
        // Qui possiamo utilizzare il metodo parser() il quale server per leggere il token e verificarlo, se si dovessero presentare errori tipo
        // - token scaduto
        // - token manipolato (firma non valida)
        // - token malformato
        // il metodo parser ci darà un eccezione per ogni tipo di problematica.
        try {
            Jwts.parser().verifyWith(Keys.hmacShaKeyFor(secret.getBytes())).build().parse(token);
        } catch (Exception ex) {
            throw new UnauthorizedException("Ci sono stati problemi con il token, Rieffetuare il login");
        }
    }
}
