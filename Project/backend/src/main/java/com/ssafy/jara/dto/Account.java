package com.ssafy.jara.dto;

import java.util.Date;
import java.util.List;

public class Account {
	private int id;				// 회원 number
	private String nickname;	// 별명
	private String email;		// 이메일
	private String password;	// 비밀번호
	private boolean sex;		// 성별
	private Date birthday; 		// 생일
	private String location;	// 주소
	private String bio;			// 소개글
	private boolean status;		// 회원가입 시 :0, 로그인 가능 : 1
	private String code;			// 6자리 인증번호
	
	private List<Integer> followerList;
	private List<Integer> followingList;
	
	private List<Article> myArticleList;	// 작성한 게시글 목록
	private List<Tip> scrapTipList;			// 스크랩한 팁 목록 
	
	private String PTY;			// 강수형태 - 없음(0), 비(1), 비/눈(2), 눈(3), 소나기(4), 빗방울(5), 빗방울/눈날림(6), 눈날림(7)
	private String SKY;			// 하늘상태 - 맑음(1), 구름많음(3), 흐림(4) 
	private String T1H;			// 기온
	
	private String img_src;		// 프로필 사진
	
	private String access_token;	// 네이버 access_token
	private String refresh_token;	// 네이버 refresh_token
	
	public Account() {}

	public Account(int id, String nickname, String email, String password, boolean sex, Date birthday, String location,
			String bio, boolean status, String code, List<Integer> followerList, List<Integer> followingList,
			List<Article> myArticleList, List<Tip> scrapTipList, String pTY, String sKY, String t1h, String img_src,
			String access_token, String refresh_token) {
		super();
		this.id = id;
		this.nickname = nickname;
		this.email = email;
		this.password = password;
		this.sex = sex;
		this.birthday = birthday;
		this.location = location;
		this.bio = bio;
		this.status = status;
		this.code = code;
		this.followerList = followerList;
		this.followingList = followingList;
		this.myArticleList = myArticleList;
		this.scrapTipList = scrapTipList;
		PTY = pTY;
		SKY = sKY;
		T1H = t1h;
		this.img_src = img_src;
		this.access_token = access_token;
		this.refresh_token = refresh_token;
	}

	public String getPTY() {
		return PTY;
	}

	public void setPTY(String PTY) {
		this.PTY = PTY;
	}

	public String getSKY() {
		return SKY;
	}

	public void setSKY(String SKY) {
		this.SKY = SKY;
	}

	public String getT1H() {
		return T1H;
	}

	public void setT1H(String T1H) {
		this.T1H = T1H;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public String getBio() {
		return bio;
	}

	public void setBio(String bio) {
		this.bio = bio;
	}

	public int getId() {
		return id;
	}

	public List<Integer> getFollowerList() {
		return followerList;
	}

	public void setFollowerList(List<Integer> followerList) {
		this.followerList = followerList;
	}

	public List<Integer> getFollowingList() {
		return followingList;
	}

	public void setFollowingList(List<Integer> followingList) {
		this.followingList = followingList;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isSex() {
		return sex;
	}

	public void setSex(boolean sex) {
		this.sex = sex;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}
	
	public List<Article> getMyArticleList() {
		return myArticleList;
	}

	public void setMyArticleList(List<Article> myArticleList) {
		this.myArticleList = myArticleList;
	}

	public List<Tip> getScrapTipList() {
		return scrapTipList;
	}

	public void setScrapTipList(List<Tip> scrapTipList) {
		this.scrapTipList = scrapTipList;
	}
	
	public String getImg_src() {
		return img_src;
	}

	public void setImg_src(String img_src) {
		this.img_src = img_src;
	}
	
	public String getAccess_token() {
		return access_token;
	}

	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}

	public String getRefresh_token() {
		return refresh_token;
	}

	public void setRefresh_token(String refresh_token) {
		this.refresh_token = refresh_token;
	}

	@Override
	public String toString() {
		return "Account [id=" + id + ", nickname=" + nickname + ", email=" + email + ", password=" + password + ", sex="
				+ sex + ", birthday=" + birthday + ", location=" + location + ", bio=" + bio + ", status=" + status
				+ ", code=" + code + ", followerList=" + followerList + ", followingList=" + followingList
				+ ", myArticleList=" + myArticleList + ", scrapTipList=" + scrapTipList + ", PTY=" + PTY + ", SKY="
				+ SKY + ", T1H=" + T1H + ", img_src=" + img_src + ", access_token=" + access_token + ", refresh_token="
				+ refresh_token + "]";
	}
	
}
