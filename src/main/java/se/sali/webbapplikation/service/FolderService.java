package se.sali.webbapplikation.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.sali.webbapplikation.model.Folder;
import se.sali.webbapplikation.model.User;
import se.sali.webbapplikation.repository.IFolderRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class FolderService {

    private IFolderRepository folderRepository;

    /**
     * Creates a new folder for a user
     * @param name the name of the folder
     * @param owner the user who owns the folder
     * @return the saved Folder entity
     */
    public Folder createFolder(String name, User owner) {
        Folder folder = new Folder();
        folder.setName(name);
        folder.setOwner(owner);
        Folder saved = folderRepository.save(folder);
        return saved;
    }

    /**
     * Retrieves all folders owned by a specific user
     * @param owner the user whose folders should be retrieved
     * @return list of folders belonging to the user
     */
    public List<Folder> getUserFolders(User owner) {
        List<Folder> folders = folderRepository.findByOwner(owner);
        return folders;
    }

    /**
     * Deletes a folder if the user is authorized
     * @param folderId the ID of the folder to delete
     * @param owner the user attempting to delete the folder
     * @throws RuntimeException if folder is not found or user is unauthorized
     */
    public void deleteFolder(UUID folderId, User owner) {
        Optional<Folder> folderOpt = folderRepository.findById(folderId);

        if (folderOpt.isEmpty()) {
            throw new RuntimeException("Folder not found");
        }

        Folder folder = folderOpt.get();

        if (!folder.getOwner().equals(owner)) {
            throw new RuntimeException("Unauthorized");
        }

        folderRepository.delete(folder);
    }
}