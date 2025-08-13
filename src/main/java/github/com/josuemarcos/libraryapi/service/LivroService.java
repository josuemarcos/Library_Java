package github.com.josuemarcos.libraryapi.service;

import github.com.josuemarcos.libraryapi.model.GeneroLivro;
import github.com.josuemarcos.libraryapi.model.Livro;
import github.com.josuemarcos.libraryapi.repository.LivroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import static github.com.josuemarcos.libraryapi.repository.specs.LivroSpecs.*;

@Service
@RequiredArgsConstructor
public class LivroService {
    private final LivroRepository livroRepository;


    public Livro salvarLivro(Livro livro) {
        return livroRepository.save(livro);
    }

    public Optional<Livro> encontrarLivroPorId(UUID id) {
        return livroRepository.findById(id);
    }

    public void deletarLivro(Livro livro) {
        livroRepository.delete(livro);
    }

    public Page<Livro> pesquisaLivrosPorParametros(String isbn,
                                                   String titulo,
                                                   String nomeAutor,
                                                   GeneroLivro genero,
                                                   Integer anoPublicacao,
                                                   Integer pagina,
                                                   Integer tamanhoPagina) {

        Specification<Livro> specs = ((root, query, cb) -> cb.conjunction());

        if(isbn != null) {
            specs = specs.and(isbnEqual(isbn));
        }

        if(titulo != null) {
            specs = specs.and(tituloLike(titulo));
        }

        if(genero != null) {
            specs = specs.and(generoEqual(genero));
        }

        if(anoPublicacao != null) {
            specs = specs.and(anoPublicacaoEqual(anoPublicacao));
        }

        if(nomeAutor != null) {
            specs = specs.and(nomeAutorLike(nomeAutor));
        }

        Pageable pageRequest = PageRequest.of(pagina, tamanhoPagina);

        return livroRepository.findAll(specs,  pageRequest);

    }

    public void atualizarLivro(Livro livro) {
        livroRepository.save(livro);
    }
}
