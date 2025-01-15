package com.Alura.RepositorioLibros.model;



import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "libros")
public class Libro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String titulo;
    private Integer numeroDeDescargas;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "libro_idiomas", joinColumns = @JoinColumn(name = "libro_id"))
    private List<String> idiomas;

    @ManyToMany (fetch = FetchType.EAGER)
    @JoinTable(
            name = "libros_autores", // Nombre de la tabla intermedia
            joinColumns = @JoinColumn(name = "libro_id"), // Columna que referencia a Libro
            inverseJoinColumns = @JoinColumn(name = "autor_id") // Columna que referencia a Autor
    )
    private List<Autores> autores;

    public Libro() {

    }

    public Libro(DatosLibro datosLibro) {
        this.titulo = datosLibro.titulo();
        this.numeroDeDescargas = datosLibro.numeroDeDescargas();
        this.idiomas = datosLibro.idiomas();
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public List<Autores> getAutores() {
        return autores;
    }

    public void setAutores(List<Autores> autores) {
        //autores.forEach(a -> a.setLibro(this));
        this.autores = autores;
    }

    public List<String> getIdiomas() {
        return idiomas;
    }

    public void setIdiomas(List<String> idiomas) {
        this.idiomas = idiomas;
    }

    public Integer getNumeroDeDescargas() {
        return numeroDeDescargas;
    }

    public void setNumeroDeDescargas(Integer numeroDeDescargas) {
        this.numeroDeDescargas = numeroDeDescargas;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return titulo;
    }
}
