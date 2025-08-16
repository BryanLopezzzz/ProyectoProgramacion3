package hospital.controller;

import hospital.model.datos.*;
import hospital.model.entidades.*;
import java.util.List;

public class AdministradorController {
    private final MedicoDAO medicoDAO = new MedicoDAO();
    private final FarmaceutaDAO farmaceutaDAO = new FarmaceutaDAO();
    private final PacienteDAO pacienteDAO = new PacienteDAO();
    private final MedicamentoDAO medicamentoDAO = new MedicamentoDAO();

    // ==== Médicos ====
    public void agregarMedico(Medico m) {
        medicoDAO.agregar(m);
    }
    public List<Medico> listarMedicos() {
        return medicoDAO.listar();
    }

    // ==== Farmaceutas ====
    public void agregarFarmaceuta(Farmaceuta f) {
        farmaceutaDAO.agregar(f);
    }
    public List<Farmaceuta> listarFarmaceutas() {
        return farmaceutaDAO.listar();
    }

    // ==== Pacientes ====
    public void agregarPaciente(Paciente p) {
        pacienteDAO.agregar(p);
    }
    public List<Paciente> listarPacientes() {
        return pacienteDAO.listar();
    }

    // ==== Medicamentos ====
    public void agregarMedicamento(Medicamento m) {
        medicamentoDAO.agregar(m);
    }
    public List<Medicamento> listarMedicamentos() {
        return medicamentoDAO.listar();
    }
}
