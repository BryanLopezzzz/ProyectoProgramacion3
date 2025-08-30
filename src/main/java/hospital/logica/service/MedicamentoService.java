package hospital.logica.service;

import hospital.datos.MedicamentoDAO;
import hospital.model.entidades.Medicamento;

import java.util.List;

public class MedicamentoService extends GenericServiceImpl<Medicamento> {

    private final MedicamentoDAO medicamentoDAO;

    public MedicamentoService() {
        super(new MedicamentoDAO());
        this.medicamentoDAO = (MedicamentoDAO) super.dao;
    }

    @Override
    public void agregar(Medicamento m) throws Exception {
        validarMedicamento(m);
        super.agregar(m);
    }

    @Override
    public void modificar(Medicamento m) throws Exception {
        validarMedicamento(m);
        super.modificar(m);
    }

    public Medicamento buscarPorCodigo(String codigo) throws Exception {
        if (codigo == null || codigo.isBlank()) {
            throw new Exception("El código no puede estar vacío.");
        }
        return medicamentoDAO.buscarPorCodigo(codigo);
    }

    public List<Medicamento> buscarPorNombre(String nombre) {
        return medicamentoDAO.buscarPorNombre(nombre);
    }

    private void validarMedicamento(Medicamento m) throws Exception {
        if (m == null) throw new Exception("El medicamento no puede ser nulo.");
        if (m.getCodigo() == null || m.getCodigo().isBlank()) throw new Exception("El código es obligatorio.");
        if (m.getNombre() == null || m.getNombre().isBlank()) throw new Exception("El nombre es obligatorio.");
        if (m.getPresentacion() == null || m.getPresentacion().isBlank()) throw new Exception("La presentación es obligatoria.");
    }
}
