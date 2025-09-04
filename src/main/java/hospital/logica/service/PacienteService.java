package hospital.logica.service;

import hospital.logicaDatos.PacienteDAO;
import hospital.model.entidades.Paciente;

import java.time.LocalDate;
import java.util.List;

public class PacienteService extends GenericServiceImpl<Paciente> {

    private final PacienteDAO pacienteDAO;

    public PacienteService() {
        super(new PacienteDAO());
        this.pacienteDAO = (PacienteDAO) super.dao;
    }

    @Override
    public void agregar(Paciente paciente) throws Exception {
        validarPaciente(paciente);
        super.agregar(paciente); // usa el método genérico
    }

    @Override
    public void modificar(Paciente paciente) throws Exception {
        validarPaciente(paciente);
        super.modificar(paciente);
    }

    public List<Paciente> buscarPorNombre(String nombre) {
        return pacienteDAO.buscarPorNombre(nombre);
    }

    public Paciente buscarPorId(String id){ return pacienteDAO.buscarPorId(id); }

    private void validarPaciente(Paciente p) throws Exception {
        if (p == null) throw new Exception("El paciente no puede ser nulo.");
        if (p.getId() == null || p.getId().isBlank()) throw new Exception("El ID es obligatorio.");
        if (p.getNombre() == null || p.getNombre().isBlank()) throw new Exception("El nombre es obligatorio.");
        if (p.getFechaNacimiento() == null || p.getFechaNacimiento().isAfter(LocalDate.now())) {
            throw new Exception("La fecha de nacimiento no es válida.");
        }
        if (p.getTelefono() == null || p.getTelefono().isBlank()) {
            throw new Exception("El teléfono es obligatorio.");
        }
    }
}
