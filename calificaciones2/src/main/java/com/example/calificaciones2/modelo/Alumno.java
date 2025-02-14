package com.example.calificaciones2.modelo;

public class Alumno {
    private int id;
    private String nombre;
    private double nota;
    private String calificacion;

    // Constructor vacío necesario para frameworks como Spring
    public Alumno() {}

    // Constructor con parámetros
    public Alumno(int id, String nombre, double nota) {
        this.id = id;
        this.nombre = nombre;
        this.nota = nota;
        this.calificacion = calcularCalificacion();
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getNota() {
        return nota;
    }

    public void setNota(double nota) {
        this.nota = nota;
        this.calificacion = calcularCalificacion(); // Actualiza la calificación al cambiar la nota
    }

    public String getCalificacion() {
        return calificacion;
    }
    public void setCalificacion(String calificacion) {
        this.calificacion = calificacion;
    }

    // Metodo privado para calcular la calificación según la nota
    private String calcularCalificacion() {
        if (nota >= 9) return "A";
        if (nota >= 7) return "B";
        if (nota >= 5) return "C";
        return "F";
    }
}
