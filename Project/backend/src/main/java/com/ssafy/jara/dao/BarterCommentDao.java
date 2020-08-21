package com.ssafy.jara.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.jara.dto.BarterComment;

@Mapper
public interface BarterCommentDao {
	// 물물교환 새로운 댓글 등록
	public int insertBarterComment(BarterComment barterComment);
	
	// 물물교환 댓글 수정 (contents)
	public int updateBarterComment(BarterComment barterComment);
	
	// 물물교환 댓글 삭제
	public int deleteBarterComment(int id);
	
	// 물물교환 전체 댓글 조회
	public List<BarterComment> selectListBarterComment(int item_id);
	
	// 물물교환 댓글 조회
	public BarterComment selectBarterComment(int id);
}
