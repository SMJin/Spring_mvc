package hello.itemservice.web.basic;

import hello.itemservice.domain.Item;
import hello.itemservice.domain.ItemRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/basic/items")
@RequiredArgsConstructor
public class basicItemController {

    private final ItemRepository itemRepository = new ItemRepository();

    @GetMapping
    public String items(Model model) {
        log.info("items");
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        log.info(items.toString());
        return "basic/items";
    }

    @PostConstruct
    public void init() {
        itemRepository.save(new Item("itemA", 10, 1));
        itemRepository.save(new Item("itemB", 20, 2));
    }

    @GetMapping("{itemId}")
    public String item(
            @PathVariable Long itemId,
            Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "basic/item";
    }

    @GetMapping("/add")
    public String addForm() {
        return "basic/addForm";
    }

    @PostMapping("/add")
    public String addItem(
            @ModelAttribute Item item
//            , Model model
    ) {
        itemRepository.save(item);

//        model.addAttribute("item", item);
        return "basic/item";
    }

    @GetMapping("/{itemId}/edit")
    public String editForm(
            @PathVariable Long itemId,
            Model model
    ) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "basic/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String editItem(
            @PathVariable Long itemId,
            @ModelAttribute Item item
    ) {
        itemRepository.update(itemId, item);

        return "redirect:/basic/items/{itemId}";
    }
}
