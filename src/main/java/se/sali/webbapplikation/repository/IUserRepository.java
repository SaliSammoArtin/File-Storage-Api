package se.sali.webbapplikation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.sali.webbapplikation.model.User;

import java.util.Optional;
import java.util.UUID;

public interface IUserRepository extends JpaRepository<User, UUID> {
    Optional<User>findByUsername(String username);
}
