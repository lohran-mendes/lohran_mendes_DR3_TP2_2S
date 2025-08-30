import edu.infnet.IPlanoSaude;

public class StubPlanoDeSaude implements IPlanoSaude {
    int PorcentualDeCobertura = 0;

    @Override
    public int getPorcentualDeCobertura() {
        return this.PorcentualDeCobertura;
    }

    @Override
    public void setPorcentualDeCobertura(int novoPercentualDeCobertura) {
        this.PorcentualDeCobertura = novoPercentualDeCobertura;
    }
}