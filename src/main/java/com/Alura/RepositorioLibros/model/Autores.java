package com.Alura.RepositorioLibros.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "autores")
public class Autores {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String nombre;
    private Integer fechaDeNacimiento;
    private Integer fechaDeFallecimiento;
    @ManyToMany(mappedBy = "autores", fetch = FetchType.EAGER) // Relación bidireccional
    private List<Libro> libro;

    public Autores() {

    }

    public Autores(DatosAutores datosAutores) {
        this.nombre = datosAutores.nombre();
        this.fechaDeNacimiento = datosAutores.fechaDeNacimiento();
        this.fechaDeFallecimiento = datosAutores.fechaDeFallecimiento();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getFechaDeNacimiento() {
        return fechaDeNacimiento;
    }

    public void setFechaDeNacimiento(Integer fechaDeNacimiento) {
        this.fechaDeNacimiento = fechaDeNacimiento;
    }

    public Integer getFechaDeFallecimiento() {
        return fechaDeFallecimiento;
    }

    public void setFechaDeFallecimiento(Integer fechaDeFallecimiento) {
        this.fechaDeFallecimiento = fechaDeFallecimiento;
    }

    public List<Libro> getLibro() {
        return libro;
    }

    public void setLibro(List<Libro> libro) {
        this.libro = libro;
    }

    public String getNombreFormateado() {
        if (nombre != null && nombre.contains(",")) {
            String[] partes = nombre.split(",", 2); // Divide el nombre en dos partes
            return partes[1].trim() + ", " + partes[0].trim(); // Intercambia las partes
        }
        return nombre; // Retorna el nombre sin cambios si no tiene coma
    }

    @Override
    public String toString() {
        return getNombreFormateado();

    }
}
