package bk.controller;

import bk.model.Author;
import bk.model.CoAuthorshipSimply;
import bk.model.Pager;
import bk.model.Tag;
import bk.repository.AuthorRepository;
import bk.repository.CoAuthorshipSimplyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class AuthorController {
    private static final int BUTTONS_TO_SHOW = 5;
    private static final int INITIAL_PAGE = 0;
    private static final int INITIAL_PAGE_SIZE = 50;
    private static final int[] PAGE_SIZES = {50, 100, 200};
    @Autowired
    AuthorRepository authorRepository;
    @Autowired
    CoAuthorshipSimplyRepository coAuthorshipSimplyRepository;
    @GetMapping("/authors")
    public String author(
            @RequestParam(value = "page", required = false, defaultValue = "0") Optional<Integer> page,
            @RequestParam(value = "pageSize", required = false, defaultValue = "100") Optional<Integer> pageSize,
            Model model) {
        int evalPageSize = pageSize.orElse(INITIAL_PAGE_SIZE);
        int evalPage = (page.orElse(0) < 1) ? INITIAL_PAGE : page.get() - 1;
        Pageable pageable = new PageRequest(evalPage, evalPageSize);
        Page<Author> authors = authorRepository.findAll(pageable);
        Pager pager = new Pager(authors.getTotalPages(), authors.getNumber(), BUTTONS_TO_SHOW);
        model.addAttribute("authors", authors);
        model.addAttribute("selectedPageSize", evalPageSize);
        model.addAttribute("pageSizes", PAGE_SIZES);
        model.addAttribute("pager", pager);
        return "author";
    }

    @RequestMapping(value = "/authors/get-id", method = RequestMethod.GET)
    public @ResponseBody
    List<Author> getTags(@RequestParam String id) {
        List<Author> authors = authorRepository.findByIdPrefix(id + "%");
        return authors;
    }
}
