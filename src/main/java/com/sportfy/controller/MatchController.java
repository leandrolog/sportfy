package com.sportfy.controller;

import com.sportfy.dto.MatchDto;
import com.sportfy.dto.UserDto;
import com.sportfy.model.Match;
import com.sportfy.model.User;
import com.sportfy.service.MatchService;
import com.sportfy.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class MatchController {

    @Autowired
    private MatchService matchService;
    @Autowired
    private UserService userService;

    @PostMapping("/match")
    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    public Match createMatch(@RequestBody MatchDto matchDto) {
        return matchService.save(matchDto);
    }

    @PostMapping("/match/{id}/addPlayer/{userId}")
    public void matchAddPlayers(@PathVariable("id") Long id, @PathVariable ("userId") Long userId) {
        try {
          //  User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            matchService.addPlayer(userId, id);
        } catch (ConflictException e) {
            throw new ConflictException("Conflict occurred");
        }
    }

    @PostMapping("/match/{matchId}/removePlayer/{userId}")
    public Match matchRemovePlayer(@PathVariable("matchId") Long matchId, @PathVariable("userId") Long userId) {
        try {
            return matchService.removePlayer(matchId, userId);
        } catch (ConflictException e) {
            throw new ConflictException("Conflict occurred");
        }
    }

    @GetMapping("/match")
    // @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public List<Match> getAllMatch(@SortDefault(sort = "id", direction = Sort.Direction.ASC) Sort sort) {
        return matchService.findAll(sort);
    }

    @GetMapping("/match/{id}")
    // @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public Match getMatch(@PathVariable(value = "id") Long id) {
        Optional<Match> matchOptional = matchService.findById(id);
        if (matchOptional.isEmpty()) {
            throw new ConflictException("No match with this ID was found. Please provide a valid match ID.");
        } else {
            return matchOptional.get();
        }
    }

    @DeleteMapping("/match/{id}")
    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Object> deleteMatch(@PathVariable(value = "id") Long id) {
        Optional<Match> matchOptional = matchService.findById(id);
        if (!matchOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No match was found with the provided ID.");
        }
        matchService.delete(matchOptional.get());
        return ResponseEntity.status(HttpStatus.OK).body("The match has been successfully deleted.");
    }

    @PutMapping("/match/{id}")
    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    public Match updateProduct(@PathVariable(value = "id") Long id,@RequestBody MatchDto matchDto) {
        return matchService.update(matchDto, id);
    }
}

