package github.com.josuemarcos.libraryapi.validator;
import github.com.josuemarcos.libraryapi.controller.dto.CadastroLivroDTO;
import github.com.josuemarcos.libraryapi.controller.mapper.LivroMapper;
import github.com.josuemarcos.libraryapi.model.Livro;
import github.com.josuemarcos.libraryapi.repository.LivroRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class LivroValidator implements ConstraintValidator<ValidBook, CadastroLivroDTO> {

    private static final int ANO_EXIGENCIA_PRECO = 2020;

    private final LivroRepository livroRepository;
    private final LivroMapper livroMapper;

    @Override
    public boolean isValid(CadastroLivroDTO livroDTO, ConstraintValidatorContext context) {
        if (livroDTO == null) {
            return true;
        }

        boolean valid = true;
        context.disableDefaultConstraintViolation();


         //Logic to verify if the ISBN is already registered
        if(existeLivroSalvo(livroDTO)) {
            context.buildConstraintViolationWithTemplate("ISBN já cadastrado")
                    .addPropertyNode("isbn")
                    .addConstraintViolation();
            valid = false;
        }

         //Logic to verify if the price is informed when the publication year is after 2020
        if(isPrecoObrigatorioNulo(livroDTO)) {
            context.buildConstraintViolationWithTemplate("Para livros com ano de publicação após 2020, o preço é obrigatório!")
                    .addPropertyNode("preco")
                    .addConstraintViolation();
            valid = false;
        }
        return valid;
    }

    private boolean existeLivroSalvo(CadastroLivroDTO livroDTO) {
        Livro livroEntidade = livroMapper.toEntity(livroDTO);
        Optional<Livro> livroEncontrado = livroRepository.findByIsbn(livroDTO.isbn());
        if(livroEntidade.getId() == null) {
            return livroEncontrado.isPresent();
        }

        return livroEncontrado
                .map(Livro::getId)
                .stream()
                .anyMatch(id -> !id.equals(livroEntidade.getId()));
    }

    private boolean isPrecoObrigatorioNulo(CadastroLivroDTO livroDTO) {
        return livroDTO.preco() == null &&
                livroDTO.dataPublicacao().getYear() >= ANO_EXIGENCIA_PRECO;
    }

}
