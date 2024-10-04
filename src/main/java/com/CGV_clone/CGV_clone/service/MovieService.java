package com.CGV_clone.CGV_clone.service;

import com.CGV_clone.CGV_clone.domain.Movie;
import com.CGV_clone.CGV_clone.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


@Service
@Transactional(readOnly = true)
public class MovieService {

    @Autowired
    private MovieRepository movieRepository;

    @Value("${tmdb.api-key}")
    private String apiKey;

    @Autowired
    private RestTemplate restTemplate;

    @Transactional
    public List<Movie> getNowPlayingMovies() {
        String url = "https://api.themoviedb.org/3/movie/now_playing?api_key=" + apiKey + "&language=ko-KR&page=1";
        List<Movie> movies = new ArrayList<>();

        try {
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);
            System.out.println("API Response: " + response); // 응답 전체 출력


            List<Map<String, Object>> results = (List<Map<String, Object>>) response.get("results");
            System.out.println("Results: " + results); // 결과 데이터 출력


            for (Map<String, Object> result : results) {
                Movie movie = new Movie();
                movie.setOriginalLanguage((String) result.get("original_language"));
                movie.setOverview(result.get("overview") != null ? (String) result.get("overview") : "");
                movie.setPosterPath(result.get("poster_path") != null ? (String) result.get("poster_path") : "");

                String releaseDateStr = (String) result.get("release_date");
                if (releaseDateStr != null && !releaseDateStr.isEmpty()) {
                    try {
                        movie.setReleaseDate(LocalDate.parse(releaseDateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                    } catch (DateTimeParseException e) {
                        e.printStackTrace();
                        movie.setReleaseDate(null);
                    }
                } else {
                    movie.setReleaseDate(null);
                }

                movie.setTitle((String) result.get("title"));
                movie.setVoteAverage(Double.valueOf(result.get("vote_average").toString()));
                movie.setGenreIds((List<Integer>) result.get("genre_ids"));
                movies.add(movie);
            }

            // 영화 데이터를 DB에 저장하고 저장된 영화 수를 출력
            movies = movieRepository.saveAll(movies);
            System.out.println("Saved movies: " + movies.size());  // 저장된 영화의 수를 출력

            return movies;

        } catch (Exception e) {
            e.printStackTrace();
            return movies;  // 예외 발생 시 빈 리스트 반환
        }
    }


    // 영화 목록 조회 (DB에서)
    public List<Movie> getPopularMovies() {
        return movieRepository.findAll();
    }

    // 영화 상세 정보 조회
    public Movie getMovieById(Long id) {
        return movieRepository.findById(id).orElse(null);
    }

    @Transactional
    public Movie saveMovie(Movie movie) {
        return movieRepository.save(movie);
    }

    @Transactional
    public void deleteMovie(Long id) {
        movieRepository.deleteById(id);
    }
}



//package com.CGV_clone.CGV_clone.service;
//
//import com.CGV_clone.CGV_clone.domain.Movie;
//import com.CGV_clone.CGV_clone.repository.MovieRepository;
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.web.client.RestTemplate;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//@Transactional(readOnly = true)
//@Service
//public class MovieService {
//
//    @Autowired
//    private MovieRepository movieRepository;
//
//    //영화 목록 조회
//    public List<Movie> getPopularMovies() {
//        return movieRepository.findAll(); // 기본적으로 모든 영화를 반환
//    }
//
//    // 영화 상세 정보 조회
//    public Movie getMovieById(Long id) {
//        Optional<Movie> movie = movieRepository.findById(id);
//        return movie.orElse(null); // 영화가 없으면 null 반환
//    }
//
//    @Transactional
//    // 영화 저장 (단일 저장)
//    public Movie saveMovie(Movie movie) {
//        return movieRepository.save(movie);
//    }
//
//    // 영화 저장 (복수 배열 저장)
//    @Transactional
//    public List<Movie> saveMovies(List<Movie> movies) {
//        return movieRepository.saveAll(movies);
//    }
//
//    @Transactional
//    // 영화 삭제
//    public void deleteMovie(Long id) {
//        movieRepository.deleteById(id);
//    }
//
//    // TMDB API에서 현재 상영 중인 영화 데이터를 받아오기
//    @Transactional
//    public List<Movie> getNowPlayingMovies() {
//        String url = "https://api.themoviedb.org/3/movie/now_playing?api_key=" + apiKey + "&language=ko-KR&page=1";
//        RestTemplate restTemplate = new RestTemplate();
//        String jsonResponse = restTemplate.getForObject(url, String.class);
//
//        // JSON 데이터를 Movie 리스트로 변환
//        List<Movie> movies = new ArrayList<>();
//        ObjectMapper objectMapper = new ObjectMapper();
//
//        try {
//            JsonNode rootNode = objectMapper.readTree(jsonResponse);
//            JsonNode resultsNode = rootNode.path("results");
//
//            for (JsonNode movieNode : resultsNode) {
//                Movie movie = new Movie();
//                movie.setTitle(movieNode.path("title").asText());
//                movie.setOverview(movieNode.path("overview").asText());
//                movie.setReleaseDate(movieNode.path("release_date").asText());
//                movie.setVoteAverage(movieNode.path("vote_average").asDouble());
//                movie.setPosterPath(movieNode.path("poster_path").asText());
//                // 필요한 필드들을 추가로 설정
//                movies.add(movie);
//            }
//
//            // 받아온 데이터를 데이터베이스에 저장
//            saveMovies(movies);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return movies;
//    }
//
//
//}
