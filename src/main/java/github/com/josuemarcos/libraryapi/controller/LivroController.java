package github.com.josuemarcos.libraryapi.controller;

import com.fasterxml.jackson.databind.deser.CreatorProperty;
import github.com.josuemarcos.libraryapi.controller.dto.CadastroLivroDTO;
import github.com.josuemarcos.libraryapi.controller.dto.ResultadoPesquisaLivroDTO;
import github.com.josuemarcos.libraryapi.controller.mapper.LivroMapper;
import github.com.josuemarcos.libraryapi.model.Livro;
import github.com.josuemarcos.libraryapi.service.LivroService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/livros")
public class LivroController implements GenericController{
    private final LivroService service;
    private final LivroMapper mapper;

    @PostMapping
    public ResponseEntity<Object> salvar(@RequestBody @Valid CadastroLivroDTO dto) {
        Livro livro = mapper.toEntity(dto);
        service.salvar(livro);
        URI location = gerarHeaderLocation(livro.getId());
        return ResponseEntity.created(location).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResultadoPesquisaLivroDTO> encontrarLivroPorId(@PathVariable(name = "id") String id) {
        return service.encontrarLivroPorId(UUID.fromString(id))
                .map(livro -> {
                    ResultadoPesquisaLivroDTO livroDTO = mapper.toDTO(livro);
                    return ResponseEntity.ok(livroDTO);
                }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarLivro(@PathVariable(name = "id") String id) {
        Optional<Livro> livroOptional = service.encontrarLivroPorId(UUID.fromString(id));
        if(livroOptional.isPresent()) {
            service.deletarLivro(livroOptional.get());
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }


}
