package cinema.repository;

import cinema.model.Movie;

import java.util.List;

public interface MovieRepository {

    boolean checkIsEmptyMovieTable();

    List<Movie> getMovieTable();

    void addToMovieTable(Movie movie);

    void updateMovieTable(Movie movie);
}
