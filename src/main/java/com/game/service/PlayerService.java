package com.game.service;

import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PlayerService {
    List<Player> findAllPlayers(String name, String title, Race race, Profession profession,
                                       Long after, Long before, Boolean banned, Integer minExperience,
                                       Integer maxExperience, Integer minLevel, Integer maxLevel);
    List<Player> getPage(List<Player> list, Integer pageNumber, Integer pageSize);

    public Player getPlayer(Long id);
}
