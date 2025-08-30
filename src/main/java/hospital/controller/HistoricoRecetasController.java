package hospital.controller;

import hospital.model.entidades.*;
import hospital.logica.service.HistoricoRecetasService;
import java.util.List;

public class HistoricoRecetasController {
    private final HistoricoRecetasService historicoService;

    public HistoricoRecetasController() {
        this.historicoService = new HistoricoRecetasService();
    }

    public List<Receta> listarRecetas(Usuario usuario) throws Exception {
        return historicoService.listarRecetas(usuario);
    }

    public Receta buscarPorId(Usuario usuario, String recetaId) throws Exception {
        return historicoService.buscarPorId(usuario, recetaId);
    }

    public List<Receta> buscarPorPaciente(Usuario usuario, String criterio) throws Exception {
        return historicoService.buscarPorPaciente(usuario, criterio);
    }

    public List<Receta> buscarPorMedico(Usuario usuario, String criterio) throws Exception {
        return historicoService.buscarPorMedico(usuario, criterio);
    }

    public List<Receta> buscarPorEstado(Usuario usuario, EstadoReceta estado) throws Exception {
        return historicoService.buscarPorEstado(usuario, estado);
    }
}
