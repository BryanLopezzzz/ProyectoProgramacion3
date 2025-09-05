package hospital.logica;

import hospital.datos.PacienteDatos;
import hospital.datos.conector.PacienteConector;
import hospital.logica.mapper.PacienteMapper;
import hospital.model.Paciente;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class PacienteLogica {
    private final PacienteDatos datos;

    public PacienteLogica() {
        this.datos = new PacienteDatos("data/pacientes.xml");
    }

    public void agregar(Paciente paciente) throws Exception {
        validarPaciente(paciente);

        PacienteConector conector = datos.load();
        conector.getPacientes().add(PacienteMapper.toXML(paciente));
        datos.save(conector);
    }

    public List<Paciente> listar() {
        return datos.load().getPacientes().stream()
                .map(PacienteMapper::fromXML)
                .collect(Collectors.toList());
    }

    public Paciente buscarPorId(String id) {
        return datos.load().getPacientes().stream()
                .filter(p -> p.getId().equalsIgnoreCase(id))
                .map(PacienteMapper::fromXML)
                .findFirst()
                .orElse(null);
    }

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