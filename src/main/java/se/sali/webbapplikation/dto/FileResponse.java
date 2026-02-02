package se.sali.webbapplikation.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@NoArgsConstructor
@Getter
@Setter
public class FileResponse {
    private UUID id;
    private String name;
    private String content;
    private UUID ownerId;
    private UUID folderId;
}
