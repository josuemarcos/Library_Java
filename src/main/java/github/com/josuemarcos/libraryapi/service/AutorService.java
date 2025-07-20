package github.com.josuemarcos.libraryapi.service;

import github.com.josuemarcos.libraryapi.model.Autor;
import github.com.josuemarcos.libraryapi.repository.AutorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AutorService {

    private final AutorRepository repository;

    public AutorService(AutorRepository repository) {
        this.repository = repository;
    }

    public Autor salvar(Autor autor) {
       return repository.save(autor);
    }

    public Optional<Autor> encontrarAutorPorId(UUID id) {
        return repository.findById(id);
    }

    public void deletarAutor(UUID id) {
        repository.deleteById(id);
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
}
