package com.contas.pagar.controller;

import com.contas.pagar.model.request.ContaRequest;
import com.contas.pagar.model.response.ContaResponse;
import com.contas.pagar.model.response.MensagemResponse;
import com.contas.pagar.model.response.ValorTotalPagoResponse;
import com.contas.pagar.service.ContaService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.time.LocalDate;

@RestController
@RequestMapping("contas")
@RequiredArgsConstructor
@Validated
public class ContaController {
    private final ContaService contaService;

    @GetMapping
    public ResponseEntity<Page<ContaResponse>> obterContas(@RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate dataVencimento,
                                                           @RequestParam(required = false) String descricao,
                                                           Pageable pageable) {
        return ResponseEntity.ok(contaService.recuperarContas(dataVencimento, descricao, pageable));
    }

    @GetMapping("{id}")
    public ResponseEntity<ContaResponse> obterConta(@PathVariable("id") Long id) {
        return ResponseEntity.ok(contaService.obterConta(id));
    }

    @PostMapping
    public ResponseEntity<ContaResponse> registrarContas(@Valid @RequestBody ContaRequest contaRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(contaService.registrarContas(contaRequest));
    }

    @PutMapping("{id}")
    public ResponseEntity<ContaResponse> atualizarConta(@RequestBody ContaRequest contaRequest, @PathVariable("id") Long id) {
        return  ResponseEntity.status(HttpStatus.CREATED).body(contaService.atualizarConta(contaRequest, id));
    }

    @PatchMapping("{id}/situacao")
    public ResponseEntity<ContaResponse> alterarSituacaoConta(@PathVariable Long id, @RequestBody ContaRequest contaRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(contaService.alterarSituacao(id, contaRequest.getSituacao()));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<ContaResponse> removeConta(@PathVariable Long id){
        ContaResponse usuarioResponse = contaService.obterConta(id);
        contaService.removeConta(usuarioResponse);
        return  ResponseEntity.status(HttpStatus.NO_CONTENT).body(usuarioResponse);
    }

    @GetMapping("valor-total-pago")
    public ResponseEntity<ValorTotalPagoResponse> obterValorTotalPagoPorPeriodo(
            @RequestParam("inicio") @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate inicio,
            @RequestParam("fim") @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate fim) {
        return ResponseEntity.ok(contaService.obterValorTotalPagoPorPeriodo(inicio, fim));
    }

    @PostMapping("importar")
    public ResponseEntity<MensagemResponse> importarContas(@RequestParam("file") MultipartFile file) {
        MensagemResponse mensagemResponse = contaService.importarContasCsv(file);
        return ResponseEntity.status(HttpStatus.CREATED).body(mensagemResponse);
    }
}
