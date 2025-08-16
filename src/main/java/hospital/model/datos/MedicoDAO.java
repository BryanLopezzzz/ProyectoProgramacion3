package hospital.model.datos;

import hospital.model.entidades.Medico;

public class MedicoDAO extends GenericDAO<Medico> {

    public MedicoDAO() {
        super(Medico.class, "data/medicos.xml");
    }

    @Override
    protected String getId(Medico m) {
        return m.getId();
    }

    @Override
    public void agregar(Medico m) {
        // Regla: clave inicial = id
        m.setClave(m.getId());
        super.agregar(m);
    }
}
