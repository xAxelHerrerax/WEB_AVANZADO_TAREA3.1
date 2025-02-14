package com.example.calificaciones2.servicio;

import com.example.calificaciones2.modelo.Alumno;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class AlumnoServicio {
    private List<Alumno> alumnos;
    private final String ARCHIVO_JSON = "alumnos.json";
    private int idCounter = 1;

    public AlumnoServicio() {
        alumnos = cargarAlumnosDesdeJSON();
        if (alumnos == null) {
            alumnos = new ArrayList<>();
        }
        idCounter = alumnos.isEmpty() ? 1 : alumnos.get(alumnos.size() - 1).getId() + 1;
    }

    private List<Alumno> cargarAlumnosDesdeJSON() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            File file = new File(ARCHIVO_JSON);
            if (file.exists()) {
                return new ArrayList<>(Arrays.asList(objectMapper.readValue(file, Alumno[].class)));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    private void guardarAlumnosEnJSON() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper.writeValue(new File(ARCHIVO_JSON), alumnos);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Alumno> obtenerTodos() {
        return alumnos;
    }

    public Alumno obtenerPorId(int id) {
        return alumnos.stream()
                .filter(a -> a.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public void agregarAlumno(Alumno alumno) {
        alumno.setId(idCounter++);
        alumnos.add(alumno);
        guardarAlumnosEnJSON();
    }

    // Actualizar nombre y nota de un alumno
    public void actualizarAlumno(Alumno alumno) {
        Alumno alumnoExistente = obtenerPorId(alumno.getId());
        if (alumnoExistente != null) {
            alumnoExistente.setNombre(alumno.getNombre());
            alumnoExistente.setNota(alumno.getNota());
            alumnoExistente.setCalificacion(alumno.getCalificacion());
            guardarAlumnosEnJSON();
        }
    }

    // Actualizar solo la nota de un alumno
    public void actualizarNota(int id, double nuevaNota) {
        Alumno alumno = obtenerPorId(id);
        if (alumno != null) {
            alumno.setNota(nuevaNota);
            alumno.setCalificacion(calcularCalificacion(nuevaNota));
            guardarAlumnosEnJSON();
        }
    }

    // Calcular la calificación
    private String calcularCalificacion(double nota) {
        if (nota >= 9) return "A";
        if (nota >= 7) return "B";
        if (nota >= 5) return "C";
        return "F";
    }

    public void eliminarAlumno(int id) {
        alumnos.removeIf(a -> a.getId() == id);
        guardarAlumnosEnJSON();
    }
}