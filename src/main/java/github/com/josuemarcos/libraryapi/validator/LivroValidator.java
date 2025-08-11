package github.com.josuemarcos.libraryapi.validator;


import github.com.josuemarcos.libraryapi.exceptions.RegistroDuplicadoException;
import github.com.josuemarcos.libraryapi.model.Livro;
import github.com.josuemarcos.libraryapi.repository.LivroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class LivroValidator {
    private final LivroRepository livroRepository;

    public void validarLivro(Livro livro) {
        if(existeLivroSalvo(livro)) {
            throw new RegistroDuplicadoException("ISBN já cadastrado");
        }
    }

    public boolean existeLivroSalvo(Livro livro) {
        Optional<Livro> livroEncontrado = livroRepository.findByIsbn(livro.getIsbn());
        if(livro.getId() == null) {
            return livroEncontrado.isPresent();
        }

        return livroEncontrado
                .map(Livro::getId)
                .stream()
                .anyMatch(id -> !id.equals(livro.getId()));
    }
}
