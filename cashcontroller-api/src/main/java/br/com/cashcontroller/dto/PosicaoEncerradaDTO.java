package br.com.cashcontroller.dto;

import lombok.*;

import java.time.LocalDate;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PosicaoEncerradaDTO {


        private LocalDate dataInicio;
        private String nomeAtivo;
        private Long soma;
        private LocalDate dataEncerramento;
        private double valorInvestido;
        private double valorVenda;

        @Setter(AccessLevel.NONE)
        private double resultadoAtivo;

        public double getResultadoAtivo() {
                return valorVenda - valorInvestido;
        }

        public PosicaoEncerradaDTO(LocalDate dataInicio, String nomeAtivo, Long soma, LocalDate dataEncerramento, double valorInvestido, double valorVenda) {
                this.dataInicio = dataInicio;
                this.nomeAtivo = nomeAtivo;
                this.soma = soma;
                this.dataEncerramento = dataEncerramento;
                this.valorInvestido = valorInvestido;
                this.valorVenda = valorVenda;
        }
}
