package bk.controller;

import bk.model.Author;
import bk.model.Paper;
import bk.repository.PaperRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PaperController {
    @Autowired
    PaperRepository paperRepository;
    @GetMapping("/papers")
    public String paper(
            @RequestParam(value="page", required=false,defaultValue = "0") int page,
            @RequestParam(value="size", required=false,defaultValue = "100") int size,
            Model model){
        Pageable pageable = new PageRequest(page, size);
        Page<Paper> paperPage = paperRepository.findAll(pageable);
        model.addAttribute("papers", paperPage.getContent());
        return "paper";
    }
}
