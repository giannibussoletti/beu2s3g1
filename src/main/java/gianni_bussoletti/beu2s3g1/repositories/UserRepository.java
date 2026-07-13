package gianni_bussoletti.beu2s3g1.repositories;

import gianni_bussoletti.beu2s3g1.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByMail(String mail);

    boolean existsByMail(String mail);
}
