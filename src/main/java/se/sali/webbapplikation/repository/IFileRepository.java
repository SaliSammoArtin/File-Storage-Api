package se.sali.webbapplikation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.sali.webbapplikation.model.File;
import se.sali.webbapplikation.model.User;

import java.util.List;
import java.util.UUID;

public interface IFileRepository extends JpaRepository<File, UUID> {
    List<File> findByOwner (User owner);
}
