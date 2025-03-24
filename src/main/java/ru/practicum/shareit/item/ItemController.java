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
    public Item addItem(@Validated(CreateObject.class) @RequestBody ItemDto itemDto,
                        @RequestHeader(value = "X-Sharer-User-Id", defaultValue = "0") Long idUser) {
            return itemService.addItem(idUser, itemDto);
    }

    @PatchMapping("/{idItem}")
    public Item updateItem(@PathVariable Long idItem,@Validated(UpdateObject.class) @RequestBody ItemDto itemDto,
                           @RequestHeader(value = "X-Sharer-User-Id", defaultValue = "0") Long idUser) {
        return itemService.updateItem(idUser, idItem, itemDto);
    }

    @GetMapping("/{idItem}")
    public Item getItemById(@PathVariable Long idItem) {
        return itemService.getItemById(idItem);
    }

    @GetMapping
    public List<Item> getAllItemsByUser(@RequestHeader(value = "X-Sharer-User-Id", defaultValue = "0") Long idUser) {
        return itemService.getAllItems(idUser);
    }

    @DeleteMapping("/{idItem}")
    public void removeItem(@PathVariable Long idItem,
                           @RequestHeader(value = "X-Sharer-User-Id", defaultValue = "0") Long idUser) {
        itemService.removeItem(idUser, idItem);
    }

    @GetMapping("/search")
    public List<Item> searchItemsByText(@RequestParam String text) {
        return itemService.searchItems(text);
    }

}
