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
    public ResponseEntity<FileResponse> uploadFile(@RequestBody UploadFileRequest request) {
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
    }

    @GetMapping
    public ResponseEntity<List<FileResponse>> getUserFiles() {
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
    }

    @GetMapping("/{id}")
    public ResponseEntity<FileResponse> downloadFile(@PathVariable UUID id) {
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
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFile(@PathVariable UUID id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();

        fileService.deleteFile(id, user);
        return ResponseEntity.noContent().build();
    }
}