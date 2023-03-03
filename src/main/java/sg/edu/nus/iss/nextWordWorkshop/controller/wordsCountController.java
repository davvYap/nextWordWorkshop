package sg.edu.nus.iss.nextWordWorkshop.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import sg.edu.nus.iss.nextWordWorkshop.model.Words;
import sg.edu.nus.iss.nextWordWorkshop.util.wordsCountUtil;

@Controller
@RequestMapping(path={"/","/words"})
public class wordsCountController {

    @Autowired
    private wordsCountUtil wordsCU;
    
    @GetMapping(path="/input")
    public String showInputForm(Model m){
        Words words = new Words();
        m.addAttribute("wordsModel",words);
        return "input";
    }

    //using query string
    @GetMapping(path="/calculate")
    public String calculateWords(@RequestParam String fullWords, Model m){
        wordsCU.calculateProbability(m, fullWords);
        return "result";
    }    
}
