package com.game.service;

import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;

public interface PlayerService {
    List<Player> findAllPlayers(String name, String title, Race race, Profession profession,
                                       Long after, Long before, Boolean banned, Integer minExperience,
                                       Integer maxExperience, Integer minLevel, Integer maxLevel);
    List<Player> getPage(List<Player> list, Integer pageNumber, Integer pageSize);

    Player getPlayer(Long id);

    boolean isPlayerExist(Long id);

    Integer getPlayersCount(String name, String title, Race race, Profession profession,
                            Long after, Long before, Boolean banned, Integer minExperience,
                            Integer maxExperience, Integer minLevel, Integer maxLevel);

    public void savePlayer(Player player);

    public void deletePlayer(Player player);

    public boolean isNameValid(String nameValue);

    public boolean isTitleValid(String titleValue);

    public boolean isExperienceValid(Integer experienceValue);

    public boolean isBirthdayValid(Date birthday);

    public boolean isPlayerHasEmptyBody(Player player);

    public Integer calculatePlayerLevel(Player player);

    public Integer calculateUntilNextLevel(Player player);

    public Player updatePlayer(Long id, Player newPlayer);

}
