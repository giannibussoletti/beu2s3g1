package gianni_bussoletti.beu2s3g1.config;

import com.cloudinary.Cloudinary;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class Config {
    @Bean
    public Cloudinary getCloudinaryUploader(@Value("${cloudinary.name}") String cloudName,
                                            @Value("${cloudinary.apikey}") String apiKey,
                                            @Value("${cloudinary.secret}") String secret) {

        // Consiglio di farsi dei sout per verificare se stiamo leggendo effettivamente i valori
        // giusti da application.properties
//		System.out.println(cloudName);

        Map<String, String> config = new HashMap<>();
        config.put("cloud_name", cloudName);
        config.put("api_key", apiKey);
        config.put("api_secret", secret);

        return new Cloudinary(config); // qua ritorno l'oggetto Cloudinary opportunamente configurato
    }
}
