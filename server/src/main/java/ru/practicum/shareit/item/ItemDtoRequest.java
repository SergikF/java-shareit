package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemDtoRequest {

    private Long idItem;

    private String name;

    private Long idOwner;

    private String Owner;

}
