package com.bifrost.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Videogame {

    private Integer id;
    private String name;
    private String type;
    private String year;
    private Platform platform;
    private DigitalPlatform digitalPlatform;
    private String companyName;
    private int digital;
    private int completed;
    private int platinum;
    private boolean multiplayer;
    /** {@code null} means unknown. */
    private Integer hoursPlayed;
    /** Steam application ID; only set for games imported from Steam and never changed afterwards. */
    private Integer steamAppId;
    /** Optional image URL for the game; {@code null} means no cover. */
    private String coverUrl;
}
