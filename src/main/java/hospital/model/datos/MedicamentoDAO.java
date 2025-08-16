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

    public Medicamento buscarPorCodigo(String codigo) {
        return buscarPorId(codigo); // reutiliza el genérico
    }

    public List<Medicamento> buscarPorNombre(String nombre) {
        List<Medicamento> resultados = new ArrayList<>();
        for (Medicamento m : elementos) {
            if (m.getNombre().toLowerCase().contains(nombre.toLowerCase())) {
                resultados.add(m);
            }
        }
        return resultados;
    }
}