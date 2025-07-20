package github.com.josuemarcos.libraryapi.controller;

import github.com.josuemarcos.libraryapi.controller.dto.AutorDTO;
import github.com.josuemarcos.libraryapi.model.Autor;
import github.com.josuemarcos.libraryapi.service.AutorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/autores")
public class AutorController {

    private final AutorService service;


    public AutorController(AutorService service) {
        this.service = service;
    }



    @PostMapping
    public ResponseEntity<Void> salvar(@RequestBody AutorDTO autor) {
        Autor autorEntidade = autor.mapearParaAutor();
        service.salvar(autorEntidade);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(autorEntidade.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @GetMapping("{id}")
    public ResponseEntity<AutorDTO> buscarAutorPorId(@PathVariable(name = "id") String id) {
        var idAutor = UUID.fromString(id);
        Optional<Autor> autorOptional = service.encontrarAutorPorId(idAutor);
        if(autorOptional.isPresent()) {
            Autor autor = autorOptional.get();
            AutorDTO dto = new AutorDTO(
                    autor.getId(),
                    autor.getNome(),
                    autor.getDataNascimento(),
                    autor.getNacionalidade()
            );
            return ResponseEntity.ok(dto);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deletarAutor(@PathVariable(name = "id") String id) {
        var idAutor = UUID.fromString(id);
        Optional<Autor> autorOptional = service.encontrarAutorPorId(idAutor);
        if(autorOptional.isPresent()) {
            service.deletarAutor(idAutor);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<AutorDTO>> pesquisarAutores(@RequestParam(value = "nome", required = false) String nome,
                                                           @RequestParam(value = "nacionalidade", required = false) String nacionalidade) {
        List<Autor> autores = service.pesquisarAutores(nome, nacionalidade);

        List<AutorDTO> autoresDTO = autores
                .stream()
                .map(autor -> new AutorDTO(
                autor.getId(),
                autor.getNome(),
                autor.getDataNascimento(),
                autor.getNacionalidade()
        )).toList();
        return ResponseEntity.ok(autoresDTO);
    }

    @PutMapping("{id}")
    public ResponseEntity<Void> atualizarAutor(
            @PathVariable(name = "id") String id,
            @RequestBody AutorDTO autorDTO
    ) {
        var idAutor = UUID.fromString(id);
        Optional<Autor> autorOptional = service.encontrarAutorPorId(idAutor);
        if(autorOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Autor autor = autorOptional.get();
        autor.setNome(autorDTO.nome());
        autor.setDataNascimento(autorDTO.dataNascimento());
        autor.setNacionalidade(autorDTO.nacionalidade());
        service.atualizarAutor(autor);
        return ResponseEntity.noContent().build();
    }
}
