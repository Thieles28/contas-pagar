package com.contas.pagar.model.factory;

import com.contas.pagar.domain.Role;
import com.contas.pagar.entity.Conta;
import com.contas.pagar.entity.Situacao;
import com.contas.pagar.entity.Usuario;
import com.contas.pagar.model.request.ContaRequest;
import com.contas.pagar.model.response.ContaResponse;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ContaFactory {

    private static final Long DEFAULT_ID = 1L;
    private static final BigDecimal DEFAULT_VALOR = BigDecimal.valueOf(99.99);
    private static final String DEFAULT_DESCRICAO = "Pagamento de Internet";
    private static final Situacao DEFAULT_SITUACAO = Situacao.PAGA;
    
    public static Conta criarConta() {
        return Conta.builder()
                .id(DEFAULT_ID)
                .dataVencimento(LocalDate.now())
                .dataPagamento(LocalDate.now())
                .valor(DEFAULT_VALOR)
                .descricao(DEFAULT_DESCRICAO)
                .situacao(DEFAULT_SITUACAO)
                .build();
    }

    public static Conta criarContaDataFixa() {
        return Conta.builder()
                .id(DEFAULT_ID)
                .dataVencimento(LocalDate.of(2020, 1, 1))
                .dataPagamento(LocalDate.now())
                .valor(DEFAULT_VALOR)
                .descricao(DEFAULT_DESCRICAO)
                .situacao(DEFAULT_SITUACAO)
                .build();
    }

    public static Usuario criarUsuario() {
        return Usuario.builder()
                .id(DEFAULT_ID)
                .nome("Pedro")
                .email("pedro@gmail.com")
                .senha("1234")
                .role(Role.USER)
                .build();
    }

    public static ContaRequest entradaConta() {
        return ContaRequest.builder()
                .id(DEFAULT_ID)
                .dataVencimento(LocalDate.now())
                .dataPagamento(LocalDate.now())
                .valor(DEFAULT_VALOR)
                .descricao(DEFAULT_DESCRICAO)
                .situacao(DEFAULT_SITUACAO)
                .build();
    }

    public static ContaResponse saidaConta() {
        return ContaResponse.builder()
                .id(DEFAULT_ID)
                .dataVencimento(LocalDate.now())
                .dataPagamento(LocalDate.now())
                .valor(DEFAULT_VALOR)
                .descricao(DEFAULT_DESCRICAO)
                .situacao(DEFAULT_SITUACAO)
                .build();
    }
}
