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
	private int scraps;					// 스크랩 수
	private int commentCount;			// 댓글 수
	
	private List<TipComment> comments;	// 댓글 목록
	private List<Integer> likeAccounts;	// 좋아요 사용자 목록
	private List<Integer> scrapAccounts;// 스크랩 사용자 목록
	
	private String img_src;				// 파이어베이스 이미지 파일 경로 
	
	public Tip() {} 
	
	public Tip(int id, int writer, String title, String contents, int tag_id, Date created_at, Date updated_at,
			int hits, int likes, int scraps, int commentCount, List<TipComment> comments, List<Integer> likeAccounts,
			List<Integer> scrapAccounts, String img_src) {
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
		this.scraps = scraps;
		this.commentCount = commentCount;
		this.comments = comments;
		this.likeAccounts = likeAccounts;
		this.scrapAccounts = scrapAccounts;
		this.img_src = img_src;
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
	
	public List<Integer> getScrapAccounts() {
		return scrapAccounts;
	}

	public void setScrapAccounts(List<Integer> scrapAccounts) {
		this.scrapAccounts = scrapAccounts;
	}

	public String getImg_src() {
		return img_src;
	}

	public void setImg_src(String img_src) {
		this.img_src = img_src;
	}

	public int getScraps() {
		return scraps;
	}

	public void setScraps(int scraps) {
		this.scraps = scraps;
	}

	public int getCommentCount() {
		return commentCount;
	}

	public void setCommentCount(int commentCount) {
		this.commentCount = commentCount;
	}

	@Override
	public String toString() {
		return "Tip [id=" + id + ", writer=" + writer + ", title=" + title + ", contents=" + contents + ", tag_id="
				+ tag_id + ", created_at=" + created_at + ", updated_at=" + updated_at + ", hits=" + hits + ", likes="
				+ likes + ", scraps=" + scraps + ", commentCount=" + commentCount + ", comments=" + comments
				+ ", likeAccounts=" + likeAccounts + ", scrapAccounts=" + scrapAccounts + ", img_src=" + img_src + "]";
	}

}
