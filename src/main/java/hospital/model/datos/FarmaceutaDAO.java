package hospital.model.datos;

import hospital.model.entidades.Farmaceuta;

public class FarmaceutaDAO extends GenericDAO<Farmaceuta> {

    public FarmaceutaDAO() {
        super(Farmaceuta.class, "data/farmaceutas.xml");
    }

    @Override
    protected String getId(Farmaceuta f) {
        return f.getId();
    }

    @Override
    public void agregar(Farmaceuta f) {
        // Regla: clave inicial = id
        f.setClave(f.getId());
        super.agregar(f);
    }
}