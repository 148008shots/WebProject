package com.hh.service;

import com.hh.pojo.Clubs;
import com.hh.pojo.UserClubs;

import java.util.List;

public interface ClubsService {
    List<Clubs> getAllClubs();
    void addClub(Clubs club);
    void deleteClubById(int clubId);
    void updateClub(Clubs club);

}


