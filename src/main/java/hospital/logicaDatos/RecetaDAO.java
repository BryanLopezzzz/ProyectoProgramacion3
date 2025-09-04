package hospital.logicaDatos;

import hospital.model.entidades.Receta;

public class RecetaDAO extends GenericDAO<Receta> {

    public RecetaDAO() {
        super(Receta.class, "data/recetas.xml");
    }

    @Override
    protected String getId(Receta r) {
        return r.getId();
    }

    @Override
    protected String getNombre(Receta r) {
        // Se supone que este metodo nunca se usa pero es solo para que funione GenericDAO
        if (r.getPaciente() != null) {
            return r.getPaciente().getNombre();
        }
        return "";
    }
}
