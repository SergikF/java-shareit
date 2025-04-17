package ru.practicum.shareit.item;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.validation.CreateObject;
import ru.practicum.shareit.validation.UpdateObject;

import java.util.List;

@RestController
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemDtoOutput addItem(@Validated(CreateObject.class) @RequestBody ItemDto itemDto,
                                 @RequestHeader(value = "X-Sharer-User-Id") Long idUser) {
        return ItemMapper.toItemDtoOutput(itemService.addItem(idUser, itemDto), idUser);
    }

    @PatchMapping("/{idItem}")
    public ItemDtoOutput updateItem(@PathVariable Long idItem,
                                    @Validated(UpdateObject.class) @RequestBody ItemDto itemDto,
                                    @RequestHeader(value = "X-Sharer-User-Id") Long idUser) {
        return ItemMapper.toItemDtoOutput(itemService.updateItem(idUser, idItem, itemDto), idUser);
    }

    @GetMapping("/{idItem}")
    public ItemDtoOutput getItemById(@PathVariable Long idItem,
                                     @RequestHeader(value = "X-Sharer-User-Id") Long idUser) {
        return ItemMapper.toItemDtoOutput(itemService.getItemById(idItem), idUser);
    }

    @GetMapping
    public List<ItemDtoOutput> getAllItemsByUser(@RequestHeader(value = "X-Sharer-User-Id") Long idUser) {
        return itemService.getAllItems(idUser).stream()
                .map(item -> ItemMapper.toItemDtoOutput(item, idUser)).toList();
    }

    @DeleteMapping("/{idItem}")
    public void removeItem(@PathVariable Long idItem,
                           @RequestHeader(value = "X-Sharer-User-Id") Long idUser) {
        itemService.removeItem(idUser, idItem);
    }

    @GetMapping("/search")
    public List<ItemDtoOutput> searchItemsByText(@RequestParam String text,
                                                 @RequestHeader(value = "X-Sharer-User-Id") Long idUser) {
        return itemService.searchItems(text).stream()
                .map(item -> ItemMapper.toItemDtoOutput(item, idUser)).toList();
    }

    @PostMapping("/{itemId}/comment")
    public CommentDtoOutput addCommentToItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                                             @PathVariable Long itemId, @RequestBody CommentDto commentDto) {
        return CommentMapper.toCommentDtoOutput((itemService.saveComment(userId, itemId, commentDto)));
    }
}
