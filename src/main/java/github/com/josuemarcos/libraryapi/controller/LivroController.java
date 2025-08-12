package github.com.josuemarcos.libraryapi.controller;
import github.com.josuemarcos.libraryapi.controller.dto.CadastroLivroDTO;
import github.com.josuemarcos.libraryapi.controller.dto.ResultadoPesquisaLivroDTO;
import github.com.josuemarcos.libraryapi.controller.mapper.LivroMapper;
import github.com.josuemarcos.libraryapi.model.GeneroLivro;
import github.com.josuemarcos.libraryapi.model.Livro;
import github.com.josuemarcos.libraryapi.service.LivroService;
import github.com.josuemarcos.libraryapi.validator.ValidBook;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/livros")
public class LivroController implements GenericController{
    private final LivroService livroService;
    private final LivroMapper livroMapper;

    @PostMapping
    public ResponseEntity<Object> salvar(@RequestBody @Valid CadastroLivroDTO dto) {
        Livro livro = livroMapper.toEntity(dto);
        livroService.salvarLivro(livro);
        URI location = gerarHeaderLocation(livro.getId());
        return ResponseEntity.created(location).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResultadoPesquisaLivroDTO> encontrarLivroPorId(@PathVariable(name = "id") String id) {
        return livroService.encontrarLivroPorId(UUID.fromString(id))
                .map(livro -> {
                    ResultadoPesquisaLivroDTO livroDTO = livroMapper.toDTO(livro);
                    return ResponseEntity.ok(livroDTO);
                }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarLivro(@PathVariable(name = "id") String id) {
        Optional<Livro> livroOptional = livroService.encontrarLivroPorId(UUID.fromString(id));
        if(livroOptional.isPresent()) {
            livroService.deletarLivro(livroOptional.get());
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<ResultadoPesquisaLivroDTO>> pesquisarLivros(
            @RequestParam(value = "isbn", required = false)
            String isbn,
            @RequestParam(value = "titulo", required = false)
            String titulo,
            @RequestParam(value = "nome-autor", required = false)
            String nomeAutor,
            @RequestParam(value = "genero", required = false)
            GeneroLivro genero,
            @RequestParam(value = "ano-publicacao", required = false)
            Integer anoPublicacao
    ) {
        List<ResultadoPesquisaLivroDTO> list = livroService.pesquisaLivrosPorParametros(
                isbn, titulo, nomeAutor, genero, anoPublicacao).
                stream().
                map(livroMapper::toDTO).
                toList();
        return ResponseEntity.ok(list);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> atualizarLivro(
            @PathVariable(name = "id") String id, @RequestBody @Valid CadastroLivroDTO dto) {
        return livroService.encontrarLivroPorId(UUID.fromString(id))
                .map(livro -> {
                    Livro entidadeAux = livroMapper.toEntity(dto);
                    livro.setIsbn(entidadeAux.getIsbn());
                    livro.setTitulo(entidadeAux.getTitulo());
                    livro.setDataPublicacao(entidadeAux.getDataPublicacao());
                    livro.setGenero(entidadeAux.getGenero());
                    livro.setPreco(entidadeAux.getPreco());
                    livro.setAutor(entidadeAux.getAutor());
                    livroService.atualizarLivro(livro);
                    return ResponseEntity.noContent().build();
                }).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
