import edu.infnet.CalculadoraReembolso;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


public class CalculadoraReembolsoTest {

    @Test
    public void deveSerPossivelCalcularOValorDoReembolso() {
        // Você começará implementando a função mais simples do sistema: calcular o valor a ser reembolsado com base em um valor fixo e percentual de cobertura. Escreva um teste para validar que uma consulta de R$ 200 com 70% de cobertura resulta em R$ 140 de reembolso.

        // Arrange
        CalculadoraReembolso calculadoraReembolso = new CalculadoraReembolso();
        calculadoraReembolso.setPorcentualDeCobertura(70);

        // Act
        double valorDoReembolso = calculadoraReembolso.calcularValorDoReembolso(200);

        // Assert
        Assertions.assertEquals(140, valorDoReembolso);
    }


    @Test
    public void deveLancarErroEmConsultaComValorIgualAZero() {
        // Arrange
        CalculadoraReembolso calculadoraReembolso = new CalculadoraReembolso();
        calculadoraReembolso.setPorcentualDeCobertura(50);

        // Act & Assert
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            calculadoraReembolso.calcularValorDoReembolso(0);
        });
    }

    @Test
    public void deveLancarErroEmConsultasComValorMenorQueZero() {
        // Arrange
        CalculadoraReembolso calculadoraReembolso = new CalculadoraReembolso();
        calculadoraReembolso.setPorcentualDeCobertura(50);

        // Act & Assert
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            calculadoraReembolso.calcularValorDoReembolso(-100);
        });
    }
}
