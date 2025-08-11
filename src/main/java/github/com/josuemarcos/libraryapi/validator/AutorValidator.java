package github.com.josuemarcos.libraryapi.validator;

import github.com.josuemarcos.libraryapi.exceptions.RegistroDuplicadoException;
import github.com.josuemarcos.libraryapi.model.Autor;
import github.com.josuemarcos.libraryapi.repository.AutorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AutorValidator {
    private final AutorRepository repository;

    public void validar(Autor autor) {
        if (existeAutorSalvo(autor)) {
            throw new RegistroDuplicadoException("Autor já cadastrado!");
        }
    }

    public boolean existeAutorSalvo(Autor autor){
        Optional<Autor> autorEncontrado = repository.findByNomeAndDataNascimentoAndNacionalidade(
                autor.getNome(), autor.getDataNascimento(), autor.getNacionalidade()
        );

        if(autor.getId() == null) {
            return autorEncontrado.isPresent();
        }

        return autorEncontrado
                .map(Autor::getId)
                .stream()
                .anyMatch(id -> !id.equals(autor.getId()));
        //Solução sem map/stream
       // return autorEncontrado.isPresent() && !autor.getId().equals(autorEncontrado.get().getId());
    }
}
