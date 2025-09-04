package hospital.logica.service;

import hospital.logicaDatos.FarmaceutaDAO;
import hospital.model.entidades.Farmaceuta;
import java.util.List;

public class FarmaceutaService extends GenericServiceImpl<Farmaceuta> {

    private final FarmaceutaDAO farmaceutaDAO;

    public FarmaceutaService() {
        super(new FarmaceutaDAO());
        this.farmaceutaDAO = (FarmaceutaDAO) super.dao;
    }

    @Override
    public void agregar(Farmaceuta f) throws Exception {
        validarFarmaceuta(f);
        super.agregar(f);
    }

    @Override
    public void modificar(Farmaceuta f) throws Exception {
        validarFarmaceuta(f);
        super.modificar(f);
    }

    public List<Farmaceuta> buscarPorNombre(String nombre) {
        return farmaceutaDAO.buscarPorNombre(nombre);
    }

    private void validarFarmaceuta(Farmaceuta f) throws Exception {
        if (f == null) throw new Exception("El farmaceuta no puede ser nulo.");
        if (f.getId() == null || f.getId().isBlank()) throw new Exception("El ID es obligatorio.");
        if (f.getNombre() == null || f.getNombre().isBlank()) throw new Exception("El nombre es obligatorio.");
        if (f.getClave() == null || f.getClave().isBlank()) throw new Exception("La clave es obligatoria.");
    }
}
