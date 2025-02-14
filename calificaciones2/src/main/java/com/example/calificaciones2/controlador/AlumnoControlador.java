package com.example.calificaciones2.controlador;

import com.example.calificaciones2.modelo.Alumno;
import com.example.calificaciones2.servicio.AlumnoServicio;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/alumnos")
public class AlumnoControlador {
     @Autowired
private final AlumnoServicio alumnoServicio;

    public AlumnoControlador(AlumnoServicio alumnoServicio) {
        this.alumnoServicio = alumnoServicio;
    }

    @GetMapping
    public List<Alumno> obtenerTodos() {
        return alumnoServicio.obtenerTodos();
    }
    @GetMapping("/{id}")
    public Alumno obtenerPorId(@PathVariable int id){
        return alumnoServicio.obtenerPorId(id);
    }

    @PostMapping
    public void agregarAlumno(@RequestBody Alumno alumno){
        alumnoServicio.agregarAlumno(alumno);
    }

    // Actualizar nombre y nota de un alumno
    @PutMapping("/{id}")
    public void actualizarAlumno(@PathVariable int id, @RequestBody Alumno alumno) {
        alumno.setId(id); // Asegurarse de que el ID sea correcto
        alumnoServicio.actualizarAlumno(alumno);
    }

    // Actualizar solo la nota de un alumno
    @PutMapping("/{id}/nota")
    public void actualizarNota(@PathVariable int id, @RequestParam double nota) {
        alumnoServicio.actualizarNota(id, nota);
    }

    @DeleteMapping("/{id}")
    public void eliminarAlumno(@PathVariable int id){
        alumnoServicio.eliminarAlumno(id);
    }

    @GetMapping("/filtrar")
    public List<Alumno> filtrarPorNota(@RequestParam(required = false) Double min, @RequestParam(required = false) Double max) {
        return alumnoServicio.obtenerTodos().stream()
                .filter(a -> (min == null || a.getNota() >= min) && (max == null || a.getNota() <= max))
                .collect(Collectors.toList());
    }

    @GetMapping("/ordenar")
    public List<Alumno> ordenarPorNota(@RequestParam(required = false, defaultValue = "desc") String orden) {
        List<Alumno> alumnos = alumnoServicio.obtenerTodos();
        if ("asc".equalsIgnoreCase(orden)) {
            alumnos.sort(Comparator.comparingDouble(Alumno::getNota));
        } else {
            alumnos.sort(Comparator.comparingDouble(Alumno::getNota).reversed());
        }
        return alumnos;
    }
    @GetMapping("/buscar")
    public List<Alumno> buscarPorNombre(@RequestParam String nombre) {
        return alumnoServicio.obtenerTodos().stream()
                .filter(a -> a.getNombre().toLowerCase().contains(nombre.toLowerCase()))
                .collect(Collectors.toList());
    }

    @GetMapping("/exportar-csv")
    public void exportarCSV(HttpServletResponse response) throws IOException {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=alumnos.csv");
        PrintWriter writer = response.getWriter();
        writer.println("ID,Nombre,Nota,Calificación");
        for (Alumno alumno : alumnoServicio.obtenerTodos()) {
            writer.println(alumno.getId() + "," + alumno.getNombre() + "," + alumno.getNota() + "," + alumno.getCalificacion());
        }
    }


}
