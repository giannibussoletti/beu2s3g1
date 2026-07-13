package gianni_bussoletti.beu2s3g1.payloads;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record UserDTO(
        //Tutte queste annotazioni sono regole di validazione date dalla libreria Validation
        @NotBlank(message = "Il nome proprio è obbligatorio, il campo non può essere vuoto")
        @Size(min = 2, max = 40, message = "Il nome deve avere un numero di caratteri compreso tra 2 e 40")
        String name,
        @NotBlank(message = "Il cognome è obbligatorio, il campo non può essere vuoto")
        @Size(min = 2, max = 40, message = "Il cognome deve avere un numero di caratteri compreso tra 2 e 40")
        String surname,
        @NotBlank(message = "L'email è obbligatoria")
        @Email(message = "L'email deve avere il formato corretto")
        String mail,
        @NotBlank(message = "La password è obbligatoria")
        @Size(min = 8, message = "La password deve avere almeno 8 caratteri")
//        @Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$", message = "La password deve avere almeno 1 maiuscola, 1 minuscola, 1 carattere speciale, 1 numero")
        String password,
        @Past(message = "La data deve essere precedente a quella di oggi")
        LocalDate birthDate) {
}
