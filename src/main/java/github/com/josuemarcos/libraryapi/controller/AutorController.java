package github.com.josuemarcos.libraryapi.controller;
import github.com.josuemarcos.libraryapi.controller.dto.AutorDTO;
import github.com.josuemarcos.libraryapi.controller.mapper.AutorMapper;
import github.com.josuemarcos.libraryapi.model.Autor;
import github.com.josuemarcos.libraryapi.service.AutorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/autores")
@RequiredArgsConstructor
public class AutorController implements GenericController{

    private final AutorService autorService;
    private final AutorMapper autorMapper;


    @PostMapping
    public ResponseEntity<Void> salvar(@RequestBody @Valid AutorDTO dto) {
        Autor autor = autorMapper.toEntity(dto);
        autorService.salvarAutor(autor);
        URI location = gerarHeaderLocation(autor.getId());
        return ResponseEntity.created(location).build();
    }

    @GetMapping("{id}")
    public ResponseEntity<AutorDTO> buscarAutorPorId(@PathVariable(name = "id") String id) {
        return autorService
                .encontrarAutorPorId(UUID.fromString(id))
                .map(
                        autor -> {
                            AutorDTO dto = autorMapper.toDTO(autor);
                            return ResponseEntity.ok(dto);
                        }
                ).orElseGet(() -> ResponseEntity.notFound().build());

        /*
        ----------------Forma mais simples-----------------------------------
        Optional<Autor> autorOptional = service.encontrarAutorPorId(idAutor);
        if(autorOptional.isPresent()) {
            Autor autor = autorOptional.get();
            AutorDTO dto = mapper.toDTO(autor);
            return ResponseEntity.ok(dto);
        }
        return ResponseEntity.notFound().build();
         */
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deletarAutor(@PathVariable(name = "id") String id) {
        var idAutor = UUID.fromString(id);
        Optional<Autor> autorOptional = autorService.encontrarAutorPorId(idAutor);
        if(autorOptional.isPresent()) {
            autorService.deletarAutor(autorOptional.get());
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<AutorDTO>> pesquisarAutores(@RequestParam(value = "nome", required = false) String nome,
                                                           @RequestParam(value = "nacionalidade", required = false) String nacionalidade) {
        List<Autor> autores = autorService.pesquisarAutoresPorParametros(nome, nacionalidade);

        List<AutorDTO> autoresDTO = autores
                .stream()
                .map(autorMapper::toDTO)
                .toList();
        return ResponseEntity.ok(autoresDTO);
    }

    @PutMapping("{id}")
    public ResponseEntity<Void> atualizarAutor(
            @PathVariable(name = "id") String id,
            @RequestBody AutorDTO autorDTO
    ) {
        var idAutor = UUID.fromString(id);
        Optional<Autor> autorOptional = autorService.encontrarAutorPorId(idAutor);
        if(autorOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Autor autor = autorOptional.get();
        autor.setNome(autorDTO.nome());
        autor.setDataNascimento(autorDTO.dataNascimento());
        autor.setNacionalidade(autorDTO.nacionalidade());
        autorService.atualizarAutor(autor);
        return ResponseEntity.noContent().build();
    }
}
