package hospital.datos;

import hospital.model.entidades.Paciente;

public class PacienteDAO extends GenericDAO<Paciente> {

    public PacienteDAO() {
        super(Paciente.class, "data/pacientes.xml");
    }

    @Override
    protected String getId(Paciente p) {
        return p.getId();
    }

    @Override
    protected String getNombre(Paciente p) {
        return p.getNombre();
    }
}