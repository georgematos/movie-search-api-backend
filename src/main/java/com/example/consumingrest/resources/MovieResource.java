package com.example.consumingrest.resources;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.consumingrest.domain.Movie;
import com.example.consumingrest.services.MovieService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
@RequestMapping(value = "/movieapi")
public class MovieResource {

	
	private MovieService service;
	
	@Autowired
	public MovieResource(MovieService service) {
		this.service = service;
	}

	@GetMapping("/search/{name}")
	public ResponseEntity<List<Movie>> searchMovies(@PathVariable String name)
			throws JsonMappingException, JsonProcessingException {

		List<Movie> movies = service.search(name);

		return ResponseEntity.ok().body(movies);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Movie> getMovie(@PathVariable String id)
			throws JsonMappingException, JsonProcessingException {

		Movie movie = service.getMovie(id);

		return ResponseEntity.ok().body(movie);
	}

}
