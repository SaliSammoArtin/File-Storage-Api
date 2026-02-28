package se.sali.webbapplikation.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

import java.util.UUID;

@NoArgsConstructor
@Getter
@Setter
public class FileResponse extends RepresentationModel<FileResponse> {
    private UUID id;
    private String name;
    private String content;
    private UUID ownerId;
    private UUID folderId;
}
