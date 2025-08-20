package hospital.model.service;

import hospital.model.datos.MedicamentoDAO;
import hospital.model.datos.PacienteDAO;
import hospital.model.datos.RecetaDAO;
import hospital.model.entidades.*;
import java.time.LocalDate;
import java.util.List;

public class RecetaService {

    private final RecetaDAO recetaDAO;
    private final PacienteDAO pacienteDAO;
    private final MedicamentoDAO medicamentoDAO;

    public RecetaService() {
        this.recetaDAO = new RecetaDAO();
        this.pacienteDAO = new PacienteDAO();
        this.medicamentoDAO = new MedicamentoDAO();
    }

    // ========================
    // PRESCRIPCIÓN (Médicos)
    // ========================
    public Receta crearReceta(Medico medico, String pacienteId, String recetaId) throws Exception {
        if (medico == null) {
            throw new Exception("Solo los médicos pueden prescribir recetas.");
        }

        Paciente paciente = pacienteDAO.buscarPorId(pacienteId);
        if (paciente == null) {
            throw new Exception("Paciente no encontrado.");
        }

        Receta receta = new Receta(recetaId, paciente, medico, LocalDate.now());
        receta.setEstado(EstadoReceta.CONFECCIONADA);
        recetaDAO.agregar(receta);
        return receta;
    }

    public void agregarMedicamento(String recetaId, String medicamentoId, int cantidad, String indicaciones) throws Exception {
        Receta receta = recetaDAO.buscarPorId(recetaId);
        if (receta == null) {
            throw new Exception("Receta no encontrada.");
        }

        Medicamento med = medicamentoDAO.buscarPorCodigo(medicamentoId);
        if (med == null) {
            throw new Exception("Medicamento no encontrado.");
        }

        DetalleReceta detalle = new DetalleReceta(med, cantidad, indicaciones);
        receta.agregarDetalle(detalle);
        recetaDAO.modificar(receta);
    }

    // ========================
    // DESPACHO (Farmaceutas)
    // ========================
    public void actualizarEstado(Farmaceuta farmaceuta, String recetaId, EstadoReceta nuevoEstado) throws Exception {
        if (farmaceuta == null) {
            throw new Exception("Solo los farmaceutas pueden despachar recetas.");
        }

        Receta receta = recetaDAO.buscarPorId(recetaId);
        if (receta == null) {
            throw new Exception("Receta no encontrada.");
        }

        EstadoReceta estadoAnterior = receta.getEstado();
        receta.cambiarEstado(nuevoEstado);

        if (receta.getEstado() == estadoAnterior) {
            throw new Exception("Transición de estado inválida: " + estadoAnterior + " → " + nuevoEstado);
        }

        recetaDAO.modificar(receta);
    }

    // ========================
    // Consultas generales
    // ========================
    public Receta buscarPorId(String id) throws Exception {
        if (id == null || id.isBlank()) {
            throw new Exception("El ID de la receta no puede estar vacío.");
        }
        return recetaDAO.buscarPorId(id);
    }

    public List<Receta> listarRecetas() {
        return recetaDAO.listar();
    }
}