package github.com.josuemarcos.libraryapi.service;

import github.com.josuemarcos.libraryapi.model.Livro;
import github.com.josuemarcos.libraryapi.repository.LivroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LivroService {
    private final LivroRepository repository;


    public Livro salvar(Livro livro) {
        return repository.save(livro);
    }

    public Optional<Livro> encontrarLivroPorId(UUID id) {
        return repository.findById(id);
    }

    public void deletarLivro(Livro livro) {
        repository.delete(livro);
    }
}
