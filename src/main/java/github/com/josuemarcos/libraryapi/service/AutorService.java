package github.com.josuemarcos.libraryapi.service;

import github.com.josuemarcos.libraryapi.exceptions.OperacaoNaoPermitidaException;
import github.com.josuemarcos.libraryapi.model.Autor;
import github.com.josuemarcos.libraryapi.repository.AutorRepository;
import github.com.josuemarcos.libraryapi.repository.LivroRepository;
import github.com.josuemarcos.libraryapi.validator.AutorValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
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
        Autor autor = new Autor();
        autor.setNome(nome);
        autor.setNacionalidade(nacionalidade);

        ExampleMatcher matcher = ExampleMatcher
                .matching()
                .withIgnoreNullValues()
                .withIgnoreCase()
                .withIgnorePaths("id", "dataNascimento", "dataCadastro")
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);

        Example<Autor> autorExample = Example.of(autor, matcher);
        return repository.findAll(autorExample);
    }

    public void atualizarAutor(Autor autor) {
        validator.validar(autor);
        repository.save(autor);
    }

    public boolean possuiLivro(Autor autor) {
        return livroRepository.existsByAutor(autor);
    }
}
