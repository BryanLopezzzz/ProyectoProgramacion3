package hospital.model.datos;

import hospital.model.entidades.Medico;
import java.util.List;
import java.util.ArrayList;

public class MedicoDAO extends GenericDAO<Medico> {

    public MedicoDAO() {
        super(Medico.class, "data/medicos.xml");
    }

    @Override
    protected String getId(Medico m) {
        return m.getId();
    }

    @Override
    protected String getNombre(Medico m) {
        return m.getNombre();
    }


    @Override
    public void agregar(Medico m) {
        // Regla: clave inicial = id
        m.setClave(m.getId());
        super.agregar(m);
    }
    public List<Medico> buscarPorNombre(String nombre) {
        List<Medico> resultados = new ArrayList<>();
        for (Medico m : elementos) {
            if (m.getNombre().toLowerCase().contains(nombre.toLowerCase())) {
                resultados.add(m);
            }
        }
        return resultados;
    }
}
