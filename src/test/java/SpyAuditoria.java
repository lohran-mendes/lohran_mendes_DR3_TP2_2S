import edu.infnet.IAuditoria;

public class SpyAuditoria implements IAuditoria {
    public boolean metodoFoiChamado = false;

    @Override
    public void registrarConsulta() {
        metodoFoiChamado = true;
    }
}