package hospital.controller;

import hospital.model.entidades.Farmaceuta;
import hospital.model.entidades.Administrador;
import hospital.model.service.FarmaceutaService;
import java.util.List;

public class FarmaceutaController extends ControllerGeneric<Farmaceuta> {

    private final FarmaceutaService farmaceutaService;

    public FarmaceutaController() {
        super(new FarmaceutaService());
        this.farmaceutaService = (FarmaceutaService) super.service;
    }
}
