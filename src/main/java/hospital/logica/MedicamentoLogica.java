package hospital.logica;

import hospital.datos.MedicamentoDatos;
import hospital.datos.conector.MedicamentoConector;
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
}
