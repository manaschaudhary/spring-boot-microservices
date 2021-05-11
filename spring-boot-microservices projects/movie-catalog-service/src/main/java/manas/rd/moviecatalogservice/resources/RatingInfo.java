package manas.rd.moviecatalogservice.resources;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;

import manas.rd.moviecatalogservice.models.Rating;
import manas.rd.moviecatalogservice.models.UserRating;

@Service
public class RatingInfo {
	@Autowired
    private RestTemplate restTemplate;
	
	@HystrixCommand(fallbackMethod = "getFallbackUserRatings", commandProperties = {
			@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value="2000"),
			@HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "4"),
			@HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value="50")
	})
	public UserRating getUserRatings(String userId) {
		return restTemplate.getForObject("http://ratings-data-service/ratingsdata/user/" + userId, UserRating.class);
	};
	
	public UserRating getFallbackUserRatings(String userId) {
		UserRating userRating = new UserRating();
		userRating.setRatings(Arrays.asList(new Rating("0", 0)));
		return userRating;
	}

}
