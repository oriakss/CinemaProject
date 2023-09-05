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
    public void addToMovieTable(Movie movie) {
        movieRepository.addToMovieTable(movie);
    }

    @Override
    public void removeFromMovieTable(String title) {
        movieRepository.removeFromMovieTable(title);
    }

    @Override
    public void updateMovieTable(Movie movie) {
        movieRepository.updateMovieTable(movie);
    }

    public MovieServiceImpl(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }
}
