package se.sali.webbapplikation.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

import java.util.UUID;

@NoArgsConstructor
@Getter
@Setter
public class RegisterResponse extends RepresentationModel<RegisterResponse> {
    private UUID id;
    private String username;
}
