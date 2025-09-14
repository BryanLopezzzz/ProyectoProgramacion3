package hospital.controller;

import hospital.model.EstadoReceta;
import hospital.model.Farmaceuta;
import hospital.model.Medico;
import hospital.model.Receta;

import java.util.List;

import hospital.model.EstadoReceta;
import hospital.model.Farmaceuta;
import hospital.model.Medico;
import hospital.model.Receta;
import hospital.logica.RecetaLogica;

import java.util.List;

public class RecetaController {
    private final RecetaLogica recetaLogica;

    public RecetaController() {
        this.recetaLogica = new RecetaLogica();
    }

    // MÉDICOS
    public Receta crearReceta(Medico medico, Receta receta) throws Exception {
        validarMedico(medico);
        return recetaLogica.crearReceta(receta);
    }

    public void agregarDetalle(Medico medico, String recetaId, String medicamentoId, int cantidad, String indicaciones,int diasTratamiento) throws Exception {
        validarMedico(medico);
        recetaLogica.agregarDetalle(recetaId, medicamentoId, cantidad, indicaciones,diasTratamiento);
    }

    // FARMACEUTAS
    public void actualizarEstado(Farmaceuta farmaceuta, String recetaId, EstadoReceta nuevoEstado) throws Exception {
        validarFarmaceuta(farmaceuta);
        recetaLogica.actualizarEstado(recetaId, nuevoEstado);
    }

    // CONSULTAS
    public Receta buscarReceta(String id) throws Exception {
        return recetaLogica.buscarPorId(id);
    }

    public List<Receta> buscarPorPacientw(String id) throws Exception {
        return recetaLogica.buscarPorPaciente(id);
    }

    public List<Receta> listarRecetas() {
        return recetaLogica.listar();
    }

    private void validarMedico(Medico medico) throws Exception {
        if (medico == null) throw new Exception("Solo los médicos pueden ejecutar esta acción.");
    }

    private void validarFarmaceuta(Farmaceuta farmaceuta) throws Exception {
        if (farmaceuta == null) throw new Exception("Solo los farmaceutas pueden ejecutar esta acción.");
    }
}