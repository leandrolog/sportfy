package com.sportfy.model;

import com.sportfy.model.Match;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    @Column(nullable = true)
    @OneToMany
    @JoinColumn(name = "match_id")
    private List<Match> matches;
    private String role;
    @Column(nullable = true)
    private String password;
}
