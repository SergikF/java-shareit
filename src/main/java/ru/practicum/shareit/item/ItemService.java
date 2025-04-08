package ru.practicum.shareit.item;

import java.util.List;

public interface ItemService {

    Item addItem(Long idUser, ItemDto itemDto);

    Item updateItem(Long idUser, Long idItem, ItemDto itemDto);

    Item getItemById(Long idItem);

    void removeItem(Long idUser, Long idItem);

    List<Item> getAllItems(Long idUser);

    List<Item> searchItems(String text);
}
