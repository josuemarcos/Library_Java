package github.com.josuemarcos.libraryapi.controller.dto;

import github.com.josuemarcos.libraryapi.model.Autor;

import java.time.LocalDate;
import java.util.UUID;

public record AutorDTO(UUID id,
                       String nome,
                       LocalDate dataNascimento,
                       String nacionalidade) {

    public Autor mapearParaAutor() {
        Autor autor = new Autor();
        autor.setNome(this.nome);
        autor.setDataNascimento(this.dataNascimento);
        autor.setNacionalidade(this.nacionalidade);
        return autor;
    }
}
