package com.hh.service;

import com.hh.pojo.Clubs;
import com.hh.pojo.UserClubs;

import java.util.List;

public interface UserClubsService {
    void joinClub(Integer userId, Integer clubId);

    List<UserClubs> selectUserClubsByUserIdAndClubId(Integer userId, Integer clubId);

    int increaseClubMember(Integer clubId);

    int decreaseClubMember(Integer clubId);

    void leaveClub(Integer userId, Integer clubId);

    List<UserClubs> selectClubByUserId(int userId);

    List<Clubs> selectClubByUserId1(int userId);
}
