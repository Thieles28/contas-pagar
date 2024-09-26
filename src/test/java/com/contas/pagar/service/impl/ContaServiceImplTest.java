package com.contas.pagar.service.impl;

import com.contas.pagar.entity.Conta;
import com.contas.pagar.entity.Situacao;
import com.contas.pagar.model.factory.ContaFactory;
import com.contas.pagar.model.request.ContaRequest;
import com.contas.pagar.model.response.ContaResponse;
import com.contas.pagar.model.response.MensagemResponse;
import com.contas.pagar.model.response.ValorTotalPagoResponse;
import com.contas.pagar.repository.ContaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.contas.pagar.model.factory.ContaFactory.criarConta;
import static com.contas.pagar.model.factory.ContaFactory.entradaConta;
import static com.contas.pagar.model.factory.ContaFactory.saidaConta;
import static com.contas.pagar.utils.Mensagens.CONTAS_IMPORTADAS_SUCESSO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ContaServiceImplTest {

    @Mock
    private ContaRepository mockContaRepository;
    @Mock
    private ModelMapper mockModelMapper;

    @InjectMocks
    private ContaServiceImpl contaServiceImplUnderTest;

    @Mock
    private MultipartFile file;

    @BeforeEach
    void setUp() {
        contaServiceImplUnderTest = new ContaServiceImpl(mockContaRepository, mockModelMapper);
    }

    @Test
    void testRegistrarContas() {
        final ContaRequest contaRequest = entradaConta();
        final ContaResponse expectedResult = saidaConta();
        final Conta conta = criarConta();

        when(mockModelMapper.map(contaRequest, Conta.class)).thenReturn(conta);

        final Conta contaSalva = criarConta();
        when(mockContaRepository.save(conta)).thenReturn(contaSalva);

        final ContaResponse contaResponse = saidaConta();
        when(mockModelMapper.map(contaSalva, ContaResponse.class)).thenReturn(contaResponse);

        final ContaResponse result = contaServiceImplUnderTest.registrarContas(contaRequest);

        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void testRecuperarContas() {
        final LocalDate dataVencimento = LocalDate.of(2020, 1, 1);
        final String descricao = "Pagamento de Internet";
        final Pageable pageable = PageRequest.of(0, 1);

        final Conta conta = ContaFactory.criarContaDataFixa();
        when(mockContaRepository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(List.of(conta)));

        final ContaResponse contaResponse = ContaFactory.saidaConta();
        when(mockModelMapper.map(conta, ContaResponse.class)).thenReturn(contaResponse);

        final Page<ContaResponse> result = contaServiceImplUnderTest.recuperarContas(dataVencimento, descricao, pageable);

        assertThat(result).isNotNull();
        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent()).containsExactly(contaResponse);

        verify(mockContaRepository).findAll(any(Pageable.class));
        verify(mockModelMapper).map(conta, ContaResponse.class);
    }


    @Test
    void testRecuperarContas_ContaRepositoryReturnsNoItems() {
        when(mockContaRepository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(Collections.emptyList()));

        final Page<ContaResponse> result = contaServiceImplUnderTest.recuperarContas(LocalDate.of(2020, 1, 1),
                "descricao", PageRequest.of(0, 1));

        assertThat(result).isNotNull();
    }

    @Test
    void testObterConta() {
        final ContaResponse expectedResult = saidaConta();
        final Optional<Conta> conta = Optional.of(criarConta());
        when(mockContaRepository.findById(0L)).thenReturn(conta);
        final ContaResponse contaResponse = saidaConta();
        when(mockModelMapper.map(criarConta(), ContaResponse.class)).thenReturn(contaResponse);
        final ContaResponse result = contaServiceImplUnderTest.obterConta(0L);
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void testObterConta_ContaRepositoryReturnsAbsent() {
        when(mockContaRepository.findById(0L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> contaServiceImplUnderTest.obterConta(0L)).isInstanceOf(ResponseStatusException.class);
    }


    @Test
    void testAtualizarConta_ContaRepositoryFindByIdReturnsAbsent() {
        final ContaRequest contaRequest = ContaRequest.builder().build();
        when(mockContaRepository.findById(0L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> contaServiceImplUnderTest.atualizarConta(contaRequest, 0L))
                .isInstanceOf(ResponseStatusException.class);
    }

    @Test
    void testRemoveConta() {
        final ContaResponse contaResponse = saidaConta();

        final Conta conta = criarConta();
        when(mockModelMapper.map(saidaConta(), Conta.class)).thenReturn(conta);

        contaServiceImplUnderTest.removeConta(contaResponse);

        verify(mockContaRepository).delete(criarConta());
    }

    @Test
    void testAlterarSituacao_ContaRepositoryFindByIdReturnsAbsent() {
        when(mockContaRepository.findById(0L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> contaServiceImplUnderTest.alterarSituacao(0L, Situacao.PENDENTE))
                .isInstanceOf(ResponseStatusException.class);
    }

    @Test
    void testObterValorTotalPagoPorPeriodo() {
        final ValorTotalPagoResponse expectedResult = ValorTotalPagoResponse.builder()
                .valorTotalPagoPorPeriodo(new BigDecimal("0.00"))
                .build();
        when(mockContaRepository.obterValorTotalPagoPorPeriodo(LocalDate.of(2020, 1, 1),
                LocalDate.of(2020, 1, 1))).thenReturn(new BigDecimal("0.00"));

        final ValorTotalPagoResponse result = contaServiceImplUnderTest.obterValorTotalPagoPorPeriodo(
                LocalDate.of(2020, 1, 1), LocalDate.of(2020, 1, 1));

        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void testImportarContasCsv() throws Exception {
        String csvData = "dataVencimento,valor,descricao\n" +
                "01/01/2024,100.00,Conta de teste 1\n" +
                "02/01/2024,200.00,Conta de teste 2\n";

        InputStream inputStream = new ByteArrayInputStream(csvData.getBytes());
        when(file.getInputStream()).thenReturn(inputStream);

        when(mockContaRepository.saveAll(anyList())).thenReturn(Arrays.asList(new Conta(), new Conta()));

        MensagemResponse response = contaServiceImplUnderTest.importarContasCsv(file);

        assertEquals(CONTAS_IMPORTADAS_SUCESSO, response.getMensagem());

        verify(mockContaRepository, times(1)).saveAll(anyList());
    }

    @Test
    void testImportarContasCsv_Exception() throws Exception {
        when(file.getInputStream()).thenThrow(new RuntimeException("Erro ao ler arquivo"));

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            contaServiceImplUnderTest.importarContasCsv(file);
        });

        assertEquals("Erro ao importar contas: Erro ao ler arquivo", thrown.getMessage());
    }

}
