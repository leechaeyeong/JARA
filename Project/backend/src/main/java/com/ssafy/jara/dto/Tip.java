package com.ssafy.jara.dto;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

public class Tip {

	private int id;						// 번호	tip pk
	private int writer;					// 작성자	account fk
	private String title;				// 제목
	private String contents;			// 내용
	private int tag_id;					// 태그	tag fk
	
	@JsonFormat(pattern="yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
	private Date created_at;			// 작성일
	
	@JsonFormat(pattern="yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
	private Date updated_at;			// 수정일
	private int hits;					// 조회수
	private int likes;					// 좋아요 수 
	
	private List<TipComment> comments;	// 댓글 목록
	private List<Integer> likeAccounts;	// 좋아요 사용자 목록
	
	private String stored_file_name;	// 저장된 이미지 파일 이름
	
	public Tip() {} 
	
	public Tip(int id, int writer, String title, String contents, int tag_id, Date created_at, Date updated_at, int hits, int likes, String stored_file_name) {
		super();
		this.id = id;
		this.writer = writer;
		this.title = title;
		this.contents = contents;
		this.tag_id = tag_id;
		this.created_at = created_at;
		this.updated_at = updated_at;
		this.hits = hits;
		this.likes = likes;
		this.stored_file_name = stored_file_name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getWriter() {
		return writer;
	}

	public void setWriter(int writer) {
		this.writer = writer;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContents() {
		return contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}

	public int getTag_id() {
		return tag_id;
	}

	public void setTag_id(int tag_id) {
		this.tag_id = tag_id;
	}

	public Date getCreated_at() {
		return created_at;
	}

	public void setCreated_at(Date created_at) {
		this.created_at = created_at;
	}

	public Date getUpdated_at() {
		return updated_at;
	}

	public void setUpdated_at(Date updated_at) {
		this.updated_at = updated_at;
	}

	public int getHits() {
		return hits;
	}

	public void setHits(int hits) {
		this.hits = hits;
	}

	public int getLikes() {
		return likes;
	}

	public void setLikes(int likes) {
		this.likes = likes;
	}
	
	public List<TipComment> getComments() {
		return comments;
	}

	public void setComments(List<TipComment> comments) {
		this.comments = comments;
	}

	public List<Integer> getLikeAccounts() {
		return likeAccounts;
	}

	public void setLikeAccounts(List<Integer> likeAccounts) {
		this.likeAccounts = likeAccounts;
	}
	
	public String getStored_file_name() {
		return stored_file_name;
	}

	public void setStored_file_name(String stored_file_name) {
		this.stored_file_name = stored_file_name;
	}

	@Override
	public String toString() {
		return "Tip [id=" + id + ", writer=" + writer + ", title=" + title + ", contents=" + contents + ", tag_id="
				+ tag_id + ", created_at=" + created_at + ", updated_at=" + updated_at + ", hits=" + hits + ", likes="
				+ likes + ", comments=" + comments + ", likeAccounts=" + likeAccounts + ", stored_file_name="
				+ stored_file_name + "]";
	}
	
}
