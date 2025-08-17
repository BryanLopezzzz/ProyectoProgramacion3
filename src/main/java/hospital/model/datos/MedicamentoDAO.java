package hospital.model.datos;

import hospital.model.entidades.Medicamento;
import java.util.ArrayList;
import java.util.List;
//falta la restricción de que solo el administrador pueda usar esta funcionalidad. Eso se maneja en la capa de controlador/interfaz.
public class MedicamentoDAO extends GenericDAO<Medicamento> {

    public MedicamentoDAO() {
        super(Medicamento.class, "data/medicamentos.xml");
    }

    @Override
    protected String getId(Medicamento m) {
        return m.getCodigo();
    }

    @Override
    protected String getNombre(Medicamento m) {
        return m.getNombre();
    }

    public Medicamento buscarPorCodigo(String codigo) {
        return buscarPorId(codigo); // reutiliza el genérico
    }

}