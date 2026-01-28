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

    public Folder createFolder(String name, User owner) {
        Folder folder = new Folder();
        folder.setName(name);
        folder.setOwner(owner);
        Folder saved = folderRepository.save(folder);
        return saved;
    }

    public List<Folder> getUserFolders(User owner) {
        List<Folder> folders = folderRepository.findByOwner(owner);
        return folders;
    }

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