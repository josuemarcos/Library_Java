package github.com.josuemarcos.libraryapi.repository;

import github.com.josuemarcos.libraryapi.model.Livro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface LivroRepository extends JpaRepository<Livro, UUID> {
}
