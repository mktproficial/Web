package com.imobiliaria_api.repository;

import com.imobiliaria_api.entity.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    @EntityGraph(attributePaths = {"perfis", "cliente"})
    Optional<Usuario> findByLogin(String login);

    @EntityGraph(attributePaths = {"perfis", "cliente"})
    Optional<Usuario> findById(Integer id);

    @EntityGraph(attributePaths = {"perfis", "cliente"})
    Page<Usuario> findAll(Pageable pageable);

    boolean existsByLogin(String login);

    boolean existsByLoginAndIdNot(String login, Integer id);

    boolean existsByClienteId(Integer clienteId);

    boolean existsByClienteIdAndIdNot(Integer clienteId, Integer id);
}