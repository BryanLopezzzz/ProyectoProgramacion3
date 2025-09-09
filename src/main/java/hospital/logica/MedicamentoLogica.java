package hospital.logica;

import hospital.datos.MedicamentoDatos;
import hospital.datos.conector.MedicamentoConector;
import hospital.datos.entidades.MedicamentoEntidad;
import hospital.logica.mapper.MedicamentoMapper;
import hospital.model.Medicamento;

import java.util.List;
import java.util.stream.Collectors;

public class MedicamentoLogica {
    private final MedicamentoDatos datos;

    public MedicamentoLogica() {
        this.datos = new MedicamentoDatos("data/medicamentos.xml");
    }

    public void agregar(Medicamento medicamento) throws Exception {
        validarMedicamento(medicamento);

        MedicamentoConector conector = datos.load();
        conector.getMedicamentos().add(MedicamentoMapper.toXML(medicamento));
        datos.save(conector);
    }

    public List<Medicamento> listar() {
        return datos.load().getMedicamentos().stream()
                .map(MedicamentoMapper::fromXML)   // Convertir cada MedicamentoEntidad a Medicamento
                .collect(Collectors.toList());
    }

    public Medicamento buscarPorCodigo(String codigo) {
        return datos.load().getMedicamentos().stream()
                .filter(m -> m.getCodigo().equalsIgnoreCase(codigo))
                .map(MedicamentoMapper::fromXML)
                .findFirst()
                .orElse(null);
    }

    public List<Medicamento> buscarPorNombre(String nombre) {
        return datos.load().getMedicamentos().stream()
                .map(MedicamentoMapper::fromXML)   // Convertir cada MedicamentoEntidad a Medicamento
                .filter(m -> m.getNombre().toLowerCase().contains(nombre.toLowerCase()))
                .collect(Collectors.toList());
    }

    private void validarMedicamento(Medicamento m) throws Exception {
        if (m == null) throw new Exception("El medicamento no puede ser nulo.");
        if (m.getCodigo() == null || m.getCodigo().isBlank()) throw new Exception("El código es obligatorio.");
        if (m.getNombre() == null || m.getNombre().isBlank()) throw new Exception("El nombre es obligatorio.");
        if (m.getPresentacion() == null || m.getPresentacion().isBlank()) throw new Exception("La presentación es obligatoria.");
    }

    public void modificar(Medicamento medicamento) throws Exception {
        validarMedicamento(medicamento);

        MedicamentoConector conector = datos.load();

        // Buscar el medicamento existente
        MedicamentoEntidad actual = conector.getMedicamentos().stream()
                .filter(m -> m.getCodigo().equalsIgnoreCase(medicamento.getCodigo()))
                .findFirst()
                .orElseThrow(() -> new Exception("No existe medicamento con código: " + medicamento.getCodigo()));

        // Actualizar los campos
        actual.setNombre(medicamento.getNombre());
        actual.setPresentacion(medicamento.getPresentacion());

        datos.save(conector);
    }

    public void borrar(String codigo) throws Exception {
        if (codigo == null || codigo.isBlank()) {
            throw new Exception("El código es obligatorio.");
        }

        MedicamentoConector conector = datos.load();
        boolean removed = conector.getMedicamentos().removeIf(m -> m.getCodigo().equalsIgnoreCase(codigo));

        if (!removed) {
            throw new Exception("No existe medicamento con código: " + codigo);
        }

        datos.save(conector);
    }

    public void generarReporte(String rutaReporte) throws Exception {
        if (rutaReporte == null || rutaReporte.isBlank()) {
            throw new Exception("La ruta del reporte es obligatoria.");
        }

        List<Medicamento> medicamentos = listar();

        // Crear conector para el reporte
        MedicamentoConector reporteConector = new MedicamentoConector();

        // Convertir medicamentos a entidades XML
        for (Medicamento med : medicamentos) {
            reporteConector.getMedicamentos().add(MedicamentoMapper.toXML(med));
        }

        // Crear datos para guardar el reporte
        MedicamentoDatos datosReporte = new MedicamentoDatos(rutaReporte);
        datosReporte.save(reporteConector);
    }

}
