package com.Alura.RepositorioLibros;

import com.Alura.RepositorioLibros.principal.ConsultaApi;
import com.Alura.RepositorioLibros.principal.Principal;
import com.Alura.RepositorioLibros.repository.AutoresRepository;
import com.Alura.RepositorioLibros.repository.LibroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class RepositorioLibrosApplication implements CommandLineRunner {

	@Autowired
	private LibroRepository libroRepository;
	@Autowired
	private AutoresRepository autoresRepository;

	public static void main(String[] args) {
		SpringApplication.run(RepositorioLibrosApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		Principal principal = new Principal(libroRepository,autoresRepository);
		principal.mostrarMenu();

	}
}
