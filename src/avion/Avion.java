package avion;

import java.io.Serializable;

public class Avion implements Serializable {

    private String modelo;
    private String fabricante;
    private int capacidad;
    private int autonomia;

    public Avion() {}

    public Avion(String modelo, String fabricante, int capacidad, int autonomia) {
        this.modelo = modelo;
        this.fabricante = fabricante;
        this.capacidad = capacidad;
        this.autonomia = autonomia;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getFabricante() {
        return fabricante;
    }

    public void setFabricante(String fabricante) {
        this.fabricante = fabricante;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }

    public int getAutonomia() {
        return autonomia;
    }

    public void setAutonomia(int autonomia) {
        this.autonomia = autonomia;
    }
}
