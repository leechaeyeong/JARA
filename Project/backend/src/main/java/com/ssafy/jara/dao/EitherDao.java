package com.ssafy.jara.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.jara.dto.Either;
import com.ssafy.jara.dto.EitherChoice;

@Mapper
public interface EitherDao {
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

	// 선택지 투표
	public int pickEither(EitherChoice eitherChoice);

	// 해당하는 투표의 A선택지를 선택한 user_id 리스트 조회
	public List<Integer> selectChoiceAList(int either_id);

	// 해당하는 투표의 B선택지를 선택한 user_id 리스트 조회
	public List<Integer> selectChoiceBList(int either_id);

	// 투표순 정렬 상위 5개
	public List<Either> selectListEitherTop3();
}
