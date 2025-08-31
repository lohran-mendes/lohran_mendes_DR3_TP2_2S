import edu.infnet.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Executable;


public class CalculadoraReembolsoTest {

    private void helperQueSetaPercentualDeCobertura(int percentual, boolean respostaDoAutorizador, CalculadoraReembolso calculadoraReembolso) {
        calculadoraReembolso.setPorcentualDeCobertura(percentual);
        IAutorizadorReembolso mockAutorizador = Mockito.mock(IAutorizadorReembolso.class);

        Mockito.when(mockAutorizador.autorizarReembolso()).thenReturn(respostaDoAutorizador);
        calculadoraReembolso.setAutorizadorReembolso(mockAutorizador);
    }

    public static void assertEqualsComMargem(double esperado, double atual) {
        Assertions.assertTrue(Math.abs(esperado - atual) < 0.01,
                "Esperado: " + esperado + ", Atual: " + atual);
    }

    @Test
    public void deveSerPossivelCalcularOValorDoReembolso() throws Exception {

        // Arrange
        CalculadoraReembolso calculadoraReembolso = new CalculadoraReembolso();

        helperQueSetaPercentualDeCobertura(70, true, calculadoraReembolso);

        // Act
        double valorDoReembolso = calculadoraReembolso.calcularValorDoReembolso(200);

        // Assert
        assertEqualsComMargem(140, valorDoReembolso);
        Assertions.assertEquals(140, valorDoReembolso);
    }


    @Test
    public void deveLancarErroEmConsultaComValorIgualAZero() {
        // Arrange
        CalculadoraReembolso calculadoraReembolso = new CalculadoraReembolso();
        helperQueSetaPercentualDeCobertura(50, true, calculadoraReembolso);


        // Act & Assert
        Assertions.assertThrows(Exception.class, () -> {
            calculadoraReembolso.calcularValorDoReembolso(0);
        });
    }

    @Test
    public void deveLancarErroEmConsultasComValorMenorQueZero() {
        // Arrange
        CalculadoraReembolso calculadoraReembolso = new CalculadoraReembolso();
        helperQueSetaPercentualDeCobertura(50, true, calculadoraReembolso);


        // Act & Assert
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            calculadoraReembolso.calcularValorDoReembolso(-100);
        });
    }

    @Test
    public void deveReceberUmObjetoPacienteComoArgumento() {
        // Arrange
        CalculadoraReembolso calculadoraReembolso = new CalculadoraReembolso();
        helperQueSetaPercentualDeCobertura(50, true, calculadoraReembolso);

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

        assertEqualsComMargem(2, fakeHistoricoConsultas.listaDeConsultas.size());
        Assertions.assertEquals(2, fakeHistoricoConsultas.listaDeConsultas.size());
    }

    @Test
    public void deveRetornarOPercentualDeCoberturaCorreto() {
        StubPlanoDeSaude stubPlanoDeSaude = new StubPlanoDeSaude();

        stubPlanoDeSaude.setPorcentualDeCobertura(80);
        assertEqualsComMargem(80, stubPlanoDeSaude.getPorcentualDeCobertura());
        Assertions.assertEquals(80, stubPlanoDeSaude.getPorcentualDeCobertura());

        stubPlanoDeSaude.setPorcentualDeCobertura(50);
        assertEqualsComMargem(50, stubPlanoDeSaude.getPorcentualDeCobertura());
        Assertions.assertEquals(50, stubPlanoDeSaude.getPorcentualDeCobertura());
    }

    @Test
    public void DeveVerificarSeOMetodoRegistrarConsultaFoiChamado() {
        SpyAuditoria spyAuditoria = new SpyAuditoria();
        spyAuditoria.registrarConsulta();

        Assertions.assertTrue(spyAuditoria.metodoFoiChamado);
    }

    // As regras mudaram: só se pode reembolsar consultas autorizadas. Crie uma interface AutorizadorReembolso e use Mockito para criar um mock. O mock deve simular o comportamento do autorizador e retornar falso em um dos testes. O sistema, nesses casos, deve lançar uma exceção.
    @Test
    public void deveLancarErroSeAConsultaNaoForAutorizada() {
        CalculadoraReembolso calculadoraReembolso = new CalculadoraReembolso();
        calculadoraReembolso.setPorcentualDeCobertura(50);
        IAutorizadorReembolso mockAutorizador = Mockito.mock(IAutorizadorReembolso.class);

        Mockito.when(mockAutorizador.autorizarReembolso()).thenReturn(false);
        calculadoraReembolso.setAutorizadorReembolso(mockAutorizador);


        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            calculadoraReembolso.calcularValorDoReembolso(200);
        });

    }

    @Test
    public void deveLancarErroSeOReembolsoForMaiorQue150Reais() {
        CalculadoraReembolso calculadoraReembolso = new CalculadoraReembolso();
        helperQueSetaPercentualDeCobertura(100, true, calculadoraReembolso);

        Assertions.assertThrows(Exception.class, () -> {
            calculadoraReembolso.calcularValorDoReembolso(151);
        });
    }

    @Test
    public void naoDeveLancarErroSeOReembolsoForMenorOuIgualA150Reais() {
        CalculadoraReembolso calculadoraReembolso = new CalculadoraReembolso();
        helperQueSetaPercentualDeCobertura(100, true, calculadoraReembolso);


        Assertions.assertDoesNotThrow(()->calculadoraReembolso.calcularValorDoReembolso(150));
        Assertions.assertDoesNotThrow(()->calculadoraReembolso.calcularValorDoReembolso(140));
        Assertions.assertDoesNotThrow(()->calculadoraReembolso.calcularValorDoReembolso(100));
    }

//  Agora combine todos os elementos em um teste mais completo. Use um stub para PlanoSaude, um mock para AutorizadorReembolso e o helper de criação de consultas. Simule um cenário completo e valide o funcionamento conjunto dos componentes.

    @Test
    public void deveTestarDiversosCenariosSimultaneamente() throws Exception {
        CalculadoraReembolso calculadoraReembolso = new CalculadoraReembolso();
        StubPlanoDeSaude stubPlanoDeSaude = new StubPlanoDeSaude();
        stubPlanoDeSaude.setPorcentualDeCobertura(50);

        helperQueSetaPercentualDeCobertura(stubPlanoDeSaude.getPorcentualDeCobertura(), true, calculadoraReembolso);

        double resposta = calculadoraReembolso.calcularValorDoReembolso(120);
        assertEqualsComMargem(60, resposta);
        Assertions.assertEquals(60, resposta);

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            calculadoraReembolso.calcularValorDoReembolso(-120);
        });
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            calculadoraReembolso.calcularValorDoReembolso(0);
        });
        Assertions.assertThrows(Exception.class, () -> {;
            calculadoraReembolso.calcularValorDoReembolso(500);
        });

    }
}
