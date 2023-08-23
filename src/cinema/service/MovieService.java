package cinema.service;

import cinema.model.Movie;

import java.util.List;

public interface MovieService {

    boolean checkIsEmptyMovieTable();

    List<Movie> getMovieTable();

    void addToDB(Movie movie);

    void updateInDB(Movie movie);
}
