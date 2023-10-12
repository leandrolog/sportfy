package com.sportfy.service;

import com.sportfy.model.Match;
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
}
