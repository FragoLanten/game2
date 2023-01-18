package com.game.service;


import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class PlayerServiceImpl implements PlayerService {

    @Autowired
    private PlayerRepository playerRepository;

    public List<Player> findAllPlayers(String name, String title, Race race, Profession profession,
                                       Long after, Long before, Boolean banned, Integer minExperience,
                                       Integer maxExperience, Integer minLevel, Integer maxLevel) {
        final Date afterDate = after == null ? null : new Date(after);
        final Date beforeDate = before == null ? null : new Date(before);
        List<Player> listOfPlayers = new ArrayList<>();
        playerRepository.findAll().forEach((player) -> {
            if (name != null && !player.getName().contains(name)) return;
            if (title != null && !player.getTitle().contains(title)) return;
            if (race != null && !player.getRace().equals(race)) return;
            if (profession != null && !player.getProfession().equals(profession)) return;
            if (after != null && (player.getBirthday().before(afterDate))) return;
            if (before != null && (player.getBirthday().after(beforeDate))) return;
            if (banned != null && !(player.isBanned() == banned)) return;
            if (minExperience != null && (player.getExperience().compareTo(minExperience) < 0)) return;
            if (maxExperience != null && (player.getExperience().compareTo(maxExperience) > 0)) return;
            if (minLevel != null && (player.getLevel().compareTo(minLevel) < 0)) return;
            if (maxLevel != null && (player.getLevel().compareTo(maxLevel) > 0)) return;
            listOfPlayers.add(player);
        });

        return listOfPlayers;
    }

    public List<Player> getPage(List<Player> list, Integer pageNumber, Integer pageSize) {
        int from = pageNumber * pageSize;
        int to = from + pageSize;
        if (to > list.size()) to = list.size();
        return list.subList(from, to);
    }

    public Player getPlayer(Long id) {
        return playerRepository.findById(id).get();
    }

}




