package hospital.controller;

import hospital.model.datos.MedicamentoDAO;
import hospital.model.entidades.Medicamento;
import hospital.model.entidades.Administrador;
import java.util.List;

import hospital.model.entidades.Administrador;
import hospital.model.entidades.Medicamento;
import hospital.model.service.MedicamentoService;

import java.util.List;

public class MedicamentoController extends ControllerGeneric<Medicamento> {

    private final MedicamentoService medicamentoService;

    public MedicamentoController() {
        super(new MedicamentoService());
        this.medicamentoService = (MedicamentoService) super.service;
    }

    public Medicamento buscarPorCodigo(Administrador admin, String codigo) throws Exception {
        validarAdmin(admin);
        return medicamentoService.buscarPorCodigo(codigo);
    }

    public List<Medicamento> buscarPorNombre(Administrador admin, String nombre) throws Exception {
        validarAdmin(admin);
        return medicamentoService.buscarPorNombre(nombre);
    }
}