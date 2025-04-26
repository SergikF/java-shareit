package ru.practicum.shareit.item;

import java.util.List;

public interface ItemService {

    Item addItem(Long idUser, ItemDtoInput itemDto);

    Item updateItem(Long idUser, Long idItem, ItemDtoInput itemDto);

    Item getItemById(Long idItem);

    void removeItem(Long idUser, Long idItem);

    List<Item> getAllItems(Long idUser);

    List<Item> searchItems(String text);

    Comment saveComment(Long userId, Long itemId, CommentDto inputCommentDto);

}
