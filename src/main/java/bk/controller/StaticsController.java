package bk.controller;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

@Controller
public class StaticsController {
    @GetMapping("/statistics")
    public String home(Model model){
        try {
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(new FileReader("/home/quangminh/IdeaProjects/Journal2/result.json"));
            JSONArray jsonArray = (JSONArray) obj;
            model.addAttribute("result", jsonArray.toJSONString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "statics";
    }

}
