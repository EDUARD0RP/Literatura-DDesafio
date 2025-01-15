package com.Alura.RepositorioLibros.principal;

import com.Alura.RepositorioLibros.model.*;
import com.Alura.RepositorioLibros.repository.AutoresRepository;
import com.Alura.RepositorioLibros.repository.LibroRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Principal {

    private Scanner teclado = new Scanner(System.in);
    private String  apiURL = "https://gutendex.com/books/?search=";
    private ConsultaApi consultaApi = new ConsultaApi();
    private ConvierteDatos convierteDatos = new ConvierteDatos();
    private String titulodeLibroBuscado;
    private LibroRepository libroRepository;
    private AutoresRepository autoresRepository;

    public Principal(LibroRepository libroRepository, AutoresRepository autoresRepository) {
        this.libroRepository = libroRepository;
        this.autoresRepository = autoresRepository;
    }


    //metodo de mostrar menu
    public void mostrarMenu(){

        var opcionUsuario = -1;
        System.out.println("\n*********** *********** *********** ");
        System.out.println("      Challenge de Literatura       ");
        System.out.println("*********** *********** *********** ");
        while(opcionUsuario != 0){

            System.out.println("\nElija la opcion deseada: ");
            System.out.println("""
                                1.- Buscar un libro por titulo
                                2.-  Listar libros registrados
                                3.-  Listar autores registrados
                                4.-  Listar autores vivos en un determiando año
                                5.-  Listar libros por idioma
                                6.-  Borrar libro de la lista
                    
                                7) Salir
                                """);

            opcionUsuario = teclado.nextInt();
            teclado.nextLine();

            switch (opcionUsuario){

                case 1:
                    buscarLibro();
                     break;

                case 2:
                    listarTodosLosLibros();
                    break;

                case 3:
                    listarAutoresRegistrados();
                    break;

                case 4:
                    listarAutoresVivos();
                    break;

                case 5:
                    listarLibroPorIdioma();
                    break;

                case 6:
                    borrarLibro();
                    break;

                case 7:
                    System.out.println("Saliendo de la aplicacion...");
                    opcionUsuario = 0;
                    break;

                default:
                    System.out.println("Opcion no valida :P");
                    break;



            }

        }

    }



    public DatosJson getDatosJson(){
        System.out.println("Ingrese el titulo del libro: ");
        titulodeLibroBuscado = teclado.nextLine();
        var url = apiURL + titulodeLibroBuscado.replace(" ", "+");
        var json =  consultaApi.consultaDeUrl(url);
        DatosJson datosJson = convierteDatos.obtenerDatos(json,DatosJson.class);
        return datosJson;

    }

    public void buscarLibro() {
        String nombreDelAutor;

        DatosJson datosJson = getDatosJson();

        Optional<DatosLibro> libroEncontrado = datosJson.libros().stream()
                .filter(l -> l.titulo().toLowerCase().contains(titulodeLibroBuscado.toLowerCase()))
                .findFirst();

        if (libroEncontrado.isPresent()) {

            DatosLibro datosLibro = libroEncontrado.get();

            List<Autores> autores = libroEncontrado.get().autores().stream()
                    .map(Autores::new)
                    .collect(Collectors.toList());

            for (int i = 0; i < autores.size(); i++) {
                nombreDelAutor = autores.get(i).getNombre();
                Autores autorEncontrado = autoresRepository.findByNombre(nombreDelAutor);

                if (autorEncontrado == null) {
                    autoresRepository.save(autores.get(i));
                }
            }

            Libro libroCreado = new Libro(datosLibro);
            List<Autores> autoresAGuardar = new ArrayList<>();

            for (int i = 0; i < autores.size() ; i++) {
                nombreDelAutor = autores.get(i).getNombre();
                Autores autorEncontrado = autoresRepository.findByNombre(nombreDelAutor);
                autoresAGuardar.add(autorEncontrado);
            }
            libroCreado.setAutores(autoresAGuardar);

            Optional<Libro> libroExistente = libroRepository.findByTitulo(libroCreado.getTitulo());
            if (libroExistente.isEmpty()) {
                libroRepository.save(libroCreado);
            }

            System.out.println("\n*********** Libro *********** ");
            System.out.println("Titulo: " + libroCreado.getTitulo());
            System.out.println("Autor: " + libroCreado.getAutores());
            System.out.println("Idiomas: " + libroCreado.getIdiomas());
            System.out.println("Numero de descargas: " + libroCreado.getNumeroDeDescargas());
            System.out.println("*********** ***********");


        } else {
            System.out.println("** Libro no Encontrado ** ");
        }
    }

    private void listarTodosLosLibros() {

        List<Libro> librosEnLaBase = libroRepository.findAll();
        System.out.println("\n*********** Libros en Base de Datos ***********");
        mostraLibrosPorConsulta(librosEnLaBase);
    }

    private void listarAutoresRegistrados() {
        List<Autores> autoresEnLaBase = autoresRepository.autoresRegistrados();
        System.out.println("\n*********** Autores registrados *********** ");
        for (Autores autores : autoresEnLaBase) {
            System.out.println("Autor: " + autores.getNombre());
            System.out.println("Fecha de nacimiento: " + autores.getFechaDeNacimiento());
            System.out.println("Fecha de fallecimiento: " + autores.getFechaDeFallecimiento());
            System.out.println("Libros: " + autores.getLibro() + "\n");
            System.out.println("*********** *********** *********** ");

        }

    }

    private void listarAutoresVivos() {
        System.out.println("Ingrese el año para buscar los autores: ");
        var fechaIngresado = teclado.nextInt();
        List<Autores> autoresVivos = autoresRepository.autoresPorFecha(fechaIngresado);

        for (Autores autoresVivo : autoresVivos) {
            System.out.println("\n*********** Autores ***********");
            System.out.println("Autor: " + autoresVivo.getNombre());
            System.out.println("Fecha de nacimiento: " + autoresVivo.getFechaDeNacimiento());
            System.out.println("Fecha de fallecimiento: " + autoresVivo.getFechaDeFallecimiento());
            System.out.println("*********** *********** *********** ");
        }
    }

    private void listarLibroPorIdioma() {
        System.out.println("""
                Ingrese el Idioma a Buscar:
                Español -> es
                Ingles -> en
                Frances  -> fr
                """);
        var idioma = teclado.nextLine();
        List<Libro> librosIdioma = libroRepository.librosPorIdioma(idioma);

        System.out.printf("\n*********** Libros registrados con el idioma '%s'***********\n",idioma);
        mostraLibrosPorConsulta(librosIdioma);
    }

    public void mostraLibrosPorConsulta (List<Libro> libros){

        for (Libro libro : libros) {
            System.out.println("Titulo: " + libro.getTitulo());
            System.out.println("Autores: " + libro.getAutores());
            System.out.println("Idiomas: " + libro.getIdiomas());
            System.out.println("Numero de descargas: " + libro.getNumeroDeDescargas() + "\n");
            System.out.println("*********** *********** ***********");
        }

    }

    private void borrarLibro() {
        listarTodosLosLibros();
        System.out.println("Ingrese el Titulo: ");
        var titulo = teclado.nextLine();
        libroRepository.deleteByTitulo(titulo);
    }
}
