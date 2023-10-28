package com.sportfy.service;

import com.sportfy.controller.ConflictException;
import com.sportfy.dto.MatchDto;
import com.sportfy.model.Match;
import com.sportfy.model.User;
import com.sportfy.repository.MatchRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MatchService {

    @Autowired
    private MatchRepository matchRepository;
    @Autowired
    private UserService userService;

    @Transactional
    public Match save(Match match) {
        return matchRepository.save(match);
    }

    @Transactional
    public Match update(MatchDto matchDto, Long matchId) {
        Optional<Match> matchOptional = findById(matchId);
        if (!matchOptional.isPresent()) {
            throw new ConflictException("Match not found.");
        }
        Match matchToUpdate = matchOptional.get();
        BeanUtils.copyProperties(matchDto, matchToUpdate);
        return matchRepository.save(matchToUpdate);
    }

    public List<Match> findAll(Sort sort) {
        return matchRepository.findAll(sort);
    }

    public Optional<Match> findById(Long id) {
        return matchRepository.findById(id);
    }

    @Transactional
    public void delete(Match match) {
        matchRepository.delete(match);
    }

    public void addPlayer(User user, Long id) {
        Optional<Match> matchOptional = findById(id);

        if (matchOptional.isPresent()) {
            save(verifySlot(matchOptional.get(), user));
        } else {
            throw new ConflictException("Match not found");
        }
    }

    public Match removePlayer(Long matchId, Long userId) {
        Optional<Match> matchOptional = findById(matchId);
        Optional<User> userOptional = userService.findById(userId);
        if (matchOptional.isEmpty() || userOptional.isEmpty()) {
            throw new IllegalArgumentException("Match or user not found.");
        }
        Match match = matchOptional.get();
        User user = userOptional.get();
        if (!match.getPlayers().contains(user)) {
            throw new IllegalArgumentException("The user is not associated with this match.");
        }
        return save(removeSlotPlayer(match, user));
    }

    public Match removeSlotPlayer(Match match, User user) {
        if (match.getSlot() >= 0) {
            match.removePlayer(user.getId());
            Integer newBalance = match.getSlot() + 1;
            match.setSlot(newBalance);
            return match;
        } else {
            throw new RuntimeException("The match is empty");
        }
    }

    public Match verifySlot(Match match, User user) {
        if (match.getSlot() <= 0) {
            throw new RuntimeException("The match is full");
        } else {
            match.addPlayer(user);
            Integer newBalance = match.getSlot() - 1;
            match.setSlot(newBalance);
            return match;
        }
    }
}
