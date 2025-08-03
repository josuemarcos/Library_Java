package github.com.josuemarcos.libraryapi.service;

import github.com.josuemarcos.libraryapi.exceptions.OperacaoNaoPermitidaException;
import github.com.josuemarcos.libraryapi.model.Autor;
import github.com.josuemarcos.libraryapi.repository.AutorRepository;
import github.com.josuemarcos.libraryapi.repository.LivroRepository;
import github.com.josuemarcos.libraryapi.validator.AutorValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AutorService {

    private final AutorRepository repository;
    private final AutorValidator validator;
    private final LivroRepository livroRepository;

    public Autor salvar(Autor autor) {
        validator.validar(autor);
        return repository.save(autor);
    }

    public Optional<Autor> encontrarAutorPorId(UUID id) {
        return repository.findById(id);
    }

    public void deletarAutor(Autor autor) {
        if(possuiLivro(autor)) {
            throw new OperacaoNaoPermitidaException("Não é possível deletar um autor que possui livros cadastrados!");
        }
        repository.delete(autor);
    }

    public List<Autor> pesquisarAutores(String nome, String nacionalidade) {
        if(nome != null && nacionalidade != null) {
            return repository.findByNomeAndNacionalidade(nome, nacionalidade);
        } else if (nome != null) {
            return repository.findByNome(nome);
        } else if (nacionalidade != null) {
            return repository.findByNacionalidade(nacionalidade);
        }
        return repository.findAll();
    }

    public void atualizarAutor(Autor autor) {
        validator.validar(autor);
        repository.save(autor);
    }

    public boolean possuiLivro(Autor autor) {
        return livroRepository.existsByAutor(autor);
    }
}
