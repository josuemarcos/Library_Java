package github.com.josuemarcos.libraryapi.controller.mapper;

import github.com.josuemarcos.libraryapi.controller.dto.CadastroLivroDTO;
import github.com.josuemarcos.libraryapi.model.Livro;
import github.com.josuemarcos.libraryapi.repository.AutorRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class LivroMapper {

    @Autowired
    AutorRepository autorRepository;


    @Mapping(target = "autor", expression = "java(autorRepository.findById(dto.idAutor()).orElse(null))")
    public abstract Livro toEntity(CadastroLivroDTO dto);


}
