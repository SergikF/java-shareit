package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDtoShort {

    private Long id;

    private String text;

    private ItemDtoShort item;

    private String authorName;

    private LocalDateTime created;

}
