package ru.practicum.shareit.item;

public class ItemMapper {

    public static ItemDto toItemDto(Item item) {
        return new ItemDto(
                item.getName(),
                item.getDescription(),
                item.getAvailable()
        );
    }

    public static Item toItem(ItemDto itemDto) {
        if (itemDto.getName() != null) {
            itemDto.setName(itemDto.getName().trim());
        }
        if (itemDto.getDescription() != null) {
            itemDto.setDescription(itemDto.getDescription().trim());
        }
        return new Item(
                null,
                itemDto.getName(),
                itemDto.getDescription(),
                itemDto.getAvailable(),
                null,
                null
        );
    }

}
