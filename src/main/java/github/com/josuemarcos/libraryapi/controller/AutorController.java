package github.com.josuemarcos.libraryapi.controller;

import github.com.josuemarcos.libraryapi.controller.dto.AutorDTO;
import github.com.josuemarcos.libraryapi.model.Autor;
import github.com.josuemarcos.libraryapi.service.AutorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

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
}
