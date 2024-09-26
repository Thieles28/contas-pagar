package com.contas.pagar.service.impl;

import com.contas.pagar.entity.Conta;
import com.contas.pagar.entity.Situacao;
import com.contas.pagar.model.request.ContaRequest;
import com.contas.pagar.model.response.ContaResponse;
import com.contas.pagar.model.response.MensagemResponse;
import com.contas.pagar.model.response.ValorTotalPagoResponse;
import com.contas.pagar.repository.ContaRepository;
import com.contas.pagar.service.ContaService;
import com.opencsv.CSVReader;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.contas.pagar.utils.Mensagens.CONTAS_IMPORTADAS_SUCESSO;
import static com.contas.pagar.utils.Mensagens.ERRO_IMPORTACAO_CONTAS;

@Service
@RequiredArgsConstructor
public class ContaServiceImpl implements ContaService {

    private final ContaRepository contaRepository;

    private final ModelMapper modelMapper;

    @Override
    public ContaResponse registrarContas(ContaRequest contaRequest) {
        return modelMapper.map(contaRepository
                .save(modelMapper.map(contaRequest, Conta.class)), ContaResponse.class);
    }

    @Override
    public Page<ContaResponse> recuperarContas(LocalDate dataVencimento, String descricao, Pageable pageable) {
        return contaRepository.findAll(pageable)
                .stream()
                .filter(conta ->
                        (dataVencimento == null || conta.getDataVencimento().equals(dataVencimento))
                          && (descricao == null || conta.getDescricao().contains(descricao))
                )
                .map(conta -> modelMapper.map(conta, ContaResponse.class))
                .collect(Collectors.collectingAndThen(Collectors.toList(), list -> new PageImpl<>(list, pageable, list.size())));
    }

    @Override
    public ContaResponse obterConta(Long id) {
        Conta conta = contaRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Conta não encontrado"));
        return modelMapper.map(conta, ContaResponse.class);
    }

    @Override
    public ContaResponse atualizarConta(ContaRequest contaRequest, Long id) {
        ContaResponse contaResponse = obterConta(id);
        modelMapper.map(contaRequest, contaResponse);

        Conta conta = modelMapper.map(contaResponse, Conta.class);
        contaRepository.save(conta);

        return contaResponse;
    }

    @Override
    public void removeConta(ContaResponse contaResponse) {
        contaRepository.delete(modelMapper.map(contaResponse, Conta.class));
    }

    @Override
    public ContaResponse alterarSituacao(Long id, Situacao situacao) {
        ContaResponse contaResponse = obterConta(id);
        contaResponse.setSituacao(situacao);
        Conta conta = modelMapper.map(contaResponse, Conta.class);
        contaRepository.save(conta);
        return contaResponse;
    }

    @Override
    public ValorTotalPagoResponse obterValorTotalPagoPorPeriodo(LocalDate inicio, LocalDate fim) {
        BigDecimal valorTotal = contaRepository.obterValorTotalPagoPorPeriodo(inicio, fim);
        BigDecimal valorFinal = Objects.isNull(valorTotal) ? BigDecimal.ZERO : valorTotal;
        return ValorTotalPagoResponse.builder()
                .valorTotalPagoPorPeriodo(valorFinal)
                .build();
    }

    @Override
    public MensagemResponse importarContasCsv(MultipartFile file) {
        try (CSVReader csvReader = new CSVReader(new InputStreamReader(file.getInputStream()))) {
            csvReader.readNext();

            String[] nextLine;
            List<Conta> contas = new ArrayList<>();
            while ((nextLine = csvReader.readNext()) != null) {
                Conta conta = new Conta();
                conta.setDataVencimento(LocalDate.parse(nextLine[0], DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                conta.setValor(new BigDecimal(nextLine[1]));
                conta.setDescricao(nextLine[2]);
                conta.setSituacao(Situacao.PENDENTE);
                contas.add(conta);
            }
            contaRepository.saveAll(contas);

            return new MensagemResponse(CONTAS_IMPORTADAS_SUCESSO);
        } catch (Exception e) {
            throw new RuntimeException(ERRO_IMPORTACAO_CONTAS + e.getMessage());
        }
    }

}
