import edu.infnet.*;
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

    @Test
    public void deveReceberUmObjetoPacienteComoArgumento() {
        // Arrange
        CalculadoraReembolso calculadoraReembolso = new CalculadoraReembolso();
        calculadoraReembolso.setPorcentualDeCobertura(50);
        IPaciente dummyPaciente = null;

        // Act & Assert
        Assertions.assertDoesNotThrow(() -> calculadoraReembolso.calcular(dummyPaciente));
    }

    @Test
    public void deveAdicionarConsultasAoHistoricoDeConsultas() {
        FakeHistoricoConsultas fakeHistoricoConsultas = new FakeHistoricoConsultas();
        IConsulta fakeConsulta = null;

        fakeHistoricoConsultas.adicionarConsulta(fakeConsulta);
        fakeHistoricoConsultas.adicionarConsulta(fakeConsulta);

        Assertions.assertEquals(2, fakeHistoricoConsultas.listaDeConsultas.size());
    }

    @Test
    public void deveRetornarOPercentualDeCoberturaCorreto() {
        StubPlanoDeSaude stubPlanoDeSaude = new StubPlanoDeSaude();

        stubPlanoDeSaude.setPorcentualDeCobertura(80);
        Assertions.assertEquals(80, stubPlanoDeSaude.getPorcentualDeCobertura());

        stubPlanoDeSaude.setPorcentualDeCobertura(50);
        Assertions.assertEquals(50, stubPlanoDeSaude.getPorcentualDeCobertura());
    }

    @Test
    public void DeveVerificarSeOMetodoRegistrarConsultaFoiChamado() {
        SpyAuditoria spyAuditoria = new SpyAuditoria();
        spyAuditoria.registrarConsulta();

        Assertions.assertTrue(spyAuditoria.metodoFoiChamado);
    }
}
