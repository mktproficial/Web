package com.imobiliaria_api.repository;

import com.imobiliaria_api.entity.Aluguel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AluguelRepository extends JpaRepository<Aluguel, Integer> {
}