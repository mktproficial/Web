package com.imobiliaria_api.repository;

import com.imobiliaria_api.entity.Locacao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocacaoRepository extends JpaRepository<Locacao, Integer> {
}
