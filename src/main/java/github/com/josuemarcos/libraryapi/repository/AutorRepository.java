package github.com.josuemarcos.libraryapi.repository;

import github.com.josuemarcos.libraryapi.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AutorRepository extends JpaRepository<Autor, UUID> {
}
