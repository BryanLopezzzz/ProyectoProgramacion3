package hospital.model;

public class DetalleReceta {
    private Medicamento medicamento;
    private int cantidad;
    private String indicaciones; // ej: "Tomar cada 8 horas"

    public DetalleReceta() {}

    public DetalleReceta(Medicamento medicamento, int cantidad, String indicaciones) {
        this.medicamento = medicamento;
        this.cantidad = cantidad;
        this.indicaciones = indicaciones;
    }

    public Medicamento getMedicamento() {
        return medicamento;
    }

    public void setMedicamento(Medicamento medicamento) {
        this.medicamento = medicamento;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public String getIndicaciones() {
        return indicaciones;
    }

    public void setIndicaciones(String indicaciones) {
        this.indicaciones = indicaciones;
    }

    @Override
    public String toString() {
        return medicamento.getNombre() + " x" + cantidad + " (" + indicaciones + ")";
    }
}
