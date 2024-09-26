package com.contas.pagar.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ValorTotalPagoResponse {
    private BigDecimal valorTotalPagoPorPeriodo;
}
