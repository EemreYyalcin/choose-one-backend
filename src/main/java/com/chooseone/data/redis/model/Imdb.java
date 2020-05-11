package com.chooseone.data.redis.model;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class Imdb{

	@JsonProperty("Released")
	private String released;

	@JsonProperty("totalSeasons")
	private String totalSeasons;

	@JsonProperty("Metascore")
	private String metascore;

	@JsonProperty("imdbID")
	private String imdbID;

	@JsonProperty("Plot")
	private String plot;

	@JsonProperty("Director")
	private String director;

	@JsonProperty("Title")
	private String title;

	@JsonProperty("Actors")
	private String actors;

	@JsonProperty("imdbRating")
	private String imdbRating;

	@JsonProperty("imdbVotes")
	private String imdbVotes;

	@JsonProperty("Ratings")
	private List<RatingsItem> ratings;

	@JsonProperty("Response")
	private String response;

	@JsonProperty("Runtime")
	private String runtime;

	@JsonProperty("Type")
	private String type;

	@JsonProperty("Awards")
	private String awards;

	@JsonProperty("Year")
	private String year;

	@JsonProperty("Language")
	private String language;

	@JsonProperty("Rated")
	private String rated;

	@JsonProperty("Poster")
	private String poster;

	@JsonProperty("Country")
	private String country;

	@JsonProperty("Genre")
	private String genre;

	@JsonProperty("Writer")
	private String writer;

}