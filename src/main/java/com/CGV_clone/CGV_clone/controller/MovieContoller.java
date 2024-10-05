package com.CGV_clone.CGV_clone.controller;

import com.CGV_clone.CGV_clone.DTO.MovieDto;
import com.CGV_clone.CGV_clone.DTO.MovieMainDto;
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

    // 메인페이지!! api 받아오기
    @GetMapping("/main")
    public List<MovieMainDto> getMoviesForMainPage() {
        return movieService.getNowPlayingMovies().stream().map(movie -> {
            MovieMainDto dto = new MovieMainDto();
            dto.setId(movie.getId());
            dto.setTitle(movie.getTitle());
            dto.setPosterPath(movie.getPosterPath());
            dto.setVoteAverage(movie.getVoteAverage());
            return dto;
        }).collect(Collectors.toList());
    }

    // 상세페이지!! api 받아오기
    @GetMapping("/{id}")
    public Movie getMovieById(@PathVariable Long id) {
        return movieService.getMovieById(id);
    }



    // 인기 영화 리스트 가져오기 (api 요청때마다 주기적으로 갱신)
    @GetMapping("/popular")
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

    // 실시간으로 상영중인 영화 리스트 가져오기 (api 요청떄마다 주기적으로 갱신)
    @GetMapping("/now-playing")
    public List<Movie> getNowPlayingMovies() {
        return movieService.getNowPlayingMovies();
    }


    // 영화 저장 (단일 저장) == 일단 안 씀
    @PostMapping
    public Movie saveMovie(@RequestBody Movie movie) {
        return movieService.saveMovie(movie);
    }


    // 영화 삭제 == 이것도 잘 안 씀
    @DeleteMapping("/{id}")
    public void deleteMovie(@PathVariable Long id) {
        movieService.deleteMovie(id);
    }

    // 영화 저장 (복수 배열 저장)  -> 아직 안 됨 아마 고유 Id 문제
//    @PostMapping("/bulk")
//    public List<Movie> saveMovies(@RequestBody List<Movie> movies) {
//        return movieService.saveMovies(movies);
//    }
}
