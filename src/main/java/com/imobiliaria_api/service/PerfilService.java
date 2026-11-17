package com.imobiliaria_api.service;

import com.imobiliaria_api.entity.Perfil;
import com.imobiliaria_api.repository.PerfilRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PerfilService {

    private final PerfilRepository repository;

    public PerfilService(PerfilRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<Perfil> listar() {
        return repository.findAllByOrderByNomeAsc();
    }
}