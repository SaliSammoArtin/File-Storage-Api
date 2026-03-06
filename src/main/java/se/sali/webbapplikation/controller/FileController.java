package se.sali.webbapplikation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import se.sali.webbapplikation.dto.ErrorResponse;
import se.sali.webbapplikation.dto.FileResponse;
import se.sali.webbapplikation.dto.UploadFileRequest;
import se.sali.webbapplikation.model.File;
import se.sali.webbapplikation.model.User;
import se.sali.webbapplikation.security.AuthUtils;
import se.sali.webbapplikation.service.FileService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/file")
@RequiredArgsConstructor
public class FileController {
    private final FileService fileService;
    private final AuthUtils authUtils;

    @PostMapping
    public ResponseEntity<?> uploadFile(@RequestBody UploadFileRequest request) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            User user = authUtils.getCurrentUser(auth);

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
            response.add(linkTo(methodOn(FileController.class).getUserFiles()).withRel("all-files"));
            response.add(linkTo(methodOn(FileController.class).deleteFile(file.getId())).withRel("delete"));
            response.add(linkTo(methodOn(FileController.class).downloadFile(file.getId())).withRel("download"));

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body(new ErrorResponse(e.getMessage(), 400));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ErrorResponse("File upload failed", 500));
        }
    }

    @GetMapping
    public ResponseEntity<?> getUserFiles() {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            User user = authUtils.getCurrentUser(auth);

            List<FileResponse> responses = new ArrayList<>();
            List<File> files = fileService.getUserFiles(user);
            for (File file : files) {
                FileResponse response = new FileResponse();
                response.setId(file.getId());
                response.setName(file.getName());
                response.setContent(file.getContent());
                response.setFolderId(file.getFolder() != null ? file.getFolder().getId() : null);
                response.setOwnerId(file.getOwner().getId());
                response.add(linkTo(methodOn(FileController.class).deleteFile(file.getId())).withRel("delete"));
                response.add(linkTo(methodOn(FileController.class).downloadFile(file.getId())).withRel("download"));
                responses.add(response);
            }
            return ResponseEntity.ok(responses);
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse("Failed to retrieve file", 500);
            return ResponseEntity.status(500).body(error);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> downloadFile(@PathVariable UUID id) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            User user = authUtils.getCurrentUser(auth);

            File file = fileService.downloadFile(id, user);

            FileResponse response = new FileResponse();
            response.setId(file.getId());
            response.setName(file.getName());
            response.setContent(file.getContent());
            response.setFolderId(file.getFolder() != null ? file.getFolder().getId() : null);
            response.setOwnerId(file.getOwner().getId());
            response.add(linkTo(methodOn(FileController.class).deleteFile(file.getId())).withRel("delete"));


            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            ErrorResponse error = new ErrorResponse(e.getMessage(), 404);
            return ResponseEntity.status(404).body(error);
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse("Failed to download file", 500);
            return ResponseEntity.status(500).body(error);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFile(@PathVariable UUID id) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            User user = authUtils.getCurrentUser(auth);

            fileService.deleteFile(id, user);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            ErrorResponse error = new ErrorResponse(e.getMessage(), 404);
            return ResponseEntity.status(404).body(error);
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse("Failed to delete file", 500);
            return ResponseEntity.status(500).body(error);
        }
    }
}