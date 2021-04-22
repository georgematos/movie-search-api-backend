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

	@Value("${omdb.api.url}")
	private String resourceUrl;

	@Value("${omdb.api.key}")
	private String apiKey;

	private RestTemplate restTemplate;

	@Autowired
	public MovieService(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	public List<Movie> search(String name) {

		List<Movie> movies = new ArrayList<>();

		StringBuilder stringBuilder = new StringBuilder();
		Formatter fmt = new Formatter(stringBuilder);
		fmt.format(resourceUrl, name, apiKey);
		String finalUrl = fmt.toString();
		ResponseEntity<String> response = restTemplate.getForEntity(finalUrl, String.class);
		fmt.close();

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

}
