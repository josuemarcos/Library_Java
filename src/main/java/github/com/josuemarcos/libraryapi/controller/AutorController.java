package github.com.josuemarcos.libraryapi.controller;

import github.com.josuemarcos.libraryapi.controller.dto.AutorDTO;
import github.com.josuemarcos.libraryapi.controller.dto.ErroResposta;
import github.com.josuemarcos.libraryapi.exceptions.OperacaoNaoPermitidaException;
import github.com.josuemarcos.libraryapi.exceptions.RegistroDuplicadoException;
import github.com.josuemarcos.libraryapi.model.Autor;
import github.com.josuemarcos.libraryapi.service.AutorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/autores")
@RequiredArgsConstructor
public class AutorController {

    private final AutorService service;


    @PostMapping
    public ResponseEntity<Object> salvar(@RequestBody @Valid AutorDTO autor) {

        try{
            Autor autorEntidade = autor.mapearParaAutor();
            service.salvar(autorEntidade);
            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(autorEntidade.getId())
                    .toUri();

            return ResponseEntity.created(location).build();
        } catch (RegistroDuplicadoException e) {
            var erroDTO = ErroResposta.erroConflito(e.getMessage());
            return ResponseEntity.status(erroDTO.status()).body(erroDTO);
        }

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
    public ResponseEntity<Object> deletarAutor(@PathVariable(name = "id") String id) {
        try {
            var idAutor = UUID.fromString(id);
            Optional<Autor> autorOptional = service.encontrarAutorPorId(idAutor);
            if(autorOptional.isPresent()) {
                service.deletarAutor(autorOptional.get());
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.notFound().build();
        } catch(OperacaoNaoPermitidaException e) {
            var erroDTO = ErroResposta.erroPadrao(e.getMessage());
            return ResponseEntity.status(erroDTO.status()).body(erroDTO);
        }
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
    public ResponseEntity<Object> atualizarAutor(
            @PathVariable(name = "id") String id,
            @RequestBody AutorDTO autorDTO
    ) {
        try{
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
        } catch (RegistroDuplicadoException e) {
            var erroDto = ErroResposta.erroConflito(e.getMessage());
            return ResponseEntity.status(erroDto.status()).body(erroDto);
        }
    }
}
