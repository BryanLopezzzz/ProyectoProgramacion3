package hospital.logica.service;

import hospital.datos.RecetaDAO;
import hospital.model.entidades.*;
import java.util.List;
import java.util.stream.Collectors;

public class HistoricoRecetasService {
    private final RecetaDAO recetaDAO;

    public HistoricoRecetasService() {
        this.recetaDAO = new RecetaDAO();
    }

    public List<Receta> listarRecetas(Usuario usuario) throws Exception {
        validarUsuario(usuario);
        return recetaDAO.listar();
    }

    public Receta buscarPorId(Usuario usuario, String recetaId) throws Exception {
        validarUsuario(usuario);
        return recetaDAO.buscarPorId(recetaId);
    }

    public List<Receta> buscarPorPaciente(Usuario usuario, String criterio) throws Exception {
        validarUsuario(usuario);
        return recetaDAO.listar().stream()
                .filter(r -> r.getPaciente() != null &&
                        (r.getPaciente().getId().equalsIgnoreCase(criterio) ||
                                r.getPaciente().getNombre().toLowerCase().contains(criterio.toLowerCase())))
                .collect(Collectors.toList());
    }

    public List<Receta> buscarPorMedico(Usuario usuario, String criterio) throws Exception {
        validarUsuario(usuario);
        return recetaDAO.listar().stream()
                .filter(r -> r.getMedico() != null &&
                        (r.getMedico().getId().equalsIgnoreCase(criterio) ||
                                r.getMedico().getNombre().toLowerCase().contains(criterio.toLowerCase())))
                .collect(Collectors.toList());
    }

    public List<Receta> buscarPorEstado(Usuario usuario, EstadoReceta estado) throws Exception {
        validarUsuario(usuario);
        return recetaDAO.listar().stream()
                .filter(r -> r.getEstado() == estado)
                .collect(Collectors.toList());
    }

    private void validarUsuario(Usuario usuario) throws Exception {
        if (usuario == null) {
            throw new Exception("Debe iniciar sesión para consultar recetas.");
        }
        if (!(usuario instanceof Medico ||
                usuario instanceof Farmaceuta ||
                usuario instanceof Administrador)) {
            throw new Exception("Usuario no autorizado para ver histórico de recetas.");
        }
    }
}
