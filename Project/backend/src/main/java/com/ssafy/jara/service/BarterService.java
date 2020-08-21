package com.ssafy.jara.service;

import java.util.HashMap;
import java.util.List;

import com.ssafy.jara.dto.Barter;

public interface BarterService {
	// 새로운 물물교환 등록
	public int insertBarter(Barter barter);
	
	// 해당 물물교환 조회
	public Barter selectBarter(int id);
	
	// 해당 물물교환 수정
	public int updateBarter(Barter barter);
	
	// 해당 물물교환 삭제
	public int deleteBarter(int id);
	
	// 전체 물물교환 조회
	public List<Barter> selectListBarter();
	
	// 물물교환 상태 수정 (false(거래중)에서 true(거래완료)로 변경)
	public int updateBarterStatus(HashMap<String, Object> hashMap);
	
	// 물물교환 게시글 이미지 경로 저장
	public int updateBarterImgSrc(HashMap<String, Object> hashMap);
}
