package com.sportfy.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "match")
@AllArgsConstructor
@NoArgsConstructor
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Schedule schedule;
    private Integer slot;
    private String category;
    @Column(nullable = true)
    @OneToMany()
    @JoinColumn(name = "user_id")
    private List<User> players;


    public void addPlayer(User user) {
        if (this.players == null) {
            this.players = new ArrayList<>();
        }
        this.players.add(user);
    }
}
