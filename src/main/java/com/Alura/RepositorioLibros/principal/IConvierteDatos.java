package com.Alura.RepositorioLibros.principal;

public interface IConvierteDatos {
    <T> T obtenerDatos(String json, Class<T> clase);
}
