package github.com.josuemarcos.libraryapi.repository;

import github.com.josuemarcos.libraryapi.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AutorRepository extends JpaRepository<Autor, UUID> {
    List<Autor> findByNome(String nome);
    List<Autor> findByNacionalidade(String Nacionalidade);
    List<Autor> findByNomeAndNacionalidade(String nome, String Nacionalidade);
    Optional<Autor> findByNomeAndDataNascimentoAndNacionalidade(String nome, LocalDate dataNascimento, String nacionalidade);
}
