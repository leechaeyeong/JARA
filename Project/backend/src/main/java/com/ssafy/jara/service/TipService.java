package com.ssafy.jara.service;

import java.util.HashMap;
import java.util.List;

import com.ssafy.jara.dto.Tip;

public interface TipService {

	// 팁 등록
	public int insertTip(Tip tip);

	// 전체 팁 조회
	public List<Tip> selectListTip();

	// 팁 조회
	public Tip selectTip(int id);

	// 팁 수정
	public int updateTip(Tip tip);

	// 팁 삭제
	public int deleteTip(int id);

	// 팁 좋아요 등록
	public int insertTipLike(HashMap<String, Integer> hashMap);

	// 팁 좋아요 하는 사용자 전체 조회
	public List<Integer> selectTipLikeAccounts(int tip_id);

	// 사용자의 해당 팁 좋아요 여부 확인
	public int selectTipLike(HashMap<String, Integer> hashMap);

	// 팁 좋아요 삭제
	public int deleteTipLike(HashMap<String, Integer> hashMap);

	// 팁 스크랩 등록
	public int insertTipScrap(HashMap<String, Integer> hashMap);

	// 사용자가 스크랩한 전체 팁 조회
	public List<Tip> selectListTipUserScrap(int user_id);

	// (좋아요수+조회수) 순 > 좋아요순 > 최신순 정렬 상위 5개 조회
	public List<Tip> selectListTipTop5();

	// 팁 이미지 경로(firebase) 저장
	public int updateTipImg(HashMap<String, Object> hashMap);

	// 팁 스크랩 사용자 전체 조회
	public List<Integer> selectListTipScrap(int tip_id);
}
