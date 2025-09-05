package hospital.datos;

import hospital.model.Farmaceuta;

public class FarmaceutaDatos extends GenericDatos<Farmaceuta> {

    public FarmaceutaDatos() {
        super(Farmaceuta.class, "data/farmaceutas.xml");
    }

    @Override
    protected String getId(Farmaceuta f) {
        return f.getId();
    }

    @Override
    protected String getNombre(Farmaceuta f) {
        return f.getNombre();
    }

    @Override
    public void agregar(Farmaceuta f) {
        // Regla: clave inicial = id
        f.setClave(f.getId());
        super.agregar(f);
    }
}