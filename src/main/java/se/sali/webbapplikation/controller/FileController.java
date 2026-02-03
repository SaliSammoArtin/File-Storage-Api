package se.sali.webbapplikation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import se.sali.webbapplikation.dto.FileResponse;
import se.sali.webbapplikation.dto.UploadFileRequest;
import se.sali.webbapplikation.model.File;
import se.sali.webbapplikation.model.User;
import se.sali.webbapplikation.service.FileService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
public class FileController {
    private final FileService fileService;

    @PostMapping
    public ResponseEntity<?> uploadFile(@RequestBody UploadFileRequest request) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            User user = (User) auth.getPrincipal();

            File file = fileService.uploadFile(
                    request.getName(),
                    request.getContent(),
                    request.getFolderId(),
                    user);
            FileResponse response = new FileResponse();
            response.setName(file.getName());
            response.setContent(file.getContent());
            response.setFolderId(file.getFolder() != null ? file.getFolder().getId() : null);
            response.setOwnerId(file.getOwner().getId());
            response.setId(file.getId());

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("File upload failed");
        }
    }

    @GetMapping
    public ResponseEntity<?> getUserFiles() {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            User user = (User) auth.getPrincipal();

            List<FileResponse> responses = new ArrayList<>();
            List<File> files = fileService.getUserFiles(user);
            for (File file : files) {
                FileResponse response = new FileResponse();
                response.setId(file.getId());
                response.setName(file.getName());
                response.setContent(file.getContent());
                response.setFolderId(file.getFolder() != null ? file.getFolder().getId() : null);
                response.setOwnerId(file.getOwner().getId());
                responses.add(response);
            }
            return ResponseEntity.ok(responses);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to retrieve files");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> downloadFile(@PathVariable UUID id) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            User user = (User) auth.getPrincipal();

            File file = fileService.downloadFile(id, user);

            FileResponse response = new FileResponse();
            response.setId(file.getId());
            response.setName(file.getName());
            response.setContent(file.getContent());
            response.setFolderId(file.getFolder() != null ? file.getFolder().getId() : null);
            response.setOwnerId(file.getOwner().getId());

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to download file");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFile(@PathVariable UUID id) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            User user = (User) auth.getPrincipal();

            fileService.deleteFile(id, user);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to delete file");
        }
    }
}