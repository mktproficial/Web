package com.imobiliaria_api.repository;

import com.imobiliaria_api.entity.Imovel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImovelRepository extends JpaRepository<Imovel, Integer> {
}