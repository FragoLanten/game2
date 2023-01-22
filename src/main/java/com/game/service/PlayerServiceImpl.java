package com.game.service;


import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.repository.PlayerRepository;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;

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

    public boolean isPlayerExist(Long id) {
        return playerRepository.existsById(id);
    }

    @Override
    public Integer getPlayersCount(String name, String title, Race race, Profession profession,
                                   Long after, Long before, Boolean banned, Integer minExperience,
                                   Integer maxExperience, Integer minLevel, Integer maxLevel) {
        final Date afterDate = after == null ? null : new Date(after);
        final Date beforeDate = before == null ? null : new Date(before);
        Integer countOfSortedPlayers=0;
        List<Player> listOfPlayers = new ArrayList<>();
        playerRepository.findAll().forEach((player)->{
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
        for (int i = 0; i < listOfPlayers.size(); i++) {
            countOfSortedPlayers++;
        }
        return countOfSortedPlayers;
    }

    public void savePlayer(Player player) {
        playerRepository.save(player);
    }

    public void deletePlayer(Player player) {
        playerRepository.delete(player);
    }

    public Player updatePlayer(Long id, Player newPlayer) {
        Player oldPlayer = playerRepository.findById(id).get();
//        Long newId = newPlayer.getId();
//        if (newId!=null) {
//            oldPlayer.setId(newId);
//        }

        String newName = newPlayer.getName();
        if (newName!=null) {
            oldPlayer.setName(newName);
        }
        String newTitle = newPlayer.getTitle();
        if (newTitle!=null) {
            oldPlayer.setTitle(newTitle);
        }
        Race newRace = newPlayer.getRace();
        if (newRace!=null) {
            oldPlayer.setRace(newRace);
        }
        Profession newProfession = newPlayer.getProfession();
        if (newProfession!=null) {
            oldPlayer.setProfession(newProfession);
        }
        Date newBirthday = newPlayer.getBirthday();
        if (newBirthday!=null) {
            if (isBirthdayValid(newBirthday)) {
                oldPlayer.setBirthday(newBirthday);
            }
            else throw new IllegalArgumentException();
        }

        if (newPlayer.isBanned() != null) {
            oldPlayer.setBanned(newPlayer.isBanned());
        }

        Integer newExperience = newPlayer.getExperience();
        if (newExperience!=null) {
            if (isExperienceValid(newExperience)) {
                oldPlayer.setExperience(newExperience);
                oldPlayer.setLevel(calculatePlayerLevel(oldPlayer));
                oldPlayer.setUntilNextLevel(calculateUntilNextLevel(oldPlayer));
            }
            else throw new IllegalArgumentException();
        }
//        Integer newLevel = newPlayer.getLevel();
//        if (newLevel!=null) {
//            oldPlayer.setExperience(newExperience);
//            oldPlayer.setLevel(calculatePlayerLevel(oldPlayer));
//            oldPlayer.setUntilNextLevel(calculateUntilNextLevel(oldPlayer));
//        }

//        if (needToUpdateLevel) {
//            oldPlayer.setLevel(calculatePlayerLevel(newPlayer));
//            oldPlayer.setUntilNextLevel(calculateUntilNextLevel(newPlayer));
//        }


        return playerRepository.save(oldPlayer);
    }




    public boolean isNameValid(String nameValue) {
        return ((!nameValue.isEmpty()) && (nameValue.length() <= 12));
    }

    public boolean isTitleValid(String titleValue) {
        return titleValue.length() <= 30;
    }

    public boolean isExperienceValid(Integer experienceValue) {
        return experienceValue<=10000000&&experienceValue>=0;
    }

    public boolean isBirthdayValid(Date birthday) {
        return birthday.getTime()>=0;
    }

    public boolean isPlayerHasEmptyBody(Player player) {
        if ((player.getName()==null)||(player.getTitle()==null)
                ||(player.getRace()==null)||(player.getProfession()==null)
                ||(player.getBirthday()==null)||(player.getExperience()==null))
            return true;
        else return false;
    }

    public Integer calculatePlayerLevel(Player player) {
        Integer experience = player.getExperience();
        return (int) ((Math.sqrt(2500+(200*experience))-50)/100);
    }

    public Integer calculateUntilNextLevel(Player player) {
        Integer experience = player.getExperience();
        return (50*(player.getLevel()+1)*(player.getLevel()+2) - experience);
    }

}




