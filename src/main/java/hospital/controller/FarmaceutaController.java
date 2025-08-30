package hospital.controller;

import hospital.model.entidades.Farmaceuta;
import hospital.logica.service.FarmaceutaService;

public class FarmaceutaController extends ControllerGeneric<Farmaceuta> {

    private final FarmaceutaService farmaceutaService;

    public FarmaceutaController() {
        super(new FarmaceutaService());
        this.farmaceutaService = (FarmaceutaService) super.service;
    }
}
