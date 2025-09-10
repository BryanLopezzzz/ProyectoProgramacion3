package hospital.logica;

import hospital.datos.RecetaDatos;
import hospital.datos.conector.PacienteConector;
import hospital.datos.conector.RecetaConector;
import hospital.datos.entidades.RecetaEntidad;
import hospital.logica.mapper.PacienteMapper;
import hospital.logica.mapper.RecetaMapper;
import hospital.model.*;
import jakarta.xml.bind.JAXBException;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

public class RecetaLogica {
    private final RecetaDatos datos;
    private final PacienteLogica pacienteLogica = new PacienteLogica();
    private final MedicoLogica medicoLogica = new MedicoLogica();

    public RecetaLogica() { this.datos = new RecetaDatos("data/recetas.xml"); }

    public List<Receta> listar() {
        return datos.load().getRecetas().stream()
                .map(e -> RecetaMapper.fromXML(e, pacienteLogica, medicoLogica, false))
                .collect(Collectors.toList());
    }

    public Receta buscarPorId(String id) {
        return datos.load().getRecetas().stream()
                .filter(r -> r.getId().equalsIgnoreCase(id))
                .map(e -> RecetaMapper.fromXML(e, pacienteLogica, medicoLogica, false))
                .findFirst()
                .orElse(null);
    }
    // =========================
    // CRUD Básico
    // =========================
    public Receta crearReceta(Receta receta) throws Exception {
        validarReceta(receta);

        RecetaConector conector = datos.load();
        conector.getRecetas().add(RecetaMapper.toXML(receta));
        datos.save(conector);

        return receta;
    }

    // =========================
    // Detalles / Estado
    // =========================
    public void agregarDetalle(String recetaId, String medicamentoId, int cantidad, String indicaciones, int diasTratamiento) throws Exception {
        Receta receta = buscarPorId(recetaId);
        if (receta == null) throw new Exception("Receta no encontrada.");

        Medicamento medicamento = new MedicamentoLogica().buscarPorCodigo(medicamentoId);
        if (medicamento == null) throw new Exception("Medicamento no encontrado: " + medicamentoId);

        DetalleReceta detalle = RecetaMapper.crearDetalle(medicamento, cantidad, indicaciones, diasTratamiento);
        receta.agregarDetalle(detalle);
        actualizar(receta);
    }

    // =========================
    // Helpers internos
    // =========================

    private void validarReceta(Receta r) throws Exception {
        if (r == null) throw new Exception("La receta no puede ser nula.");
        if (r.getId() == null || r.getId().isBlank()) throw new Exception("El ID de la receta es obligatorio.");
        if (r.getPaciente() == null) throw new Exception("La receta debe tener un paciente asociado.");
        if (r.getMedico() == null) throw new Exception("La receta debe tener un médico asociado.");
        if (r.getFecha() == null) throw new Exception("La receta debe tener una fecha válida.");
        if (r.getFechaRetiro() == null) throw new Exception("La receta debe tener una fecha de retiro.");
        if (r.getFechaRetiro().isBefore(r.getFecha()))
            throw new Exception("La fecha de retiro no puede ser anterior a la fecha de confección.");
    }
    public void actualizarEstado(String recetaId, EstadoReceta nuevoEstado) throws Exception {
        Receta receta = buscarPorId(recetaId);
        if (receta == null) throw new Exception("Receta no encontrada.");

        // Regla: Para pasar a EN_PROCESO, debe estar en ventana de retiro [-3, +3] respecto a hoy
        if (nuevoEstado == EstadoReceta.EN_PROCESO) {
            if (receta.getFechaRetiro() == null)
                throw new Exception("La receta no tiene fecha de retiro definida.");

            LocalDate hoy = LocalDate.now();
            long diff = ChronoUnit.DAYS.between(receta.getFechaRetiro(), hoy); // positivo si hoy > retiro
            if (diff < -3 || diff > 3) {
                throw new Exception("No se puede poner en PROCESO: fuera de la ventana de retiro (±3 días).");
            }
        }

        EstadoReceta anterior = receta.getEstado();
        receta.cambiarEstado(nuevoEstado);

        // Si no cambió, la transición fue inválida (saltos no permitidos, etc.)
        if (receta.getEstado() == anterior) {
            throw new Exception("Transición de estado inválida: " + anterior + " → " + nuevoEstado);
        }

        actualizar(receta);
    }
    //Clase
    public Receta actualizar(Receta receta) throws Exception {
        validarReceta(receta);

        RecetaConector data = datos.load();
        for (int i = 0; i < data.getRecetas().size(); i++) {
            var actual = data.getRecetas().get(i);

            if (actual.getId().equalsIgnoreCase(receta.getId())) {
                // Reemplazar el registro en XML
                data.getRecetas().set(i, RecetaMapper.toXML(receta));

                datos.save(data);
                return receta;
            }
        }
        throw new Exception("No existe receta con id: " + receta.getId());
    }

    public boolean eliminar(String id) throws Exception {
        RecetaConector conector = datos.load();
        boolean eliminado = conector.getRecetas().removeIf(r -> r.getId().equalsIgnoreCase(id));
        if (eliminado) {
            datos.save(conector);
        }
        return eliminado;
    }

    public List<Receta> buscarPorPaciente(String pacienteId) {
        return datos.load().getRecetas().stream()
                .map(e -> RecetaMapper.fromXML(e, pacienteLogica, medicoLogica, false))
                .filter(r -> r.getPaciente().getId().equalsIgnoreCase(pacienteId))
                .collect(Collectors.toList());
    }

    public List<Receta> buscarPorMedico(String medicoId) {
        return datos.load().getRecetas().stream()
                .map(e -> RecetaMapper.fromXML(e, pacienteLogica, medicoLogica, false))
                .filter(r -> r.getMedico().getId().equalsIgnoreCase(medicoId))
                .collect(Collectors.toList());
    }

//    public Receta create(Receta nuevo) throws JAXBException {
//        RecetaConector data= datos.load();
//        RecetaEntidad receta = RecetaMapper.toXML(nuevo);
//        data.getRecetas().add(receta);
//        datos.save(data);
//        return RecetaMapper.fromXML(receta);
//    }
}
