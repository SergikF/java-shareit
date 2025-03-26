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
    public ItemDto addItem(@Validated(CreateObject.class) @RequestBody ItemDto itemDto,
                        @RequestHeader(value = "X-Sharer-User-Id") Long idUser) {
        return ItemMapper.toItemDto(itemService.addItem(idUser, itemDto));
    }

    @PatchMapping("/{idItem}")
    public ItemDto updateItem(@PathVariable Long idItem, @Validated(UpdateObject.class) @RequestBody ItemDto itemDto,
                           @RequestHeader(value = "X-Sharer-User-Id") Long idUser) {
        return ItemMapper.toItemDto(itemService.updateItem(idUser, idItem, itemDto));
    }

    @GetMapping("/{idItem}")
    public ItemDto getItemById(@PathVariable Long idItem) {
        return ItemMapper.toItemDto(itemService.getItemById(idItem));
    }

    @GetMapping
    public List<ItemDto> getAllItemsByUser(@RequestHeader(value = "X-Sharer-User-Id") Long idUser) {
        return itemService.getAllItems(idUser).stream().map(ItemMapper::toItemDto).toList();
    }

    @DeleteMapping("/{idItem}")
    public void removeItem(@PathVariable Long idItem,
                           @RequestHeader(value = "X-Sharer-User-Id") Long idUser) {
        itemService.removeItem(idUser, idItem);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItemsByText(@RequestParam String text) {
        return itemService.searchItems(text).stream().map(ItemMapper::toItemDto).toList();
    }

}
