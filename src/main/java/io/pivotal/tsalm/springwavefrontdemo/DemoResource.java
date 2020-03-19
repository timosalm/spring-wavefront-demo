package io.pivotal.tsalm.springwavefrontdemo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/items")
public class DemoResource {

    private static final Logger logger = LoggerFactory.getLogger(DemoResource.class);

    private DemoRepository repository;

    @Autowired
    public DemoResource(DemoRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<DemoItem> retrieveAllItems() {
        logger.info("Retrieve all items");
        final List<DemoItem> allItems = repository.findAll();
        logger.info("Retrieved {} item(s)", allItems.size());
        return allItems;
    }

    @PostMapping
    public DemoItem addItem() {
        var newItem = new DemoItem();
        logger.info("Adding item " + newItem.getId());
        final DemoItem addedItem = repository.save(newItem);
        logger.info("Added item " + newItem.getId());
        return addedItem;
    }
}
