package com.hh.service.impl;

import com.hh.mapper.ClubsMapper;
import com.hh.pojo.Clubs;
import com.hh.pojo.UserClubs;
import com.hh.service.ClubsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClubsServiceImpl implements ClubsService {
    private final ClubsMapper clubsMapper;

    @Autowired
    public ClubsServiceImpl(ClubsMapper clubsMapper) {
        this.clubsMapper = clubsMapper;
    }

    @Override
    public List<Clubs> getAllClubs() {
        return clubsMapper.selectAllClubs();
    }

    @Override
    public void addClub(Clubs club) {
        clubsMapper.insertClub(club);
    }

    @Override
    public void deleteClubById(int clubId) {
        clubsMapper.deleteClubById(clubId);
    }

    @Override
    public void updateClub(Clubs club) {
        clubsMapper.updateClubById(club);
    }

    @Override
    public void joinClub(Integer userId, Integer clubId) {

        clubsMapper.joinClub(userId,clubId);
        Integer nweMember = 1;
        clubsMapper.updateClubMember(nweMember,clubId);
    }

    @Override
    public List<UserClubs> selectClubByUserId(int userId) {
        return clubsMapper.selectClubByUserId(userId);
    }
    @Override
    public List<Clubs> selectClubByUserId1(int userId) {
        return clubsMapper.selectClubByUserId1(userId);
    }
}
