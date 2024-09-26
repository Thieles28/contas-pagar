package com.contas.pagar.entity;

import com.contas.pagar.model.factory.ContaFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@DisplayName("Testes para a classe Conta")
class ContaTest {

    private Conta conta;

    @BeforeEach
    void setUp() {
        conta = new Conta();
    }

    @Test
    @DisplayName("Deve verificar valores padrão quando a entidade é criada")
    void testDefaultValues() {
        assertNull(conta.getId());
        assertNull(conta.getDataVencimento());
        assertNull(conta.getDataPagamento());
        assertNull(conta.getValor());
        assertNull(conta.getDescricao());
        assertNull(conta.getSituacao());
    }

    @Test
    @DisplayName("Deve definir e obter corretamente os valores dos atributos")
    void testSettersAndGetters() {
        Conta conta = ContaFactory.criarConta();
        assertNotNull(conta);
        assertEquals(LocalDate.now(), conta.getDataVencimento());
        assertEquals(LocalDate.now(), conta.getDataPagamento());
        assertEquals(BigDecimal.valueOf(99.99), conta.getValor());
        assertEquals("Pagamento de Internet", conta.getDescricao());
        assertEquals(Situacao.PAGA, conta.getSituacao());
    }

    @Test
    @DisplayName("Deve definir e obter corretamente o código da conta")
    void testSetAndGetCodigoCliente() {
        conta.setId(10L);
        assertEquals(10L, conta.getId());
    }

    @Test
    @DisplayName("Deve definir e obter corretamente o valor")
    void testSetAndGetValor() {
        conta.setValor(BigDecimal.valueOf(150.75));
        assertEquals(BigDecimal.valueOf(150.75), conta.getValor());
    }

}