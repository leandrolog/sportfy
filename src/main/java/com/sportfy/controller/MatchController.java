package com.sportfy.controller;

import com.sportfy.dto.MatchDto;
import com.sportfy.model.Match;
import com.sportfy.service.MatchService;
import com.sportfy.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    @Operation(description = "Create a new match")

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Match created successfully"),
            @ApiResponse(responseCode = "400", description = "Error creating a match")
    })
    public Match createMatch(@RequestBody MatchDto matchDto) {
        return matchService.save(matchDto);
    }

    @PostMapping("/match/{id}/addPlayer/{userId}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Player added successfully"),
            @ApiResponse(responseCode = "400", description = "Error adding player to the match")
    })
    public void matchAddPlayers(@PathVariable("id") Long id, @PathVariable("userId") Long userId) {
        try {
            //  User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            matchService.addPlayer(userId, id);
        } catch (ConflictException e) {
            throw new ConflictException("Conflict occurred");
        }
    }

    @PostMapping("/match/{matchId}/removePlayer/{userId}")
    @Operation(description = "remove a player")

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Player removed successfully"),
            @ApiResponse(responseCode = "400", description = "Error removing player from the match")
    })
    public Match matchRemovePlayer(@PathVariable("matchId") Long matchId, @PathVariable("userId") Long userId) {
        try {
            return matchService.removePlayer(matchId, userId);
        } catch (ConflictException e) {
            throw new ConflictException("Conflict occurred");
        }
    }

    @GetMapping("/match")
    // @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @Operation(description = "get all matchs")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Return all matches"),
            @ApiResponse(responseCode = "400", description = "Error finding all matches")
    })
    public List<Match> getAllMatch(@SortDefault(sort = "id", direction = Sort.Direction.ASC) Sort sort) {
        return matchService.findAll(sort);
    }

    @GetMapping("/match/{id}")
    // @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @Operation(description = "find a match")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Return one match"),
            @ApiResponse(responseCode = "400", description = "Error finding the match")
    })
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
    @Operation(description = "delete match")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Match deleted successfully"),
            @ApiResponse(responseCode = "400", description = "Error deleting the match")
    })
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
    @Operation(description = "update match")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Match updated successfully"),
            @ApiResponse(responseCode = "400", description = "Error updating the match")
    })
    public Match updateMatch(@PathVariable(value = "id") Long id, @RequestBody MatchDto matchDto) {
        return matchService.update(matchDto, id);
    }
}
