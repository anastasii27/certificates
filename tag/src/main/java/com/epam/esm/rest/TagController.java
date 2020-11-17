package com.epam.esm.rest;

import com.epam.esm.dto.TagDto;
import com.epam.esm.model.Pagination;
import com.epam.esm.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.afford;

@RestController
@RequestMapping("/tags")
@EnableHypermediaSupport(type = EnableHypermediaSupport.HypermediaType.HAL_FORMS)
public class TagController {
    @Autowired
    private TagService tagService;

    @GetMapping
    public List<TagDto> getAllTags(@Valid Pagination pagination){
        List<TagDto> tags = tagService.findAll(pagination);
        tags.forEach(
                tag-> tag.add(linkTo(methodOn(TagController.class)
                        .getTag(tag.getId()))
                        .withSelfRel())
        );
        return tags;
    }

    @GetMapping("/{id}")
    public EntityModel<TagDto> getTag(@PathVariable long id){
        TagDto tag = tagService.findById(id);

        return EntityModel.of(tag, linkTo(methodOn(TagController.class)
                .getTag(id)).withSelfRel()
                .andAffordance(afford(methodOn(TagController.class).deleteTag(id))));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EntityModel<TagDto> createTag(@RequestBody @Valid TagDto tag){
        TagDto createdTag = tagService.create(tag);

        return EntityModel.of(createdTag, linkTo(methodOn(TagController.class)
                .createTag(tag)).withSelfRel()
                .andAffordance(afford(methodOn(TagController.class).deleteTag(createdTag.getId()))));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTag(@PathVariable long id){
        tagService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
