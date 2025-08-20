package hospital.model.service;

import hospital.model.datos.MedicoDAO;
import hospital.model.entidades.Medico;

import java.util.List;

public class MedicoService extends GenericServiceImpl<Medico> {

    private final MedicoDAO medicoDAO;

    public MedicoService() {
        super(new MedicoDAO());
        this.medicoDAO = (MedicoDAO) super.dao;
    }

    @Override
    public void agregar(Medico medico) throws Exception {
        validarMedico(medico);
        super.agregar(medico); // usa el método genérico
    }

    @Override
    public void modificar(Medico medico) throws Exception {
        validarMedico(medico);
        super.modificar(medico);
    }

    public List<Medico> buscarPorNombre(String nombre) {
        return medicoDAO.buscarPorNombre(nombre);
    }

    private void validarMedico(Medico m) throws Exception {
        if (m == null) throw new Exception("El médico no puede ser nulo.");
        if (m.getId() == null || m.getId().isBlank()) throw new Exception("El ID es obligatorio.");
        if (m.getNombre() == null || m.getNombre().isBlank()) throw new Exception("El nombre es obligatorio.");
        if (m.getEspecialidad() == null || m.getEspecialidad().isBlank()) throw new Exception("La especialidad es obligatoria.");
    }
}
