package se.sali.webbapplikation.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@NoArgsConstructor
@Setter
@Getter
public class UploadFileRequest {
    private String name;
    private String content;
    private UUID folderId;
}
