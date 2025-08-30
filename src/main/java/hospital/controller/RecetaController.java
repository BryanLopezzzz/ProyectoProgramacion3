package hospital.controller;

import hospital.model.entidades.*;
import hospital.logica.service.RecetaService;
import java.util.List;

public class RecetaController {

    private final RecetaService recetaService;

    public RecetaController() {
        this.recetaService = new RecetaService();
    }

    // ========================
    // PRESCRIPCIÓN (Médicos)
    // ========================
    public Receta crearReceta(Medico medico, String pacienteId, String recetaId) throws Exception {
        if (medico == null) {
            throw new Exception("Solo los médicos pueden prescribir recetas.");
        }
        return recetaService.crearReceta(medico, pacienteId, recetaId);
    }

    public void agregarMedicamento(Medico medico, String recetaId, String medicamentoId, int cantidad, String indicaciones) throws Exception {
        if (medico == null) {
            throw new Exception("Solo los médicos pueden agregar medicamentos a la receta.");
        }
        recetaService.agregarMedicamento(recetaId, medicamentoId, cantidad, indicaciones);
    }

    // ========================
    // DESPACHO (Farmaceutas)
    // ========================
    public void actualizarEstado(Farmaceuta farmaceuta, String recetaId, EstadoReceta nuevoEstado) throws Exception {
        if (farmaceuta == null) {
            throw new Exception("Solo los farmaceutas pueden despachar recetas.");
        }
        recetaService.actualizarEstado(farmaceuta, recetaId, nuevoEstado);
    }

    // ========================
    // Consultas generales
    // ========================
    public Receta buscarReceta(String id) throws Exception {
        return recetaService.buscarPorId(id);
    }

    public List<Receta> listarRecetas() {
        return recetaService.listarRecetas();
    }
}