package gianni_bussoletti.beu2s3g1.security;

import gianni_bussoletti.beu2s3g1.exceptions.UnauthorizedException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@AllArgsConstructor
@Component
public class TokenFilter extends OncePerRequestFilter {
    private final JWTTools jwtTools;

    @Override
    // request è la richiesta corrente
    // response serve a mandare una risposta di errore
    // filterChain serve per andare al prossimo step
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Questo metodo viene chiamato ad ogni richiesta
        // LA responsabilità di questo metodo sarà quello di controllare se i token sono ok
        //Se sono ok andiamo avanti nella catena
        // SE c'è qualche problema col token non si va al prossimo ste e si manda una risposta di errore

        // 1. Verifichiamo che ci sia la Authorization nell'header e controllare che sia nel formato corretto "Bearer token"
        // 1.1 SE Auth Header è sbagliato in qualsiasi forma --> errore
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            System.out.println(authHeader);
            throw new UnauthorizedException("Inserire il token nell'Authorization payload nel formato corretto 'Bearer '");
        }

        //2. Estraiamo il token dall'header
        // Il metodo replace sostituisce una parte della stringa che gli andiamo a chiedere.
        String accessToken = authHeader.replace("Bearer ", "");
        System.out.println(accessToken);
        //3. Verifichiamo che il token sia OK
        this.jwtTools.verifyToken(accessToken);


        //3.1 Controlliamo se non sia mal formato
        //3.2 Che non sia scaduto
        //3.3 Che la firma sia ok e quindi non sia stato manipolato
        // Se il token è OK --> andiamo avanti, ad un prossimo filtro, od ad un controller sa siamo gli ultimi filtri
        filterChain.doFilter(request, response); // <-- Questo permette di andare avanti con l'applicazione.
        // Se il token ha problemi --> Errore, rifai il login.
    }

    @Override
    // Tramite questo metodo possiamo specificare dei criteri per i quali il filtro non debba essere utilizzato.
    //Ad esempio in un login
    //O anche in fase di registrazione
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
//        return request.getServletPath().equals("/auth/login");
        //Tutti gli endpoint dentro AuthController non avranno la richiesta del token
        return new AntPathMatcher().match("/auth/**", request.getServletPath());
    }
}
