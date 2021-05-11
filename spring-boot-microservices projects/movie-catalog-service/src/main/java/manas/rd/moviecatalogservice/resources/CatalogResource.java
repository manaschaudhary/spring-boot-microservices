package manas.rd.moviecatalogservice.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
//import org.springframework.web.reactive.function.client.WebClient;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import manas.rd.moviecatalogservice.models.CatalogItem;
import manas.rd.moviecatalogservice.models.Movie;
import manas.rd.moviecatalogservice.models.Rating;
import manas.rd.moviecatalogservice.models.UserRating;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/catalog")
public class CatalogResource {
    
    @Autowired
    private MovieInfo movieInfo;
    
    @Autowired
    private RatingInfo ratingInfo;
        
    public List<CatalogItem> getFallbackCatalog(@PathVariable("userId") String userId) {
    	return Arrays.asList(new CatalogItem("No Movie", "No Desc", 0));
    }

    @RequestMapping("/{userId}")
    public List<CatalogItem> getCatalog(@PathVariable("userId") String userId) {

//        UserRating userRating = restTemplate.getForObject("http://ratings-data-service/ratingsdata/user/" + userId, UserRating.class);
//
//        return userRating.getRatings().stream()
//                .map(rating -> {
//                    Movie movie = restTemplate.getForObject("http://movie-info-service/movies/" + rating.getMovieId(), Movie.class);
//                    return new CatalogItem(movie.getName(), movie.getDescription(), rating.getRating());
//                })
//                .collect(Collectors.toList());

    //1) return Collections.singletonList(new CatalogItem("Transformer", "Transformer description", 4));
    	
//    	List<Rating> ratings = Arrays.asList(
//        		new Rating("1234", 3),
//        		new Rating("5467",4)  
//        );
    	
    	UserRating userRating = ratingInfo.getUserRatings(userId);
    	
    	return userRating.getRatings().stream().map(rating -> 
    		movieInfo.getCatalogItem(rating)
    	).collect(Collectors.toList());
    }
    
    
   
    
}

/*
Alternative WebClient way
Movie movie = webClientBuilder.build().get().uri("http://localhost:8082/movies/"+ rating.getMovieId())
.retrieve().bodyToMono(Movie.class).block();
*/