package com.itwill.teamfourmen.service;

import java.util.List;
import java.util.Optional;

import com.itwill.teamfourmen.domain.*;
import com.itwill.teamfourmen.dto.comment.ReviewLikeDTO;
import com.itwill.teamfourmen.repository.ReviewCommentsRepository;
import org.springframework.stereotype.Service;

import com.itwill.teamfourmen.repository.ReviewDao;
import com.itwill.teamfourmen.repository.ReviewLikeRepository;
import com.itwill.teamfourmen.repository.TmdbLikeDao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class FeatureService {

	private final ReviewDao reviewDao;
	private final TmdbLikeDao tmdbLikeDao;
	private final ReviewLikeRepository reviewLikeDao;
	private final ReviewCommentsRepository commentDao;

	public void postReview(Review review) {

		log.info("postReview(reviewDto={})", review);

		review = reviewDao.save(review);

	}

	public List<Review> getReviews(String category, int tmdbId) {

		log.info("getReviews(category={}, id={})", category, tmdbId);

		List<Review> tmdbReviewList = reviewDao.findByCategoryAndTmdbId(category, tmdbId);

		return tmdbReviewList;
	}

	public Integer getNumOfReviewComment(Long reivewId){
		log.info("GET NUM OF REVIEW COMMENT REVIEW ID  = {} ",reivewId);

		Review review = Review.builder().reviewId(reivewId).build();

		List<ReviewComments> listComment = commentDao.findByReview(review);

		int numOfComment = 0;

		if(listComment != null) {
			numOfComment = listComment.size();
		} else {
			 numOfComment = 0;
		}

		log.info("COMMENT NUM = {}" , numOfComment);

		return numOfComment;
	}

	public Review getMyReviewInTmdbWork(String email, String category, int tmdbId) {

		log.info("getMyReviewInTmdbWork(email={}, category={}, tmdbId={}", email, category, tmdbId);

		Optional<Review> myTmdbReviewOptional = reviewDao.findByMemberEmailAndCategoryAndTmdbId(email, category, tmdbId);
		Review myTmdbReview = myTmdbReviewOptional.orElse(null);

		return myTmdbReview;
	}


	public TmdbLike didLikeTmdb(Member member, String category, int tmdbId) {

		log.info("didLikeTmdb(member={})", member);

		Optional<TmdbLike> tmdbLikeOptional = tmdbLikeDao.findByMemberEmailAndCategoryAndTmdbId(member.getEmail(), category, tmdbId);

		TmdbLike tmdbLike = tmdbLikeOptional.orElse(null);
		log.info("tmdbLike={}", tmdbLike);


		return tmdbLike;
	}


	public void addLike(TmdbLike tmdbLike) {

		log.info("addLike(tmdbLike={})", tmdbLike);

		tmdbLike = tmdbLikeDao.save(tmdbLike);

		log.info("저장된 tmdbLike={}", tmdbLike);

	}

	public void deleteLike(TmdbLike tmdbLike) {

		log.info("deleteLike(tmdbLike={})", tmdbLike);
		tmdbLikeDao.deleteByMemberEmailAndCategoryAndTmdbId(tmdbLike.getMember().getEmail(), tmdbLike.getCategory(), tmdbLike.getTmdbId());

	}

	public Long getNumOfReviewLike(Long reviewId){
		log.info("GET NUM OF REVIEW LIKE = {}" ,reviewId);

		Review review = reviewDao.findByReviewId(reviewId);

		Long countReviewLike = reviewLikeDao.countByReview(review);

		if(countReviewLike == 0L){
		   return 0L;
		} else {
			return countReviewLike;
		}
	}


	public void controllReviewLike(ReviewLikeDTO reviewLikeDTO) {
		log.info("addReviewLike(reviewLike={})", reviewLikeDTO);

		Review review = Review.builder()
				.reviewId(reviewLikeDTO.getReviewId())
				.build();

		Member member = Member.builder()
				.email(reviewLikeDTO.getEmail())
				.build();

		ReviewLike isLikedReview = reviewLikeDao.findByReviewAndMember(review , member);

		log.info("IS REVIEW LIKED? = {}", isLikedReview);

		boolean didReviewLike = didReviewLike(review, member);

		if(!didReviewLike) {
			ReviewLike entity = ReviewLike.builder()
					.review(review)
					.member(member)
					.build();

			reviewLikeDao.save(entity);
		} else {


			ReviewLike entity = reviewLikeDao.findByReviewAndMember(review, member);

			if(entity != null) {
				reviewLikeDao.delete(entity);
			}
		}
	}

	public boolean didReviewLike(Review review, Member member) {
		log.info("DID REVIEW LIKED ?? REVIEW = {}, MEMBER = {}", review, member);

		Review targetReview = reviewDao.findByReviewId(review.getReviewId());

		log.info("TARGET REVIEW REVIEW ID = {}, REVIEW CONTENT = {}", targetReview.getReviewId(), targetReview.getContent());

		ReviewLike reviewLike = reviewLikeDao.findByReviewAndMember(targetReview, member);

		log.info("target Review LiKE = {}", reviewLike);

		if (reviewLike != null) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * MyPage에서 내가 작성한 모든 리뷰를 가져오기 위함
	 * - 작성자 권오중
	 * @param email
	 * @return Review 리스트
	 */
	public List<Review> getAllMyReview (String email) {
		log.info("GET ALL MY REVIEW E-MAIL = {}", email);

		List<Review> myAllReivew = reviewDao.findByMemberEmail(email);

		for(Review review : myAllReivew) {
			log.info("My review = {}", review);
		}

		return myAllReivew;
	}

	public List<TmdbLike> getLikedList (Member member, String category){
		log.info("GET LIKED LIST OF CATEGORY = {}, MEMBER EMAIL = {}", category, member.getEmail());

		List<TmdbLike> likedList = tmdbLikeDao.findByMemberEmailAndCategory(member.getEmail(), category);

		return likedList;
	}

}