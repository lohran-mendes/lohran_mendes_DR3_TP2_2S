import edu.infnet.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class CalculadoraReembolsoTest {

    // Helper que configura o percentual de cobertura e um mock para o autorizador de reembolso.
    // O mock simula o comportamento do IAutorizadorReembolso, permitindo controlar o retorno do método autorizarReembolso.
    private void helperQueSetaPercentualDeCobertura(int percentual, CalculadoraReembolso calculadoraReembolso) {
        calculadoraReembolso.setPorcentualDeCobertura(percentual);
        IAutorizadorReembolso mockAutorizador = Mockito.mock(IAutorizadorReembolso.class);

        Mockito.when(mockAutorizador.autorizarReembolso()).thenReturn(true);
        calculadoraReembolso.setAutorizadorReembolso(mockAutorizador);
    }

    // Helper para comparar valores double com margem de erro.
    private static void assertEqualsComMargem(double esperado, double atual) {
        Assertions.assertTrue(Math.abs(esperado - atual) < 0.01,
                "Esperado: " + esperado + ", Atual: " + atual);
    }

    // Teste que verifica o cálculo correto do valor do reembolso
    // Utiliza mock para garantir que o reembolso será autorizado
    @Test
    public void deveSerPossivelCalcularOValorDoReembolso() throws Exception {
        CalculadoraReembolso calculadoraReembolso = new CalculadoraReembolso();
        helperQueSetaPercentualDeCobertura(70, calculadoraReembolso);

        double valorDoReembolso = calculadoraReembolso.calcularValorDoReembolso(200);

        assertEqualsComMargem(140, valorDoReembolso);
        Assertions.assertEquals(140, valorDoReembolso);
    }

    // Teste que garante que uma exceção é lançada ao tentar calcular reembolso com valor igual a zero.
    // Utiliza mock para simular autorização.
    @Test
    public void deveLancarErroEmConsultaComValorIgualAZero() {
        CalculadoraReembolso calculadoraReembolso = new CalculadoraReembolso();
        helperQueSetaPercentualDeCobertura(50, calculadoraReembolso);

        Assertions.assertThrows(Exception.class, () -> calculadoraReembolso.calcularValorDoReembolso(0));
    }

    // Teste que garante que uma exceção é lançada ao tentar calcular reembolso com valor negativo.
    @Test
    public void deveLancarErroEmConsultasComValorMenorQueZero() {
        CalculadoraReembolso calculadoraReembolso = new CalculadoraReembolso();
        helperQueSetaPercentualDeCobertura(50, calculadoraReembolso);

        Assertions.assertThrows(IllegalArgumentException.class, () -> calculadoraReembolso.calcularValorDoReembolso(-100));
    }

    // Teste que verifica se o método calcular pode receber um objeto do tipo IPaciente como argumento.
    // Não são feitas verificações adicionais, pois o objetivo é apenas testar a assinatura do método.
    @Test
    public void deveReceberUmObjetoPacienteComoArgumento() {
        CalculadoraReembolso calculadoraReembolso = new CalculadoraReembolso();
        helperQueSetaPercentualDeCobertura(50, calculadoraReembolso);

        IPaciente dummyPaciente = null;

        Assertions.assertDoesNotThrow(() -> calculadoraReembolso.calcular(dummyPaciente));
    }

    // Teste que verifica se as consultas estão sendo corretamente adicionadas ao histórico de consultas.
    // Utiliza uma implementação fake de IHistoricoConsultas para verificar o comportamento do método adicionarConsulta.
    @Test
    public void deveAdicionarConsultasAoHistoricoDeConsultas() {
        FakeHistoricoConsultas fakeHistoricoConsultas = new FakeHistoricoConsultas();
        IConsulta fakeConsulta = null;

        fakeHistoricoConsultas.adicionarConsulta(fakeConsulta);
        fakeHistoricoConsultas.adicionarConsulta(fakeConsulta);

        assertEqualsComMargem(2, fakeHistoricoConsultas.listaDeConsultas.size());
        Assertions.assertEquals(2, fakeHistoricoConsultas.listaDeConsultas.size());
    }

    // Teste que verifica se o percentual de cobertura está sendo corretamente retornado pelo StubPlanoDeSaude.
    // Utiliza a classe StubPlanoDeSaude para testar o comportamento do método getPorcentualDeCobertura.
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

    // Teste que verifica se o método registrarConsulta da auditoria é chamado ao registrar uma consulta.
    // Utiliza um espião (spy) para verificar a interação com o objeto de auditoria.
    @Test
    public void DeveVerificarSeOMetodoRegistrarConsultaFoiChamado() {
        SpyAuditoria spyAuditoria = new SpyAuditoria();
        spyAuditoria.registrarConsulta();

        Assertions.assertTrue(spyAuditoria.metodoFoiChamado);
    }

    // Teste que garante que uma exceção é lançada se a consulta não for autorizada.
    // Utiliza um mock para simular o comportamento do IAutorizadorReembolso.
    @Test
    public void deveLancarErroSeAConsultaNaoForAutorizada() {
        CalculadoraReembolso calculadoraReembolso = new CalculadoraReembolso();
        calculadoraReembolso.setPorcentualDeCobertura(50);
        IAutorizadorReembolso mockAutorizador = Mockito.mock(IAutorizadorReembolso.class);

        Mockito.when(mockAutorizador.autorizarReembolso()).thenReturn(false);
        calculadoraReembolso.setAutorizadorReembolso(mockAutorizador);

        Assertions.assertThrows(IllegalArgumentException.class, () -> calculadoraReembolso.calcularValorDoReembolso(200));
    }

    // Teste que garante que uma exceção é lançada se o valor do reembolso ultrapassar 150 reais.
    // Utiliza o percentual de cobertura de 100% para simplificar o teste.
    @Test
    public void deveLancarErroSeOReembolsoForMaiorQue150Reais() {
        CalculadoraReembolso calculadoraReembolso = new CalculadoraReembolso();
        helperQueSetaPercentualDeCobertura(100, calculadoraReembolso);

        Assertions.assertThrows(Exception.class, () -> calculadoraReembolso.calcularValorDoReembolso(151));
    }

    // Teste que verifica que não é lançada exceção para valores de reembolso menores ou iguais a 150 reais.
    // Utiliza o percentual de cobertura de 100% para simplificar o teste.
    @Test
    public void naoDeveLancarErroSeOReembolsoForMenorOuIgualA150Reais() {
        CalculadoraReembolso calculadoraReembolso = new CalculadoraReembolso();
        helperQueSetaPercentualDeCobertura(100, calculadoraReembolso);

        Assertions.assertDoesNotThrow(() -> calculadoraReembolso.calcularValorDoReembolso(150));
        Assertions.assertDoesNotThrow(() -> calculadoraReembolso.calcularValorDoReembolso(140));
        Assertions.assertDoesNotThrow(() -> calculadoraReembolso.calcularValorDoReembolso(100));
    }


    // Teste que simula diversos cenários de cálculo de reembolso.
    // Utiliza um stub para o plano de saúde e verifica o comportamento da calculadora de reembolso em diferentes situações.
    @Test
    public void deveTestarDiversosCenariosSimultaneamente() throws Exception {
        CalculadoraReembolso calculadoraReembolso = new CalculadoraReembolso();
        StubPlanoDeSaude stubPlanoDeSaude = new StubPlanoDeSaude();
        stubPlanoDeSaude.setPorcentualDeCobertura(50);

        helperQueSetaPercentualDeCobertura(stubPlanoDeSaude.getPorcentualDeCobertura(), calculadoraReembolso);

        double resposta = calculadoraReembolso.calcularValorDoReembolso(120);
        assertEqualsComMargem(60, resposta);
        Assertions.assertEquals(60, resposta);

        Assertions.assertThrows(IllegalArgumentException.class, () -> calculadoraReembolso.calcularValorDoReembolso(-120));
        Assertions.assertThrows(IllegalArgumentException.class, () -> calculadoraReembolso.calcularValorDoReembolso(0));
        Assertions.assertThrows(Exception.class, () -> calculadoraReembolso.calcularValorDoReembolso(500));

    }
}
