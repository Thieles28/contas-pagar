package com.contas.pagar.service;

import com.contas.pagar.entity.Situacao;
import com.contas.pagar.model.request.ContaRequest;
import com.contas.pagar.model.response.ContaResponse;
import com.contas.pagar.model.response.MensagemResponse;
import com.contas.pagar.model.response.ValorTotalPagoResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

public interface ContaService {
    ContaResponse registrarContas(ContaRequest users);

    Page<ContaResponse> recuperarContas(LocalDate dataVencimento, String descricao, Pageable pageable);

    ContaResponse obterConta(Long id);

    ContaResponse atualizarConta(ContaRequest contaRequest, Long id);

    void removeConta(ContaResponse contaResponse);

    ContaResponse alterarSituacao(Long id, Situacao situacao);

    ValorTotalPagoResponse obterValorTotalPagoPorPeriodo(LocalDate inicio, LocalDate fim);

    MensagemResponse importarContasCsv(MultipartFile file);
}


