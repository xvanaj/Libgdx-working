package com.mygdx.game.world.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LogEntry {

    private EntryType entryType;
    private Long hour;
    private String entry;

}
