package gianni_bussoletti.beu2s3g1.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import gianni_bussoletti.be_u2_s2_g4.entities.User;
import gianni_bussoletti.be_u2_s2_g4.exceptions.BadRequestException;
import gianni_bussoletti.be_u2_s2_g4.exceptions.NotFoundException;
import gianni_bussoletti.be_u2_s2_g4.payloads.PasswordUpdateDTO;
import gianni_bussoletti.be_u2_s2_g4.payloads.UserDTO;
import gianni_bussoletti.be_u2_s2_g4.payloads.UserUpdateDTO;
import gianni_bussoletti.be_u2_s2_g4.repositories.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final Cloudinary fileUploader;

    public UserService(UserRepository userRepository, Cloudinary fileUploader) {
        this.userRepository = userRepository;
        this.fileUploader = fileUploader;
    }

    public User save(UserDTO payload) {
//        1. Verifichiamo che l'email sia unica
        if (this.userRepository.existsByMail(payload.mail()))
            throw new BadRequestException("L'email " + payload.mail() + " esiste già");
//        2. TODO: Ulteriori controlli
//        3. Creo il nuovo oggetto User leggendo i valori dal payload
        User newUser = new User(payload.name(), payload.surname(), payload.password(), payload.mail(), payload.birthDate());
//        4. Salviamo il nuovo utente
        return this.userRepository.save(newUser);
    }

    //SE invece ci List usiamo Page, possiamo creare una pagination
    public Page<User> getAll(int page, int size, String orderBy) {
        // Andando a creare un oggetto di tipo Pageable andiamo a comunicare con il client di voler creare una pagination
//        L'oggetto pageable contiene diverse informazioni, per esempio il numero di pagine totali, utili al front-end
//        la prima prorietà è il numero di pagine, il secondo è il numero massimo di risultati, e la terza è il sorting
//        Tutti e tre possono essere parametrici
        Pageable pageable = PageRequest.of(page, size, Sort.by(orderBy));
        return this.userRepository.findAll(pageable);
    }

    public User findById(UUID userId) {
        return this.userRepository.findById(userId).orElseThrow(() -> new NotFoundException("L'utente non è stato trovato"));
    }

    public User findByIdAndUpdate(UUID userId, UserUpdateDTO body) {
        User found = this.findById(userId);
        if (!found.getMail().equals(body.mail()))
            if (this.userRepository.existsByMail(body.mail()))
                throw new BadRequestException("L'indirizzo " + body.mail() + " è già utilizzato");

        found.setName(body.name());
        found.setSurname(body.surname());
        found.setMail(body.mail());
        found.setBirthDate(body.birthDate());

        return this.userRepository.save(found);
    }

    public void findyByIdAndDelete(UUID userId) {
        User found = this.findById(userId);
        this.userRepository.delete(found);
    }

    public void updatePassword(UUID userId, PasswordUpdateDTO payload) {
        User found = this.findById(userId);

        if (!found.getPassword().equals(payload.oldPassword()))
            throw new BadRequestException("Le password non corrispondono!");

        found.setPassword(payload.newPassword());

        this.userRepository.save(found);
    }

    public void updateAvatar(UUID userId, MultipartFile file) {
        // 1. Controlli vari tipo file non più grande di tot, tipo di file permesso solo GIF
        // 2. Find by id dell'utente

        // 3. Upload del file su Cloudinary
        try {
            Map result = fileUploader.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
            String url = (String) result.get("secure_url");
            System.out.println(url);
            // 4. Se l'upload va a buon fine, Cloudinary ci restituirà l'url dell'immagine.
            // Questo URL deve essere settato nel record dell'utente
            // (setAvatarURL("url")
            // save dell'utente

            // 5. O torno void o torno l'utente modificato
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
