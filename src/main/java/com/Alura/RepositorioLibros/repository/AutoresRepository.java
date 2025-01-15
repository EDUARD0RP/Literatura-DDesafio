package com.Alura.RepositorioLibros.repository;

import com.Alura.RepositorioLibros.model.Autores;
import com.Alura.RepositorioLibros.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AutoresRepository extends JpaRepository<Autores,Long> {

    Autores findByNombre(String nombre);

    @Query("SELECT a FROM Autores a LEFT JOIN FETCH a.libro")
    List<Autores> autoresRegistrados();

    @Query("SELECT a FROM Autores a WHERE :fechaIngresado >= a.fechaDeNacimiento AND (:fechaIngresado <= a.fechaDeFallecimiento OR a.fechaDeFallecimiento IS NULL)")
    List<Autores> autoresPorFecha(int fechaIngresado);


}
