package cinema.service;

import cinema.model.Movie;
import cinema.repository.MovieRepository;

import java.util.List;

public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;

    @Override
    public boolean checkIsEmptyMovieTable() {
        return movieRepository.checkIsEmptyMovieTable();
    }

    @Override
    public List<Movie> getMovieTable() {
        return movieRepository.getMovieTable();
    }

    @Override
    public void addToDB(Movie movie) {
        movieRepository.addToDB(movie);
    }

    @Override
    public void updateInDB(Movie movie) {
        movieRepository.updateInDB(movie);
    }

    public MovieServiceImpl(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }
}
