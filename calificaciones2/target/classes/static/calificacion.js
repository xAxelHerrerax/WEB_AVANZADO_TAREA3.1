let graficoNotas = null; // Variable global para el gráfico
let alumnosGlobal = []; // Variable global para los alumnos

// Obtener la lista de alumnos y llenar la tabla
function obtenerAlumnos() {
    fetch("/api/alumnos")
        .then(response => response.json())
        .then(data => {
            alumnosGlobal = data; // Guardar los alumnos en la variable global
            llenarTabla(data); // Llenar la tabla con los datos
            cargarGrafico(); // Actualizar el gráfico
        })
        .catch(error => console.error("Error al obtener alumnos:", error));
}

// Llenar la tabla con los datos
function llenarTabla(data) {
    const tbody = document.querySelector("#listaAlumnos");
    tbody.innerHTML = data.map(alumno => `
        <tr>
            <td>${alumno.id}</td>
            <td>${alumno.nombre}</td>
            <td>${alumno.nota}</td>
            <td>${alumno.calificacion}</td>
        </tr>
    `).join("");
}

// Agregar un nuevo alumno
function agregarAlumno() {
    const nombre = document.getElementById("nombre").value;
    const nota = parseFloat(document.getElementById("nota").value);

    if (!validarNota(nota)) {
        alert("La nota debe estar entre 0 y 10.");
        return;
    }

    fetch("/api/alumnos", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ nombre, nota })
    })
        .then(() => {
            alert("Alumno agregado");
            obtenerAlumnos(); // Actualizar la tabla y el gráfico
        })
        .catch(error => console.error("Error al agregar alumno:", error));
}

// Actualizar un alumno (nombre y nota)
function actualizarAlumno() {
    const id = document.getElementById("idActualizar").value;
    const nombre = document.getElementById("nombreActualizar").value;
    const nota = parseFloat(document.getElementById("nuevaNota").value);

    if (!validarNota(nota)) {
        alert("La nota debe estar entre 0 y 10.");
        return;
    }

    const alumnoActualizado = {
        id: parseInt(id),
        nombre: nombre,
        nota: nota,
        calificacion: calcularCalificacion(nota)
    };

    fetch(`/api/alumnos/${id}`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(alumnoActualizado)
    })
        .then(response => {
            if (response.ok) {
                alert("Alumno actualizado");
                obtenerAlumnos();
            } else {
                alert("Error al actualizar el alumno");
                console.error("Respuesta del servidor:", response.status, response.statusText);
            }
        })
        .catch(error => console.error("Error al actualizar alumno:", error));
}

function calcularCalificacion(nota) {
    if (nota >= 9) return "A";
    if (nota >= 7) return "B";
    if (nota >= 5) return "C";
    return "F";
}

// Actualizar solo la nota de un alumno
function actualizarNota() {
    const id = document.getElementById("idActualizarNota").value;
    const nuevaNota = parseFloat(document.getElementById("nuevaNota").value);

    if (!validarNota(nuevaNota)) {
        alert("La nota debe estar entre 0 y 10.");
        return;
    }

    fetch(`/api/alumnos/${id}/nota?nota=${nuevaNota}`, {
        method: "PUT"
    })
        .then(response => {
            if (response.ok) {
                alert("Nota actualizada");
                obtenerAlumnos();
            } else {
                alert("Error al actualizar la nota");
                console.error("Respuesta del servidor:", response.status, response.statusText);
            }
        })
        .catch(error => console.error("Error al actualizar nota:", error));
}
// Eliminar un alumno
function eliminarAlumno() {
    const id = document.getElementById("idEliminar").value;

    fetch(`/api/alumnos/${id}`, { method: "DELETE" })
        .then(() => {
            alert("Alumno eliminado");
            obtenerAlumnos(); // Actualizar la tabla y el gráfico
        })
        .catch(error => console.error("Error al eliminar alumno:", error));
}

// Función para validar la nota
function validarNota(nota) {
    return nota >= 0 && nota <= 10;
}

// Filtrar alumnos por nota mínima y máxima
function filtrarAlumnos() {
    const minNota = parseFloat(document.getElementById("minNota").value) || 0;
    const maxNota = parseFloat(document.getElementById("maxNota").value) || 10;

    const alumnosFiltrados = alumnosGlobal.filter(alumno =>
        alumno.nota >= minNota && alumno.nota <= maxNota
    );

    llenarTabla(alumnosFiltrados);
}

// Ordenar alumnos por nota
function ordenarAlumnos(orden) {
    const alumnosOrdenados = [...alumnosGlobal]; // Copiar el arreglo global
    if (orden === "asc") {
        alumnosOrdenados.sort((a, b) => a.nota - b.nota);
    } else {
        alumnosOrdenados.sort((a, b) => b.nota - a.nota);
    }
    llenarTabla(alumnosOrdenados);
}

// Buscar alumnos por nombre
function buscarAlumnos() {
    const nombre = document.getElementById("buscarNombre").value.toLowerCase();
    const alumnosFiltrados = alumnosGlobal.filter(alumno =>
        alumno.nombre.toLowerCase().includes(nombre)
    );
    llenarTabla(alumnosFiltrados);
}

// Cargar el gráfico de notas
function cargarGrafico() {
    fetch("/api/alumnos")
        .then(response => response.json())
        .then(data => {
            const ctx = document.getElementById('graficoNotas').getContext('2d');

            // Destruir el gráfico existente si ya existe
            if (graficoNotas) {
                graficoNotas.destroy();
            }

            // Crear un nuevo gráfico
            graficoNotas = new Chart(ctx, {
                type: 'bar',
                data: {
                    labels: data.map(a => a.nombre),
                    datasets: [{
                        label: 'Notas',
                        data: data.map(a => a.nota),
                        backgroundColor: 'rgba(75, 192, 192, 0.2)',
                        borderColor: 'rgba(75, 192, 192, 1)',
                        borderWidth: 1
                    }]
                },
                options: {
                    scales: {
                        y: {
                            beginAtZero: true,
                            max: 10
                        }
                    }
                }
            });
        });
}

// Cargar la lista de alumnos y el gráfico al inicio
document.addEventListener("DOMContentLoaded", obtenerAlumnos);