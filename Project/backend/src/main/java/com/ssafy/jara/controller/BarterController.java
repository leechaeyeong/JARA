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
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.jara.dto.Barter;
import com.ssafy.jara.service.BarterService;

import io.swagger.annotations.ApiOperation;

@CrossOrigin(origins = { "*" }, maxAge = 6000)
@RestController
@RequestMapping("/jara/barters")
public class BarterController {
	
	@Autowired
	BarterService barterService;
	
	@ApiOperation(value = "새로운 물물교환 등록", response = String.class)
	@PostMapping("")
	private ResponseEntity<Barter> insertBarter(@RequestBody Barter barter) {
		int ret = barterService.insertBarter(barter);
		if (ret > 0) {
			int barterId = barter.getId();
			Barter newBarter = barterService.selectBarter(barterId);
			return new ResponseEntity<Barter>(newBarter, HttpStatus.OK);
		} else {
			return new ResponseEntity<Barter>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@ApiOperation(value = "해당 물물교환 조회", response = String.class)
	@GetMapping("/{id}")
	private ResponseEntity<Barter> selectBarter(@PathVariable int id) {
		Barter barter = barterService.selectBarter(id);
		
		if (barter == null) {
			return new ResponseEntity<Barter>(HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<Barter>(barter, HttpStatus.OK);
	}
	
	@ApiOperation(value = "해당 물물교환 수정 (title, price, contents, status)", response = String.class)
	@PutMapping("/{id}")
	private ResponseEntity<Barter> updateBarter(@PathVariable int id, @RequestBody Barter barter) {
		Barter originalBarter = barterService.selectBarter(id);
		
		HashMap<String, Object> hashMap = new HashMap<String, Object>();
		hashMap.put("id", id);
		hashMap.put("status", barter.getStatus());
		hashMap.put("img_src", barter.getImg_src());
		hashMap.put("original_updated_at", originalBarter.getUpdated_at());
		
		int ret = -1;
		
		if (barter.getStatus()) {	// status(상태)를 false(거래중)에서 true(거래완료)로 변경
			ret = barterService.updateBarterStatus(hashMap);
		} else {
			if (originalBarter.getImg_src() == null) {	// img_src만 변경
				if (barter.getImg_src() != null) {
					ret = barterService.updateBarterImgSrc(hashMap);
				} else {
					ret = barterService.updateBarter(barter);
				}
			} else {
				if (originalBarter.getImg_src().equals(barter.getImg_src())) {	// 게시글 변경
					ret = barterService.updateBarter(barter);
				} else {	// img_src만 변경
					ret = barterService.updateBarterImgSrc(hashMap);
				}
			}
		}
		
		if (ret > 0) {
			Barter updatedBarter = barterService.selectBarter(id);
			return new ResponseEntity<Barter>(updatedBarter, HttpStatus.OK);
		} else {
			return new ResponseEntity<Barter>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@ApiOperation(value = "물물교환 삭제", response = String.class)
	@DeleteMapping("/{id}")
	private ResponseEntity<String> deleteBarter(@PathVariable int id) {
		if (barterService.deleteBarter(id) > 0) {
			return new ResponseEntity<String>("success", HttpStatus.OK);
		} else {
			return new ResponseEntity<String>("fail", HttpStatus.BAD_REQUEST);
		}
	}
	
	@ApiOperation(value = "전체 물물교환 조회", response = String.class)
	@GetMapping("")
	private ResponseEntity<List<Barter>> selectListBarter() {
		return new ResponseEntity<List<Barter>>(barterService.selectListBarter(), HttpStatus.OK);
	}
	
	@ApiOperation(value = "물물교환 게시글 이미지 경로 저장", response = String.class)
	@PutMapping("/{id}/img")
	private ResponseEntity<String> updateBarterImg(@PathVariable int id, @RequestBody Barter barter) {
		Barter originalBarter = barterService.selectBarter(id);
		HashMap<String, Object> hashMap = new HashMap<String, Object>();
		hashMap.put("id", id);
		hashMap.put("img_src", barter.getImg_src());
		hashMap.put("original_updated_at", originalBarter.getUpdated_at());
		
		if (barterService.updateBarterImgSrc(hashMap) > 0) {
			return new ResponseEntity<String>(barter.getImg_src(), HttpStatus.OK);
		} else {
			return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
		}
	}
}
