package gianni_bussoletti.beu2s3g1.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
// Questa non è una normale classe per i bean ma è pensata a posta per gestire la sicurezza di Spring security
// Questa annotazione serve per configurare la gestione della sicurezza nella Classe di configurazione
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) {
        httpSecurity.formLogin(formLogin -> formLogin.disable()); // disabilità il form di login di base di Spring
        // Permette di sbloccare i vari endpoint per poi andare a personalizzarli in seguito
        // /** si traduce "Ogni endpoint che ha qualsiasi cosa dopo lo slash, quindi security non interverrà su nessun metodo
        httpSecurity.authorizeHttpRequests(req -> req.requestMatchers("/**").permitAll());
        // Siccome andiamo ad implementare la JWT che sono stateless allora andiamo a disabilitare le sessioni
        httpSecurity.sessionManagement(sessions -> sessions.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // Disabilitiamo la protezione da attacchi CSRF perché con i token non servono
        // ed andrebbero solo a complicare sia front che back-end
        httpSecurity.csrf(csrf -> csrf.disable());
        return httpSecurity.build();
    }
}
