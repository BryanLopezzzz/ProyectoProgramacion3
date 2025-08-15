package hospital.model.entidades;

public class Medico {
    //Estas caracteristicas estan en el enunciado
    private String id;
    private String clave;
    private String nombre;
    private String especialidad;

    public Medico() {
    }

    public Medico(String id, String clave, String nombre, String especialidad) {
        this.id = id;
        this.clave = clave;
        this.nombre = nombre;
        this.especialidad = especialidad;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }
    @Override
    public String toString() {
        return nombre + " (" + especialidad + ")";
    }
}
