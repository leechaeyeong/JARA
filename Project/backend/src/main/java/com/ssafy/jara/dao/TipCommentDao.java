package com.ssafy.jara.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.jara.dto.TipComment;

@Mapper
public interface TipCommentDao {

	// 팁 댓글 등록
	public int insertTipComment(TipComment tipComment);

	// 팁 전체 댓글 조회
	public List<TipComment> selectTipComments(int tip_id);

	// 팁 댓글 조회
	public TipComment selectTipComment(int id);

	// 팁 댓글 수정
	public int updateTipComment(TipComment tipComment);

	// 팁 댓글 삭제
	public int deleteTipComment(int id);

}
