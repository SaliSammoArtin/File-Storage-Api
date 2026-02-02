package se.sali.webbapplikation.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class FolderResponse {
    private UUID id;
    private String name;
    private UUID ownerId;
}
