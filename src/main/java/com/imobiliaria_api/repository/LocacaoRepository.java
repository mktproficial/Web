package com.imobiliaria_api.repository;

import com.imobiliaria_api.entity.Locacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface LocacaoRepository extends JpaRepository<Locacao, Integer> {
    @Query("""
        SELECT CASE WHEN COUNT(l) > 0 THEN true ELSE false END
        FROM Locacao l
        WHERE l.imovel.id = :imovelId
          AND l.ativo = true
          AND l.dataInicio < :dataFim
          AND l.dataFim > :dataInicio
          AND (:locacaoId IS NULL OR l.id <> :locacaoId)
        """)
    boolean existeConflito(
            @Param("imovelId") Integer imovelId,
            @Param("dataInicio") LocalDate dataInicio,
            @Param("dataFim") LocalDate dataFim,
            @Param("locacaoId") Integer locacaoId
    );

    boolean existsByImovel_IdAndAtivoTrueAndDataFimGreaterThanEqual(
            Integer imovelId,
            LocalDate data
    );
}
