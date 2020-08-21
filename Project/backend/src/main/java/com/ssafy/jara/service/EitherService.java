package com.ssafy.jara.service;

import java.util.List;

import com.ssafy.jara.dto.Either;
import com.ssafy.jara.dto.EitherChoice;

public interface EitherService {
	// 새로운 투표 등록
	public int insertEither(Either either);

	// 해당 투표 조회
	public Either selectEither(int id);

	// 해당 투표 수정
	public int updateEither(int id);

	// 해당 투표 삭제
	public int deleteEither(int id);

	// 전체 투표 조회
	public List<Either> selectListEither();

	// 투표 리스트에서 인덱스 {s_idx}번 부터 {count}개의 투표 조회
	public List<Either> selectPartialListEither(int s_idx, int count);
	
	// 선택지 투표
	public int pickEither(EitherChoice eitherChoice);

	// 해당하는 투표의 A선택지를 선택한 user_id 리스트 조회
	public List<Integer> selectChoiceAList(int either_id);

	// 해당하는 투표의 B선택지를 선택한 user_id 리스트 조회
	public List<Integer> selectChoiceBList(int either_id);

	// 투표순 정렬 상위 5개
	public List<Either> selectListEitherTop3();
}
