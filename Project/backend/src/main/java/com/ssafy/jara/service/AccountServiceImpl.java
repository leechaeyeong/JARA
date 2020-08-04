package com.ssafy.jara.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssafy.jara.dao.AccountDao;
import com.ssafy.jara.dto.Account;
import com.ssafy.jara.dto.Follow;
import com.ssafy.jara.dto.Location;

@Service
public class AccountServiceImpl implements AccountService {

	@Autowired
	AccountDao accountDao;
	
	@Override
	public int insertAccount(Account account) {
		return accountDao.insertAccount(account);
	}
	
	@Override
	public int changeStatus(int code) {
		return accountDao.changeStatus(code);
	}

	@Override
	public Account selectAccount(Account account) {
		System.out.println("SERVICE : " + account.getEmail() + " " + account.getPassword());
		return accountDao.selectAccount(account);
	}

	@Override
	public int duplicateCheck(Account account) {
		return accountDao.duplicateCheck(account);
	}
	
	@Override
	public int changeCode(String email) {
		return accountDao.changeCode(email);
	}
	
	@Override
	public int findCode(String email) {
		return accountDao.findCode(email);
	}
	
	@Override
	public int changePassword(Account account) {
		return accountDao.changePassword(account);
	}

	@Override
	public Account findAccount(int id) {
		return accountDao.findAccount(id);
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
	public int insertFollow(Follow follow) {
		System.out.println("팔로우 상태 : "+follow.isStatus());
		return accountDao.insertFollow(follow);
	}
	
	@Override
	public int approveFollow(Follow follow) {
		System.out.println("팔로우 상태 : "+follow.isStatus());
		return accountDao.approveFollow(follow);
	}

	@Override
	public List<Follow> findAllFollow() {
		return accountDao.findAllFollow();
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
	public double findX(String name) {
		return accountDao.findX(name);
	}

	@Override
	public double findY(String name) {
		return accountDao.findY(name);
	}
	
	@Override
	public Location findLocation(String name) {
		return accountDao.findLocation(name);
	}

	@Override
	public List<Location> findAllLocation() {
		return accountDao.findAllLocation();
	}


	
}
