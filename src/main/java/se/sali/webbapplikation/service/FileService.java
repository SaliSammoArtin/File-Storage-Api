package se.sali.webbapplikation.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.sali.webbapplikation.model.File;
import se.sali.webbapplikation.model.Folder;
import se.sali.webbapplikation.model.User;
import se.sali.webbapplikation.repository.IFileRepository;
import se.sali.webbapplikation.repository.IFolderRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class FileService {

    @Autowired
    private IFileRepository fileRepository;

    @Autowired
    private IFolderRepository folderRepository;

    public File uploadFile(String name, String content, UUID folderId, User owner) {
        Folder folder = null;
        if (folderId != null) {
            Optional<Folder> folderOpt = folderRepository.findById(folderId);
            if (folderOpt.isEmpty() || !folderOpt.get().getOwner().equals(owner)) {
                throw new RuntimeException("Folder not found or unauthorized");
            }
            folder = folderOpt.get();
        }

        File file = new File();
        file.setName(name);
        file.setContent(content);
        file.setFolder(folder);
        file.setOwner(owner);

        return fileRepository.save(file);
    }

    public List<File> getUserFiles(User owner) {
        return fileRepository.findByOwner(owner);
    }

    public void deleteFile(UUID fileId, User owner) {
        Optional<File> fileOpt = fileRepository.findById(fileId);

        if (fileOpt.isEmpty()) {
            throw new RuntimeException("File not found");
        }

        File file = fileOpt.get();

        if (!file.getOwner().equals(owner)) {
            throw new RuntimeException("Unauthorized");
        }

        fileRepository.delete(file);
    }

    public File downloadFile(UUID fileId, User owner) {
        Optional<File> fileOpt = fileRepository.findById(fileId);

        if (fileOpt.isEmpty()) {
            throw new RuntimeException("File not found");
        }

        File file = fileOpt.get();

        if (!file.getOwner().equals(owner)) {
            throw new RuntimeException("Unauthorized");
        }

        return file;
    }
}
}
