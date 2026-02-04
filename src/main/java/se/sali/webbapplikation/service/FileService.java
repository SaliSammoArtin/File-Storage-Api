package se.sali.webbapplikation.service;

import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class FileService {

    private final IFileRepository fileRepository;
    private final IFolderRepository folderRepository;

    /**
     * Uploads a new file to the system
     * @param name the name of the file
     * @param content the text content of the file
     * @param folderId optional folder ID where the file should be stored (can be null)
     * @param owner the user who owns the file
     * @return the saved File entity
     * @throws RuntimeException if folder is not found or user is unauthorized
     */
    public File uploadFile(String name, String content, UUID folderId, User owner) {
        Folder folder = null;
        if (folderId != null) {
            Optional<Folder> folderOpt = folderRepository.findById(folderId);
            if (folderOpt.isEmpty() || !folderOpt.get().getOwner().getId().equals(owner.getId())) {
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

    /**
     * Retrieves all files owned by a specific user
     * @param owner the user whose files should be retrieved
     * @return list of files belonging to the user
     */
    public List<File> getUserFiles(User owner) {
        return fileRepository.findByOwner(owner);
    }

    /**
     * Deletes a file if the user is authorized
     * @param fileId the ID of the file to delete
     * @param owner the user attempting to delete the file
     * @throws RuntimeException if file is not found or user is unauthorized
     */
    public void deleteFile(UUID fileId, User owner) {
        Optional<File> fileOpt = fileRepository.findById(fileId);

        if (fileOpt.isEmpty()) {
            throw new RuntimeException("File not found");
        }

        File file = fileOpt.get();

        if (!file.getOwner().getId().equals(owner.getId())) {
            throw new RuntimeException("Unauthorized");
        }

        fileRepository.delete(file);
    }

    /**
     * Downloads a file if the user is authorized
     * @param fileId the ID of the file to download
     * @param owner the user attempting to download the file
     * @return the File entity with content
     * @throws RuntimeException if file is not found or user is unauthorized
     */
    public File downloadFile(UUID fileId, User owner) {
        Optional<File> fileOpt = fileRepository.findById(fileId);

        if (fileOpt.isEmpty()) {
            throw new RuntimeException("File not found");
        }

        File file = fileOpt.get();

        if (!file.getOwner().getId().equals(owner.getId())) {
            throw new RuntimeException("Unauthorized");
        }

        return file;
    }
}
