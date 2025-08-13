package github.com.josuemarcos.libraryapi.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "usuario")
@Data
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(length = 50, nullable = false, unique = true)
    private String login;

    @Column(length = 300, nullable = false)
    private String senha;

    @Column
    private List<String> roles;
}
