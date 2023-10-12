package com.sportfy.controller;

import com.sportfy.model.Match;
import com.sportfy.service.MatchService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class MatchController {

    @Autowired
    private MatchService matchService;

    @PostMapping("/match")
    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    public Match createMatch(@RequestBody Match match) {
        // Optional<Match> existingMatch = matchService.findById(match.getId());
        //if (existingMatch.isPresent()) {
        //     throw new com.sportfy.controller.ConflictException("A match with this ID already exists.");
        //  } else {
        return matchService.save(match);

        //  }

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
    public Match updateProduct(@PathVariable(value = "id") Long id,
                               @RequestBody Match match) {
        Optional<Match> matchOptional = matchService.findById(id);
        if (!matchOptional.isPresent()) {
            throw new ConflictException("Match not found.");
        }
        Match matchToUpdate = matchOptional.get();

        BeanUtils.copyProperties(match, matchToUpdate);
        return matchService.save(matchToUpdate);
    }
}
