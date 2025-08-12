package github.com.josuemarcos.libraryapi.validator;
import github.com.josuemarcos.libraryapi.controller.dto.AutorDTO;
import github.com.josuemarcos.libraryapi.controller.mapper.AutorMapper;
import github.com.josuemarcos.libraryapi.model.Autor;
import github.com.josuemarcos.libraryapi.repository.AutorRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import java.util.Optional;


@RequiredArgsConstructor
public class AutorValidator implements ConstraintValidator<ValidAuthor, AutorDTO> {
    private final AutorRepository repository;
    private final AutorMapper autorMapper;

    @Override
    public boolean isValid(AutorDTO autorDTO, ConstraintValidatorContext context) {
        if(autorDTO == null) return true;

        boolean valid = true;
        context.disableDefaultConstraintViolation();

        if(existeAutorSalvo(autorDTO)) {
            context.buildConstraintViolationWithTemplate("Autor já cadastrado")
                    .addPropertyNode("nome")
                    .addPropertyNode("dataNascimento")
                    .addPropertyNode("nacionalidade")
                    .addConstraintViolation();
            valid = false;
        }

        return valid;
    }


    public boolean existeAutorSalvo(AutorDTO autorDTO){
        Autor autorEntidade = autorMapper.toEntity(autorDTO);
        Optional<Autor> autorEncontrado = repository.findByNomeAndDataNascimentoAndNacionalidade(
                autorDTO.nome(), autorDTO.dataNascimento(), autorDTO.nacionalidade()
        );

        if(autorEntidade.getId() == null) {
            return autorEncontrado.isPresent();
        }

        return autorEncontrado
                .map(Autor::getId)
                .stream()
                .anyMatch(id -> !id.equals(autorEntidade.getId()));
        //Solution without map/stream
       // return autorEncontrado.isPresent() && !autor.getId().equals(autorEncontrado.get().getId());
    }



}
