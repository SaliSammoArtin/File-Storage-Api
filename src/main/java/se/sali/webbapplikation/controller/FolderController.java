package se.sali.webbapplikation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import se.sali.webbapplikation.dto.CreateFolderRequest;
import se.sali.webbapplikation.dto.FolderResponse;
import se.sali.webbapplikation.model.Folder;
import se.sali.webbapplikation.model.User;
import se.sali.webbapplikation.service.FolderService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/folder")
@RequiredArgsConstructor
public class FolderController {

    private final FolderService folderService;

    @PostMapping
    public ResponseEntity<?> createFolder(@RequestBody CreateFolderRequest request) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            User user = (User) auth.getPrincipal();

            Folder folder = folderService.createFolder(request.getName(), user);
            FolderResponse response = new FolderResponse();
            response.setId(folder.getId());
            response.setName(folder.getName());
            response.setOwnerId(folder.getOwner().getId());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to create folder");
        }
    }

    @GetMapping
    public ResponseEntity<?> getFolder() {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            User user = (User) auth.getPrincipal();

            List<FolderResponse> responses = new ArrayList<>();
            List<Folder> folders = folderService.getUserFolders(user);
            for (Folder folder : folders) {
                FolderResponse response = new FolderResponse();
                response.setId(folder.getId());
                response.setName(folder.getName());
                response.setOwnerId(folder.getOwner().getId());
                responses.add(response);
            }
            return ResponseEntity.ok(responses);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to retrieve folders");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFolder(@PathVariable UUID id) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            User user = (User) auth.getPrincipal();

            folderService.deleteFolder(id, user);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to delete folder");
        }
    }
}