package edu.infnet;

public class CalculadoraReembolso {
    private int porcentualDeCobertura;

    public int getPorcentualDeCobertura() {
        return porcentualDeCobertura;
    }

    public void setPorcentualDeCobertura(int porcentualDeCobertura) {
        this.porcentualDeCobertura = porcentualDeCobertura;
    }


    public double calcularValorDoReembolso(double valorDaConsulta) {
        if (valorDaConsulta <= 0) {
            throw new IllegalArgumentException("O valor da consulta deve ser maior que zero.");
        }
        return (valorDaConsulta * porcentualDeCobertura) / 100;
    }

    public void calcular(IPaciente paciente) {
        // Implementação futura
    }
}
