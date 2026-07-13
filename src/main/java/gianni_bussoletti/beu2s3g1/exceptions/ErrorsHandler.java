package gianni_bussoletti.beu2s3g1.exceptions;

import gianni_bussoletti.beu2s3g1.payloads.ErrorsDTO;
import gianni_bussoletti.beu2s3g1.payloads.ErrorsWithListsDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
// Con questa annotazione dichiaro che questa classe sarà responsabile di gestire le eccezioni di tutta l'applicazione
// Ogni volta che ovunque nell'applicazione viene lanciata un eccezione, arriverà a questa classe
// Questi metodi dovranno essere annotati con @ExceptionHandler
// Avremo un metodo per ogni eccezione che dobbiamo gestire
public class ErrorsHandler {

    @ExceptionHandler(BadRequestException.class)
    // Questa annotazione definisce la classe dell'errore che andremo a catchare
    @ResponseStatus(HttpStatus.BAD_REQUEST) // Questo mapperà lo stato che vogliamo mandare
    public ErrorsDTO handleBadRequest(BadRequestException ex) { //Passando come attributo la classe dell'exception possiamo passare il messaggio direttamente nell'handler
        return new ErrorsDTO(ex.getMessage(), LocalDateTime.now());
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorsDTO handleNotFound(NotFoundException ex) {
        return new ErrorsDTO(ex.getMessage(), LocalDateTime.now());
    }

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorsWithListsDTO validationError(ValidationException ex) {
        return new ErrorsWithListsDTO(ex.getMessage(), LocalDateTime.now(), ex.getErrorsList());
    }

    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorsDTO unauthorizedError(UnauthorizedException ex) {
        return new ErrorsDTO(ex.getMessage(), LocalDateTime.now());
    }

    // Con questo metodo andiamo a gestire tutte le altre exception che non sono
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorsDTO handleGenericException(Exception ex) {
        //Nel caso di errori non Bad Request, non Not Found, verrà utilizzato questo handler mandando una risposta 500
//        ma non svelando i dettagli dell'errore
        ex.printStackTrace(); // Se non stampiamo lo stackTrace dell'errore la fonte del problema è nascosta anche a noi
//        e diventa difficile risolvere il problema
        return new ErrorsDTO("C'è stato un errore lato backend, stiamo lavorando", LocalDateTime.now());
    }
}
