package se.sali.webbapplikation.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "files")
@NoArgsConstructor
@Getter
@Setter
public class File {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String name;

    private String content;

    @ManyToOne
    private User owner;

    @ManyToOne
    private Folder folder;
}
