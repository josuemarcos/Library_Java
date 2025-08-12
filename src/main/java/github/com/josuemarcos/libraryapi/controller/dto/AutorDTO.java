package github.com.josuemarcos.libraryapi.controller.dto;

import github.com.josuemarcos.libraryapi.model.Autor;
import github.com.josuemarcos.libraryapi.validator.ValidAuthor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.UUID;

@ValidAuthor
public record AutorDTO(UUID id,
                       @NotBlank(message = "Campo obrigatório")
                       @Size(min = 2, max = 100, message = "Nome fora do tamanho padrão")
                       String nome,
                       @NotNull(message = "Campo obrigatório")
                       @Past(message = "Não pode ser uma data futura!")
                       LocalDate dataNascimento,
                       @NotBlank(message = "Campo obrigatório")
                       @Size(min = 2, max = 50, message = "Nacionalidade fora do tamanho padrão")
                       String nacionalidade) {

}
