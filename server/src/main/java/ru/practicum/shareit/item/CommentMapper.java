package ru.practicum.shareit.item;

import org.springframework.stereotype.Component;

@Component
public class CommentMapper {

    public static CommentDto toCommentDto(Comment comment) {
        if (comment == null) {
            return null;
        }
        CommentDto commentDto = new CommentDto();

        commentDto.setId(comment.getId());
        commentDto.setText(comment.getText());
        commentDto.setItem(comment.getItem());
        commentDto.setUser(comment.getUser());
        commentDto.setCreated(comment.getCreated());

        return commentDto;
    }

    public static CommentDtoOutput toCommentDtoOutput(Comment comment) {
        if (comment == null) {
            return null;
        }
        CommentDtoOutput commentDto = new CommentDtoOutput();

        commentDto.setId(comment.getId());
        commentDto.setText(comment.getText());
        commentDto.setItem(ItemMapper.toItemDtoShort(comment.getItem()));
        commentDto.setAuthorName(comment.getUser().getName());
        commentDto.setCreated(comment.getCreated());

        return commentDto;
    }

    public static CommentDtoShort toCommentDtoShort(Comment comment) {
        if (comment == null) {
            return null;
        }
        CommentDtoShort commentDto = new CommentDtoShort();

        commentDto.setId(comment.getId());
        commentDto.setText(comment.getText());
        commentDto.setItem(ItemMapper.toItemDtoShort(comment.getItem()));
        commentDto.setAuthorName(comment.getUser().getName());
        commentDto.setCreated(comment.getCreated());

        return commentDto;
    }

    public static Comment toComment(CommentDto commentDto) {
        Comment comment = new Comment();

        if (commentDto.getId() != null) {
            if (commentDto.getId() > 0) {
                comment.setId(commentDto.getId());
            }
        }
        if (commentDto.getText() != null) {
            if (!commentDto.getText().trim().isEmpty()) {
                comment.setText(commentDto.getText().trim());
            }
        }
        if (commentDto.getItem().getId() != null) {
            if (commentDto.getItem().getId() > 0) {
                comment.setItem(commentDto.getItem());
            }
        }
        if (commentDto.getUser().getId() != null) {
            if (commentDto.getUser().getId() > 0) {
                comment.setUser(commentDto.getUser());
            }
        }
        if (commentDto.getCreated() != null) {
            comment.setCreated(commentDto.getCreated());
        }

        return comment;
    }

}
