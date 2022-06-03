package org.sid.TDone.service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

import javax.transaction.Transactional;

import org.sid.TDone.Entities.Categorie;
import org.sid.TDone.Entities.Cinema;
import org.sid.TDone.Entities.Film;
import org.sid.TDone.Entities.Place;
import org.sid.TDone.Entities.ProjectionFilm;
import org.sid.TDone.Entities.Salle;
import org.sid.TDone.Entities.Seance;
import org.sid.TDone.Entities.Ticket;
import org.sid.TDone.Entities.Ville;
import org.sid.TDone.dao.CategorieRepository;
import org.sid.TDone.dao.CinemaRepository;
import org.sid.TDone.dao.FilmRepository;
import org.sid.TDone.dao.PlaceRepository;
import org.sid.TDone.dao.ProjectionFilmRepository;
import org.sid.TDone.dao.SalleRepository;
import org.sid.TDone.dao.SeanceRepository;
import org.sid.TDone.dao.TicketRepository;
import org.sid.TDone.dao.VilleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class TDoneInitServiceimpl implements ITDoneInitService {
	@Autowired
	private VilleRepository villeRepository;
	@Autowired
	private CinemaRepository cinemaRepository;
	@Autowired
	private SalleRepository salleRepository;
	@Autowired
	private PlaceRepository placeRepository;
	@Autowired
	private SeanceRepository seanceRepository;
	@Autowired
	private FilmRepository filmRepository;
	@Autowired
	private ProjectionFilmRepository projectionFilmRepository;
	@Autowired
	private CategorieRepository categorieRepository;
	@Autowired
	private TicketRepository ticketRepository;
	


	@Override
	public void initVilles() {
		Stream.of("Beni","Bunia","Uvira","Goma").forEach(nameVille->{
			Ville ville=new Ville();
			ville.setName(nameVille);
			villeRepository.save(ville);
		});
		
	}

	@Override
	public void initCinemas() {
		villeRepository.findAll().forEach(v->{
			Stream.of("NEW VISION","CONGO DEBOUT","LESOTO","UN JOUR NOUVEAU","TELEMA")
			.forEach(nameCinema->{
				Cinema cinema= new Cinema();
				cinema.setName(nameCinema);
				cinema.setNombredesalle(3+(int)(Math.random()*7));
				cinema.setVille(v);
				cinemaRepository.save(cinema);
				
			});
		});
		
	}

	@Override
	public void initSalles() {
		cinemaRepository.findAll().forEach(cinema->{
			for(int i=0;i<cinema.getNombredesalle();i++) {
				Salle salle=new Salle();
				salle.setName("Salle"+(i+1));
				salle.setCinema(cinema);
				salle.setNombresPlaces(15+(int)(Math.random()*20));
				salleRepository.save(salle);
			}
		});
		
	}
	@Override
	public void initPlaces() {
		salleRepository.findAll().forEach(salle->{
			for(int i=0;i<salle.getNombresPlaces();i++) {
				Place place=new Place();
				place.setNumero(i+1);
				place.setSalle(salle);
				placeRepository.save(place);
			}
		});
		
	}
	
	@Override
	public void initSeances() {
		DateFormat dateFormat=new SimpleDateFormat("HH:mm");
		Stream.of("11:00","14:00","18:00","20:00","00:00").forEach(s->{
			Seance seance=new Seance();
			try {
				seance.setHeureDebut(dateFormat.parse(s));
				seanceRepository.save(seance);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
		
	}

	

	@Override
	public void initCategories() {
		Stream.of("Actions","Comedie","Serie","Theatre").forEach(cat->{
			Categorie categorie=new Categorie();
			categorie.setName(cat);
			categorieRepository.save(categorie);
		});
		
	}

	@Override
	public void initFilms() {
		double[] durees=new double[] {1,1.5,2,2.5,3};
		List<Categorie> categories=categorieRepository.findAll();
		Stream.of("ForrsetGump","LaligneVerte","GreenBook","LeParin","LeSeigneurdesanneaux","12Homesencolere")
		.forEach(titreFilm->{
			Film film=new Film();
			film.setTitre(titreFilm);
			film.setDuree(durees[new Random().nextInt(durees.length)]);
			film.setPhoto(titreFilm.replaceAll(" ", "")+".jpg");
			film.setCategorie(categories.get(new Random().nextInt(categories.size())));
			filmRepository.save(film);
		});
		
		
	}

	@Override
	public void initProjectionFilms() {
		double[] prices=new double[] {50,100,200,300,500,100};
		List<Film> films=filmRepository.findAll();
		villeRepository.findAll().forEach(ville->{
			ville.getCinemas().forEach(cinema->{
				cinema.getSalles().forEach(salle->{
					int index=new Random().nextInt(films.size());
					Film film=films.get(index);
						seanceRepository.findAll().forEach(seance->{
						ProjectionFilm projection=new ProjectionFilm();
						projection.setDateProjection(new Date());
						projection.setFilm(film);
						projection.setPrix(prices[new Random().nextInt(prices.length)]);
						projection.setSalle(salle);
						projection.setSeance(seance);
						projectionFilmRepository.save(projection);
						});

				});
			});
		});
		
	}

	@Override
	public void initTickeks() {
		projectionFilmRepository.findAll().forEach(p->{
			p.getSalle().getPlaces().forEach(place->{
				Ticket ticket=new Ticket();
				ticket.setPlace(place);
				ticket.setPrix(p.getPrix());
				ticket.setProjectionFilm(p);
				ticket.setReserve(false);
				ticketRepository.save(ticket);
			});
		});
		
	}

}
