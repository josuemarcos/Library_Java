package github.com.josuemarcos.libraryapi.service;

import github.com.josuemarcos.libraryapi.repository.LivroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LivroService {
    private final LivroRepository repository;
}
