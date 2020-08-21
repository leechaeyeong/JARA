package com.ssafy.jara.dao;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.jara.dto.Account;
import com.ssafy.jara.dto.Follow;

@Mapper
public interface AccountDao {
	
	// 회원가입
	public int insertAccount(Account account);
	
	// 로그인 가능하도록 사용자 상태 변경
	public int changeStatus(String code);
	
	// 로그인
	public Account selectAccount(Account account);
	
	// 비밀번호 찾기 - 비밀번호 암호화
//	public String findPassword(String email);
	
	// 이메일, 닉네임 중복 확인
	public int duplicateCheck(Account account);
	
	// 사용자 인증 코드 변경
	public int changeCode(String email);
	
	// 인증 코드 찾기
	public String findCode(String email);
	
	// 비밀번호 변경하기
	public int changePassword(Account account);
	
	// id로 회원 조회
	public Account findPartAccount(int id);
	
	// email로 사용자가 있는지 조회
	public int findEmail(String email);
	
	// 모든 사용자 조회
	public List<Account> findAllAccount();
	
	// 회원 정보 수정
	public boolean updateAccount(Account account);
	
	// 회원 정보 삭제
	public int deleteAccount(int id);
	
	// 팔로우 요청 보내기
	public int insertFollow(Follow follow);
	
	// 팔로우 요청 승인하기
	public int approveFollow(Follow follow);
	
	// 팔로우 유뮤 조회
	public int findFollow(Follow follow);
	
	// 언팔로우
	public int deleteFollow(Follow follow);
	
	// follower 리스트 조회
	public List<Integer> findFollower(int following);
	
	// following 리스트 조회
	public List<Integer> findFollowing(int follower);
	
	// 이메일로 id 찾기
	public int findIdByEmail(String email);
	
	// 네이버로 로그인
	public int insertNaverAccount(Account account);
	
	// 네이버로 로그인시 주소 추가
	public int updateNaverAccount(HashMap<String, Object> hashMap);
	
	// 카카오로 로그인
	public int insertKakaoAccount(Account account);
	
	// 카카오로 로그인시 주소 추가
	public int updateKakaoAccount(HashMap<String, Object> hashMap);
}
