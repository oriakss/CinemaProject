package cinema.service;

import cinema.model.Movie;

import java.util.List;

public interface MovieService {

    boolean checkIsEmptyMovieTable();

    List<Movie> getMovieTable();

    void addToMovieTable(Movie movie);

    void removeFromMovieTable(String title);

    void updateMovieTable(Movie movie);
}
