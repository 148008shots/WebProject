package com.hh.service.impl;

import com.hh.mapper.UserClubsMapper;
import com.hh.pojo.Clubs;
import com.hh.pojo.UserClubs;
import com.hh.service.UserClubsService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserClubsServiceImpl implements UserClubsService {

    private final UserClubsMapper userClubsMapper;


    public UserClubsServiceImpl(UserClubsMapper userClubsMapper) {
        this.userClubsMapper = userClubsMapper;
    }

    @Override
    public void joinClub(Integer userId, Integer clubId) {
        userClubsMapper.joinClub(userId, clubId);
    }

    @Override
    public List<UserClubs> selectUserClubsByUserIdAndClubId(Integer userId, Integer clubId) {
        return userClubsMapper.selectUserClubsByUserIdAndClubId(userId, clubId);
    }

    @Override
    public int increaseClubMember(Integer clubId) {
        Integer nweMember = 1;
        return userClubsMapper.updateClubMember(nweMember, clubId);
    }

    @Override
    public int decreaseClubMember(Integer clubId) {
        Integer nweMember = 1;
        return userClubsMapper.updateClubMember(-nweMember, clubId);
    }

    @Override
    public void leaveClub(Integer userId, Integer clubId) {
        userClubsMapper.leaveClub(userId, clubId);
    }

    @Override
    public List<UserClubs> selectClubByUserId(int userId) {
        return userClubsMapper.selectClubByUserId(userId);
    }

    @Override
    public List<Clubs> selectClubByUserId1(int userId) {
        return userClubsMapper.selectClubByUserId1(userId);
    }
}
