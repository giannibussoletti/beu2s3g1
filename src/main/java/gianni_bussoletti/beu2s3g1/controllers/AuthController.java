package gianni_bussoletti.beu2s3g1.controllers;

import gianni_bussoletti.beu2s3g1.payloads.AuthResponseDTO;
import gianni_bussoletti.beu2s3g1.payloads.LoginDTO;
import gianni_bussoletti.beu2s3g1.services.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public AuthResponseDTO login(@RequestBody LoginDTO body) {
        String loginToken = this.authService.checkCredentialsAndGeneratorToken(body);
        return new AuthResponseDTO(loginToken);
    }
}
