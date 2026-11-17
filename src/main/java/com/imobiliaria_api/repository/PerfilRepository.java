package com.imobiliaria_api.repository;

import com.imobiliaria_api.entity.Perfil;
import com.imobiliaria_api.entity.PerfilNome;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PerfilRepository extends JpaRepository<Perfil, Integer> {

    Optional<Perfil> findByNome(PerfilNome nome);

    List<Perfil> findAllByOrderByNomeAsc();
}
