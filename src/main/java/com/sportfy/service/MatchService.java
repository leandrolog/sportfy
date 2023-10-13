package com.sportfy.service;

import com.sportfy.controller.ConflictException;
import com.sportfy.model.Match;
import com.sportfy.model.User;
import com.sportfy.repository.MatchRepository;
import jakarta.transaction.Transactional;
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
        Optional<User> userOptional = userService.findById(user.getId());

        if(matchOptional.isPresent()){
            Match match = matchOptional.get();
            verifySlot(match);
            match.addPlayer(user);
            save(match);
        }else{
            throw new ConflictException("Match not found");
        }
    }

    public void verifySlot(Match match){
        if(match.getSlot() <= 0){
            throw new IllegalArgumentException ("The match is full");
        }else{
            Integer newBalance = match.getSlot() - 1;
            match.setSlot(newBalance);
        }
    }
}
