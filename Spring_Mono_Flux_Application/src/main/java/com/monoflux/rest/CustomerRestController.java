package com.monoflux.rest;

import java.sql.Date;
import java.time.Duration;
import java.util.stream.Stream;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.monoflux.binding.CustomerEvent;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

@RestController
public class CustomerRestController {
 
 @GetMapping(value="/event",produces = MediaType.APPLICATION_PROBLEM_JSON_VALUE)
  public Mono<CustomerEvent> monoObject(){
	  
	  CustomerEvent customerEvent=new CustomerEvent("naresh", new java.util.Date());
		
	  return Mono.justOrEmpty(customerEvent);
	}
	
	@GetMapping(value="/events",produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<CustomerEvent> fluxEvents(){
		CustomerEvent event=new CustomerEvent("Mouri", new java.util.Date());
		
		//Generating Stream Of Objects using generate method 
		Stream<CustomerEvent> stream=Stream.generate(()->event);
		
		//creating Flux Object 
	    Flux<CustomerEvent> flux = Flux.fromStream(stream);
	    
	    //Setting Time interval for every object in stream 
	    Flux<Long> interval = Flux.interval(Duration.ofSeconds(5));
	    
	    //combining flux and time interval 
	    Flux<Tuple2<Long, CustomerEvent>> zip = Flux.zip(interval, flux);
	    
	    //fetch the flux object from zip
	    Flux<CustomerEvent> map = zip.map(Tuple2::getT2);
	    
	    return map;
	   
	}
}
