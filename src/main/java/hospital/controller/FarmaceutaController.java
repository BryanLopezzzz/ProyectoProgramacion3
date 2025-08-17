package hospital.controller;

import hospital.model.datos.FarmaceutaDAO;
import hospital.model.entidades.Farmaceuta;
import hospital.model.entidades.Administrador;

public class FarmaceutaController extends ControllerGeneric<Farmaceuta> {
    public FarmaceutaController() {
        super(new FarmaceutaDAO());
    }
}