package gianni_bussoletti.beu2s3g1.controllers;

import gianni_bussoletti.be_u2_s2_g4.entities.User;
import gianni_bussoletti.be_u2_s2_g4.exceptions.ValidationException;
import gianni_bussoletti.be_u2_s2_g4.payloads.PasswordUpdateDTO;
import gianni_bussoletti.be_u2_s2_g4.payloads.UserDTO;
import gianni_bussoletti.be_u2_s2_g4.payloads.UserResponseDTO;
import gianni_bussoletti.be_u2_s2_g4.payloads.UserUpdateDTO;
import gianni_bussoletti.be_u2_s2_g4.services.UserService;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    //    1. POST http://localhost:PORT/users (+req.body) --> 201
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
//    @Validated serve per dire all'applicazione che quel Payload deve essere validato
    public UserResponseDTO saveUSer(@RequestBody @Validated UserDTO body, BindingResult validation) {
        if (validation.hasErrors()) {
            List<String> errorsMessage = validation.getFieldErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toList();
            throw new ValidationException("C'è stato un errore nell'invio dei dati");
        }
        User saved = this.userService.save(body);
        return new UserResponseDTO(saved.getId());
    }

    @GetMapping
    public Page<User> getUsers(@RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "5") int size,
                               @RequestParam(defaultValue = "name") String orderBy) {
        return this.userService.getAll(page, size, orderBy);
    }

    @GetMapping("/{userId}")
    public User getById(@PathVariable UUID userId) {
        return this.userService.findById(userId);
    }

    @PutMapping("/{userId}")
    public User getByIdAndUpdate(@PathVariable UUID userId, @RequestBody UserUpdateDTO body) {
        return this.userService.findByIdAndUpdate(userId, body);
    }

    @DeleteMapping()
    public void findByIdAndDelete(UUID userId) {
        this.userService.findyByIdAndDelete(userId);
    }

    @PatchMapping("/{userId}/password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updatePassword(@PathVariable UUID userId, @RequestBody PasswordUpdateDTO body) {
        this.userService.updatePassword(userId, body);

    }

    @PatchMapping("/{userId}/avatar")
    //Il multipartFile è un metodo per gestire oggetti non JSON come immagini o video
    // Il requestParam deve avere un nome che deve coincidere con quello del frontEnd
    public void updateAvatar(@PathVariable UUID userId, @RequestParam("profile_picture") MultipartFile file) {
        //In questo ENDPOINT non si usano JSON ma si attende che arrivino payload in formato multipart/form-data
        this.userService.updateAvatar(userId, file);
    }

}
