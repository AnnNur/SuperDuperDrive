package com.udacity.jwdnd.course1.cloudstorage.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
public class Note {
    private Integer noteId;
    private String noteTitle;
    private String noteDescription;
    private Integer userId;
}
