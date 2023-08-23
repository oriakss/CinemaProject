package cinema.repository;

import cinema.model.Movie;

import java.util.List;

public interface MovieRepository {

    boolean checkIsEmptyMovieTable();

    List<Movie> getMovieTable();

    void addToDB(Movie movie);

    void updateInDB(Movie movie);
}
