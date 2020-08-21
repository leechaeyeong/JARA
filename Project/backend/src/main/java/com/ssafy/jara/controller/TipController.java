package com.ssafy.jara.controller;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.jara.dto.Tip;
import com.ssafy.jara.service.TipCommentService;
import com.ssafy.jara.service.TipService;

import io.swagger.annotations.ApiOperation;

@CrossOrigin(origins = { "*" }, maxAge = 6000)
@RestController
@RequestMapping("/jara/tips")
public class TipController {

	@Autowired
	TipService tipService;
	
	@Autowired
	TipCommentService tipCommentService;
	
	@ApiOperation(value = "팁 등록", response = String.class)
	@PostMapping("")
	private ResponseEntity<Integer> insertTip(@RequestBody Tip tip) {
		
		String img_src = tip.getImg_src();
		
		// 작성자가 이미지를 등록하지 않은 경우, 기본 이미지 자라로 설정
		if (img_src == null) {
			switch (tip.getTag_id()) { // 1 요리 2 세탁 3 청소 4 보관
			case 1:
				img_src = "https://firebasestorage.googleapis.com/v0/b/jara-8c5be.appspot.com/o/default%2Fyorijara.png?alt=media&token=99862c57-acdd-46f8-a3d5-992eab8582c8";
				break;
			case 2:
				img_src = "https://firebasestorage.googleapis.com/v0/b/jara-8c5be.appspot.com/o/default%2Flaundryjara.png?alt=media&token=54fa2d72-dcec-4381-85c9-ffa6f5af4170";
				break;
			case 3:
				img_src = "https://firebasestorage.googleapis.com/v0/b/jara-8c5be.appspot.com/o/default%2Fcleanjara.png?alt=media&token=2f6e3e49-f3a6-408a-b1f8-ae7dc6037595";
				break;
			case 4:
				img_src = "https://firebasestorage.googleapis.com/v0/b/jara-8c5be.appspot.com/o/default%2Fbogwanjara.png?alt=media&token=5eeaa65e-c19c-4f9c-b256-96dec1b1f99e";
				break;
			}
		}
		
		tip.setImg_src(img_src);
		
		if(tipService.insertTip(tip) > 0) {
			return new ResponseEntity<Integer>(tip.getId(), HttpStatus.OK);
		} 
		
		return new ResponseEntity<Integer>(0, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ApiOperation(value = "팁 이미지 경로(firebase) 저장", response = String.class)
	@PutMapping("/{id}/img")
	private ResponseEntity<String> insertTipImg(@RequestBody Tip tip) {
		HashMap<String, Object> hashMap = new HashMap<String, Object>();
		hashMap.put("id", tip.getId());
		hashMap.put("img_src", tip.getImg_src());
		
		if(tipService.updateTipImg(hashMap) > 0) {
			return new ResponseEntity<String>("success", HttpStatus.OK);
		}
		
		return new ResponseEntity<String>("fail", HttpStatus.BAD_REQUEST);
	}
	
	@ApiOperation(value = "전체 팁 조회", response = List.class)
	@GetMapping("")
	private ResponseEntity<List<Tip>> selectListTip() {
		List<Tip> tipList = tipService.selectListTip();
		
		for (int i = 0; i < tipList.size(); i++) {
			Tip tip = tipList.get(i);
			tip.setComments(tipCommentService.selectTipComments(tip.getId()));
			tip.setScrapAccounts(tipService.selectListTipScrap(tip.getId()));
			tip.setLikeAccounts(tipService.selectTipLikeAccounts(tip.getId()));
		}
		return new ResponseEntity<List<Tip>>(tipList, HttpStatus.OK);
	}
	
	@ApiOperation(value = "팁 조회", response = Tip.class)
	@GetMapping("/{id}")
	private ResponseEntity<Tip> selectTip(@PathVariable("id") int id) {
		try {
			Tip tip = tipService.selectTip(id);
			tip.setComments(tipCommentService.selectTipComments(id));
			tip.setLikeAccounts(tipService.selectTipLikeAccounts(id));
			tip.setScrapAccounts(tipService.selectListTipScrap(tip.getId()));
			
			return new ResponseEntity<Tip>(tip, HttpStatus.OK);
		
		} catch (NullPointerException e) {
			e.printStackTrace();
			
			return new ResponseEntity<Tip>(HttpStatus.NOT_FOUND);
		}
	}
	
	@ApiOperation(value = "팁 제목/내용 수정", response = String.class)
	@PutMapping("/{id}")
	private ResponseEntity<String> updateTip(@PathVariable("id") int id, @RequestBody Tip tip) {
		if(tipService.updateTip(tip) > 0) {
			return new ResponseEntity<String>("success", HttpStatus.OK);
		}
		
		return new ResponseEntity<String>("fail", HttpStatus.BAD_REQUEST);
	}
	
	@ApiOperation(value = "팁 삭제", response = String.class)
	@DeleteMapping("/{id}")
	private ResponseEntity<String> deleteTip(@PathVariable("id") int id) {
		if(tipService.deleteTip(id) > 0) {
			return new ResponseEntity<String>("success", HttpStatus.OK);
		}
		
		return new ResponseEntity<String>("fail", HttpStatus.BAD_REQUEST);
	}
	
	@ApiOperation(value = "팁 좋아요 사용자 전체 조회", response = Boolean.class)
	@GetMapping("/{id}/like")
	private ResponseEntity<List<Integer>> checkTipLike(@PathVariable("id") int tip_id) {
		return new ResponseEntity<List<Integer>>(tipService.selectTipLikeAccounts(tip_id), HttpStatus.OK);
	}
	
	@ApiOperation(value = "팁 좋아요 여부 확인 후 좋아요 삭제/등록", response = String.class)
	@PostMapping("/{id}/like")
	private ResponseEntity<String> setTipLike(@PathVariable("id") int tip_id, @RequestParam("user_id") int user_id) {
		HashMap<String, Integer> hashMap = new HashMap<>();
		hashMap.put("tip_id", tip_id);
		hashMap.put("user_id", user_id);
		
		if(tipService.selectTipLike(hashMap) > 0) { // 사용자가 해당 글에 좋아요를 이미 누른 경우 - 좋아요 삭제
			if(tipService.deleteTipLike(hashMap) > 0) { 
				return new ResponseEntity<String>("success", HttpStatus.OK);
			}
			
			return new ResponseEntity<String>("fail", HttpStatus.INTERNAL_SERVER_ERROR);
		
		} else { // 사용자가 해당 글에 좋아요를 누르지 않은 경우 - 좋아요 등록
			if(tipService.insertTipLike(hashMap) > 0) {
				return new ResponseEntity<String>("success", HttpStatus.OK);
			}
			
			return new ResponseEntity<String>("fail", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@ApiOperation(value = "팁 스크랩 등록", response = String.class)
	@PostMapping("/{id}/scrap")
	private ResponseEntity<String> insertTipScrap(@PathVariable("id") int tip_id, @RequestParam("user_id") int user_id) {
		HashMap<String, Integer> hashMap = new HashMap<>();
		hashMap.put("tip_id", tip_id);
		hashMap.put("user_id", user_id);
		
		if(tipService.insertTipScrap(hashMap) > 0) {
			return new ResponseEntity<String>("success", HttpStatus.OK);
		}
		
		return new ResponseEntity<String>("fail", HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ApiOperation(value = "팁 Top5  조회", response = List.class)
	@GetMapping("/top5")
	private ResponseEntity<List<Tip>> selectListTipTop5() {
		List<Tip> tipList = tipService.selectListTipTop5();
		
		for (int i = 0; i < tipList.size(); i++) {
			Tip tip = tipList.get(i);
			tip.setScrapAccounts(tipService.selectListTipScrap(tip.getId()));
			tip.setLikeAccounts(tipService.selectTipLikeAccounts(tip.getId()));
		}
		return new ResponseEntity<List<Tip>>(tipList, HttpStatus.OK);
	}
	
}
