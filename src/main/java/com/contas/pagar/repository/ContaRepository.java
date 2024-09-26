package com.contas.pagar.repository;

import com.contas.pagar.entity.Conta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface ContaRepository extends JpaRepository<Conta, Long> {
    @Query("SELECT SUM(c.valor) FROM Conta c WHERE c.dataPagamento BETWEEN :inicio AND :fim AND c.situacao = 'PAGA'")
    BigDecimal obterValorTotalPagoPorPeriodo(@Param("inicio") LocalDate inicio, @Param("fim") LocalDate fim);
}
