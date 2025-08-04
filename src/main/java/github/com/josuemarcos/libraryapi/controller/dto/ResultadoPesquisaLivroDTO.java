package github.com.josuemarcos.libraryapi.controller.dto;

import github.com.josuemarcos.libraryapi.model.GeneroLivro;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record ResultadoPesquisaLivroDTO(
        UUID id,
        String isbn,
        String titulo,
        LocalDate dataPublicacao,
        GeneroLivro generoLivro,
        BigDecimal preco,
        AutorDTO autor
) {
}
