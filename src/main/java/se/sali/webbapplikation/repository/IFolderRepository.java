package se.sali.webbapplikation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.sali.webbapplikation.model.Folder;
import se.sali.webbapplikation.model.User;

import java.util.List;
import java.util.UUID;

public interface IFolderRepository extends JpaRepository<Folder, UUID> {
    List<Folder> findByOwner(User owner);
}
