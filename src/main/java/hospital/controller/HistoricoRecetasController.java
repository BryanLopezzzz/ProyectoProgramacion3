package hospital.controller;

import hospital.model.EstadoReceta;
import hospital.model.Receta;
import hospital.model.Usuario;
import hospital.logica.RecetaLogica;
import java.util.List;
import java.util.stream.Collectors;

public class HistoricoRecetasController {
    private final RecetaLogica recetaLogica;

    public HistoricoRecetasController() {
        this.recetaLogica = new RecetaLogica();
    }

    public List<Receta> listarRecetas(Usuario usuario) {
        // en este caso cualquier usuario puede ver el histórico
        return recetaLogica.listar();
    }

    public Receta buscarPorId(Usuario usuario, String recetaId) {
        return recetaLogica.buscarPorId(recetaId);
    }

    public List<Receta> buscarPorPaciente(Usuario usuario, String criterio) {
        return recetaLogica.listar().stream()
                .filter(r -> r.getPaciente() != null
                        && (r.getPaciente().getId().equalsIgnoreCase(criterio)
                        || r.getPaciente().getNombre().toLowerCase().contains(criterio.toLowerCase())))
                .collect(Collectors.toList());
    }

    public List<Receta> buscarPorMedico(Usuario usuario, String criterio) {
        return recetaLogica.listar().stream()
                .filter(r -> r.getMedico() != null
                        && (r.getMedico().getId().equalsIgnoreCase(criterio)
                        || r.getMedico().getNombre().toLowerCase().contains(criterio.toLowerCase())))
                .collect(Collectors.toList());
    }

    public List<Receta> buscarPorEstado(Usuario usuario, EstadoReceta estado) {
        return recetaLogica.listar().stream()
                .filter(r -> r.getEstado() == estado)
                .collect(Collectors.toList());
    }
}
