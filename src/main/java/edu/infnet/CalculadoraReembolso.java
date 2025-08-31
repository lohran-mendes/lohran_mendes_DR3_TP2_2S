package edu.infnet;

public class CalculadoraReembolso {
    private int porcentualDeCobertura;
    private IAutorizadorReembolso autorizadorReembolso;

    public void setAutorizadorReembolso(IAutorizadorReembolso autorizadorReembolso) {
        this.autorizadorReembolso = autorizadorReembolso;
    }

    public void setPorcentualDeCobertura(int porcentualDeCobertura) {
        this.porcentualDeCobertura = porcentualDeCobertura;
    }

    public double calcularValorDoReembolso(double valorDaConsulta) throws Exception {
        if (valorDaConsulta <= 0 || !autorizadorReembolso.autorizarReembolso()) {
            throw new IllegalArgumentException("O valor da consulta deve ser maior que zero.");
        }
        double reembolso = (valorDaConsulta * porcentualDeCobertura) / 100;
        if (reembolso > 150) {
            throw new Exception("Não é possível reembolsar valores acima de R$150,00");
        }
        return reembolso;
    }

    public void calcular(IPaciente paciente) {
    }
}
