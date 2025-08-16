package hospital.model.datos;

import hospital.model.entidades.Receta;

public class RecetaDAO extends GenericDAO<Receta> {

    public RecetaDAO() {
        super(Receta.class, "data/recetas.xml");
    }

    @Override
    protected String getId(Receta r) {
        return r.getId();
    }
}
