package com.ssafy.jara.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssafy.jara.dao.AccountDao;
import com.ssafy.jara.dto.Account;
import com.ssafy.jara.dto.Follow;

@Service
public class AccountServiceImpl implements AccountService {

	@Autowired
	AccountDao accountDao;
	
	@Override
	public int insertAccount(Account account) {
		return accountDao.insertAccount(account);
	}
	
	@Override
	public int changeStatus(String code) {
		return accountDao.changeStatus(code);
	}

	@Override
	public Account selectAccount(Account account) {
		return accountDao.selectAccount(account);
	}
	
//	@Override
//	public String findPassword(String email) {
//		return accountDao.findPassword(email);
//	}

	@Override
	public int duplicateCheck(Account account) {
		return accountDao.duplicateCheck(account);
	}
	
	@Override
	public int changeCode(String email) {
		return accountDao.changeCode(email);
	}
	
	@Override
	public String findCode(String email) {
		return accountDao.findCode(email);
	}
	
	@Override
	public int changePassword(Account account) {
		return accountDao.changePassword(account);
	}

	@Override
	public Account findPartAccount(int id) {
		return accountDao.findPartAccount(id);
	}
	
	@Override
	public int findEmail(String email) {
		return accountDao.findEmail(email);
	}
	
	@Override
	public List<Account> findAllAccount() {
		return accountDao.findAllAccount();
	}

	@Override
	public boolean updateAccount(Account account) {
		return accountDao.updateAccount(account);
	}
	
	@Override
	public int deleteAccount(int id) {
		return accountDao.deleteAccount(id);
	}
	
	@Override
	public int insertFollow(Follow follow) {
		return accountDao.insertFollow(follow);
	}
	
	@Override
	public int approveFollow(Follow follow) {
		return accountDao.approveFollow(follow);
	}

	@Override
	public int findFollow(Follow follow) {
		return accountDao.findFollow(follow);
	}

	@Override
	public int deleteFollow(Follow follow) {
		return accountDao.deleteFollow(follow);
	}

	@Override
	public List<Integer> findFollower(int following) {
		return accountDao.findFollower(following);
	}

	@Override
	public List<Integer> findFollowing(int follower) {
		return accountDao.findFollowing(follower);
	}

	@Override
	public int findIdByEmail(String email) {
		return accountDao.findIdByEmail(email);
	}

	@Override
	public int insertNaverAccount(Account account) {
		return accountDao.insertNaverAccount(account);
	}

	@Override
	public int updateNaverAccount(HashMap<String, Object> hashMap) {
		return accountDao.updateNaverAccount(hashMap);
	}
	
	@Override
	public int insertKakaoAccount(Account account) {
		return accountDao.insertKakaoAccount(account);
	}

	@Override
	public int updateKakaoAccount(HashMap<String, Object> hashMap) {
		return accountDao.updateKakaoAccount(hashMap);
	}
	
}
