package org.sid.TDone;

import org.sid.TDone.Entities.Film;
import org.sid.TDone.Entities.Salle;
import org.sid.TDone.Entities.Ticket;
import org.sid.TDone.service.ITDoneInitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;

@SpringBootApplication
public class TDoneApplication implements CommandLineRunner{
	@Autowired
	private ITDoneInitService tDoneInitService;
	@Autowired
	private RepositoryRestConfiguration restConfiguration;

	public static void main(String[] args) {
		SpringApplication.run(TDoneApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		restConfiguration.exposeIdsFor(Film.class,Salle.class,Ticket.class);
		tDoneInitService.initVilles();
		tDoneInitService.initCinemas();
		tDoneInitService.initSalles();
		tDoneInitService.initPlaces();
		tDoneInitService.initSeances();
		tDoneInitService.initCategories();
		tDoneInitService.initFilms();
		tDoneInitService.initProjectionFilms();
		tDoneInitService.initTickeks();
		
	}

}
