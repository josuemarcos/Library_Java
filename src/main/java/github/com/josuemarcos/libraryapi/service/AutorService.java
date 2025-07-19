package github.com.josuemarcos.libraryapi.service;

import github.com.josuemarcos.libraryapi.model.Autor;
import github.com.josuemarcos.libraryapi.repository.AutorRepository;
import org.springframework.stereotype.Service;

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
}
