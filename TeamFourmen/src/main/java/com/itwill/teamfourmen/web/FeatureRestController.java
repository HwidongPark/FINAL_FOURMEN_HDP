package com.itwill.teamfourmen.web;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.itwill.teamfourmen.domain.Review;
import com.itwill.teamfourmen.domain.TmdbLike;
import com.itwill.teamfourmen.service.FeatureService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/feature")
public class FeatureRestController {
    
	private final FeatureService featureService;
	
	@PostMapping("/review/post")
	public void postReview(@RequestBody Review review) {
		 
		 log.info("postReview(reviewdto={})", review);
		 featureService.postReview(review);
		 		 		 
	}
	
	
	@Transactional
	@PostMapping("/like/add")
	public void addLike(@RequestBody TmdbLike tmdbLike) {
		
		log.info("addLike(tmdbLike = {})", tmdbLike);
		
		featureService.addLike(tmdbLike);
		
	}
	
	@Transactional
	@PostMapping("/like/delete")
	public void deleteLike(@RequestBody TmdbLike tmdbLike) {
		
		log.info("deleteLike(tmdbLike={})", tmdbLike);
		
		featureService.deleteLike(tmdbLike);
		
	}
		
	
}
