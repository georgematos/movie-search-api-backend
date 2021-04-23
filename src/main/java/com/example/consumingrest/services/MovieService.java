package com.example.consumingrest.services;

import java.util.ArrayList;
import java.util.Formatter;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.consumingrest.domain.Movie;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class MovieService {

	@Value("${omdb.api.url.search}")
	private String resourceUrlSearch;

	@Value("${omdb.api.url.id}")
	private String resourceUrlId;

	@Value("${omdb.api.key}")
	private String apiKey;

	private RestTemplate restTemplate;

	@Autowired
	public MovieService(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	public List<Movie> search(String name) {

		List<Movie> movies = new ArrayList<>();

		String finalUrl = getFinalUrl(name, resourceUrlSearch);

		ResponseEntity<String> response = restTemplate.getForEntity(finalUrl, String.class);

		ObjectMapper mapper = new ObjectMapper();

		try {
			JsonNode root = mapper.readTree(response.getBody());
			Iterator<JsonNode> elements = root.elements();
			JsonNode movieList = elements.next();
			movieList.forEach((x) -> {
				JsonNode id = x.path("imdbID");
				JsonNode title = x.path("Title");
				JsonNode year = x.path("Year");
				JsonNode poster = x.path("Poster");

				Movie movie = new Movie();
				movie.setId(id.asText());
				movie.setTitle(title.asText());
				movie.setYear(year.asText());
				movie.setPoster(poster.asText());

				movies.add(movie);
			});
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		return movies;
	}

	public Movie getMovie(String id) {

		Movie movie = new Movie();

		String finalUrl = getFinalUrl(id, resourceUrlId);

		ResponseEntity<String> response = restTemplate.getForEntity(finalUrl, String.class);

		ObjectMapper mapper = new ObjectMapper();

		try {
			JsonNode root = mapper.readTree(response.getBody());

			movie.setId(root.path("imdbID").asText());
			movie.setTitle(root.path("Title").asText());
			movie.setYear(root.path("Year").asText());
			movie.setPoster(root.path("Poster").asText());
			movie.setActors(root.path("Actors").asText());
			movie.setCountry(root.path("Country").asText());
			movie.setDirector(root.path("Director").asText());
			movie.setGenre(root.path("Genre").asText());
			movie.setLanguage(root.path("Language").asText());
			movie.setRuntime(root.path("Runtime").asText());
			movie.setImdbRating(root.path("imdbRating").asText());
			movie.setWriter(root.path("Writer").asText());
			movie.setPlot(root.path("Plot").asText());

		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		return movie;
	}

	private String getFinalUrl(String id, String resourceUrl) {
		StringBuilder stringBuilder = new StringBuilder();
		Formatter fmt = new Formatter(stringBuilder);
		fmt.format(resourceUrl, id, apiKey);
		String finalUrl = fmt.toString();
		fmt.close();
		return finalUrl;
	}

}
