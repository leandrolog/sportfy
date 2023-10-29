package com.sportfy.dto;

import com.sportfy.model.Schedule;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MatchDto {

    private String title;
    private Schedule schedule;
    private Integer slot;
    private String category;

}
