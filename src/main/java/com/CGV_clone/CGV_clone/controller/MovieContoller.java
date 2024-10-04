package com.CGV_clone.CGV_clone.controller;

import com.CGV_clone.CGV_clone.DTO.MovieDto;
import com.CGV_clone.CGV_clone.domain.Movie;
import com.CGV_clone.CGV_clone.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/movies")
public class MovieContoller {

    @Autowired
    private MovieService movieService;

    // 영화 목록 조회 (DB에서)
//    @GetMapping("/popular")
//    public List<Movie> getPopularMovies() {
//        return movieService.getPopularMovies();
//    }
    // 인기 영화 리스트 가져오기 (프론트로 보낼 데이터)
    @GetMapping("/movies/popular")
    public List<MovieDto> getPopularMovies() {
        return movieService.getPopularMovies().stream().map(movie -> {
            MovieDto dto = new MovieDto();
            dto.setId(movie.getId());
            dto.setTitle(movie.getTitle());
            dto.setOriginalLanguage(movie.getOriginalLanguage());
            dto.setOverview(movie.getOverview());
            dto.setPosterPath(movie.getPosterPath());
            dto.setReleaseDate(movie.getReleaseDate());
            dto.setVoteAverage(movie.getVoteAverage());
            dto.setGenreIds(movie.getGenreIds());
            return dto;
        }).collect(Collectors.toList());
    }

    // TMDB API에서 Now Playing 영화 가져오기
    @GetMapping("/now-playing")
    public List<Movie> getNowPlayingMovies() {
        return movieService.getNowPlayingMovies();
    }

    // 영화 상세 정보 조회
    @GetMapping("/{id}")
    public Movie getMovieById(@PathVariable Long id) {
        return movieService.getMovieById(id);
    }

    // 영화 저장 (단일 저장)
    @PostMapping
    public Movie saveMovie(@RequestBody Movie movie) {
        return movieService.saveMovie(movie);
    }

    // 영화 저장 (복수 배열 저장)  -> 아직 안 됨 아마 고유 Id 문제
//    @PostMapping("/bulk")
//    public List<Movie> saveMovies(@RequestBody List<Movie> movies) {
//        return movieService.saveMovies(movies);
//    }


    // 영화 삭제
    @DeleteMapping("/{id}")
    public void deleteMovie(@PathVariable Long id) {
        movieService.deleteMovie(id);
    }

}
