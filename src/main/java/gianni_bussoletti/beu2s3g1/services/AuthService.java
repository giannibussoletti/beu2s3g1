package gianni_bussoletti.beu2s3g1.services;

import gianni_bussoletti.beu2s3g1.entities.User;
import gianni_bussoletti.beu2s3g1.exceptions.UnauthorizedException;
import gianni_bussoletti.beu2s3g1.payloads.LoginDTO;
import gianni_bussoletti.beu2s3g1.security.JWTTools;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserService userService;
    private final JWTTools jwtTools;

    public AuthService(UserService userService, JWTTools jwtTools) {
        this.userService = userService;
        this.jwtTools = jwtTools;
    }


    public String checkCredentialsAndGeneratorToken(LoginDTO body) {
        // 1. Controllo credenziali
        // 1.1 Controllo se l'email esiste
        User found = this.userService.findByMail(body.email());
        // 1.2 Controllare se le password corrispondono
        if (found.getPassword().equals(body.password())) {
            // 2. Se è tutto è OK generiamo una Access Token per l'utente e lo ritorniamo
            return this.jwtTools.generatedToken(found);
// TODO migliorare gestione password.
            // 3. Altrimenti --> 401 ("Credenziali Sbagliate")
        } else throw new UnauthorizedException("Credenziali non valide");
    }
}
