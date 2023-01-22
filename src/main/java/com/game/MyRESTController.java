package com.game;

import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.service.PlayerService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class MyRESTController {

    @Autowired
    private PlayerService playerService;

    @GetMapping("/rest/players")
    public List<Player> findAllPlayers(
            @RequestParam (required = false) String name,
            @RequestParam (required = false) String title,
            @RequestParam (required = false) Race race,
            @RequestParam (required = false) Profession profession,
            @RequestParam (required = false) Long after,
            @RequestParam (required = false) Long before,
            @RequestParam (required = false) Boolean banned,
            @RequestParam (required = false) Integer minExperience,
            @RequestParam (required = false) Integer maxExperience,
            @RequestParam (required = false) Integer minLevel,
            @RequestParam (required = false) Integer maxLevel,
            @RequestParam (required = false, defaultValue = "0") Integer pageNumber,
            @RequestParam (required = false, defaultValue = "3") Integer pageSize
            ){
        List<Player> playerlist = playerService.findAllPlayers(name, title, race, profession, after, before, banned,
                                                                minExperience, maxExperience, minLevel, maxLevel);
        return playerService.getPage(playerlist, pageNumber, pageSize);

    }
    @GetMapping("rest/players/{id}")
    public ResponseEntity getPlayer(@PathVariable Long id){

        if (id==0) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if (!(playerService.isPlayerExist(id))) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        Player player = playerService.getPlayer(id);
        return new ResponseEntity<>(player, HttpStatus.OK);
    }

    @GetMapping("rest/players/count")
    public Integer getPlayersCount(
            @RequestParam (required = false) String name,
            @RequestParam (required = false) String title,
            @RequestParam (required = false) Race race,
            @RequestParam (required = false) Profession profession,
            @RequestParam (required = false) Long after,
            @RequestParam (required = false) Long before,
            @RequestParam (required = false) Boolean banned,
            @RequestParam (required = false) Integer minExperience,
            @RequestParam (required = false) Integer maxExperience,
            @RequestParam (required = false) Integer minLevel,
            @RequestParam (required = false) Integer maxLevel
    ) {
        return playerService.getPlayersCount(name, title, race, profession, after, before, banned,
                                        minExperience, maxExperience, minLevel, maxLevel);
    }

    @PostMapping("rest/players")
    public ResponseEntity savePlayer(@Validated @RequestBody Player player) {
        if (playerService.isPlayerHasEmptyBody(player)) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if (!(playerService.isNameValid(player.getName())&&
                playerService.isTitleValid(player.getTitle())&&
                playerService.isExperienceValid(player.getExperience())&&
                playerService.isBirthdayValid(player.getBirthday()))) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        player.setLevel(playerService.calculatePlayerLevel(player));

        player.setUntilNextLevel(playerService.calculateUntilNextLevel(player));

        playerService.savePlayer(player);
        return new ResponseEntity<>(player, HttpStatus.OK);


    }

    @RequestMapping("rest/players/{id}")
    public ResponseEntity deletePlayer(@PathVariable Long id) {
        if (id==0) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if (!(playerService.isPlayerExist(id))) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        Player player = playerService.getPlayer(id);
        playerService.deletePlayer(player);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("rest/players/{id}")
    public ResponseEntity updatePlayer(@PathVariable Long id, @RequestBody Player player) {
        if (id==0) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if (!(playerService.isPlayerExist(id))) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        try {
            Player result = playerService.updatePlayer(id,player);
            return new ResponseEntity<>(result, HttpStatus.OK);
        }    catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
