import edu.infnet.IConsulta;
import edu.infnet.IHistoricoConsultas;

import java.util.ArrayList;
import java.util.List;

public class FakeHistoricoConsultas implements IHistoricoConsultas {
    public List<IConsulta> listaDeConsultas = new ArrayList<>();

    @Override
    public void adicionarConsulta(IConsulta consulta) {
        listaDeConsultas.add(consulta);
    }
}