package com.game;

import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
        Player player = playerService.getPlayer(id);
        return new ResponseEntity<>(player, HttpStatus.OK);

    }



}
