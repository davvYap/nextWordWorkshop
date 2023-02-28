package sg.edu.nus.iss.nextWordWorkshop.controller;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import sg.edu.nus.iss.nextWordWorkshop.model.Words;

@Controller
@RequestMapping(path={"/","/words"})
public class wordsCountController {
    
    @GetMapping(path="/input")
    public String showInputForm(Model m){
        Words words = new Words();
        m.addAttribute("wordsModel",words);
        return "input";
    }

    //using query string
    @GetMapping(path="/calculate")
    public String calculateWords(@RequestParam String fullWords, Model m){
        this.calculateProbability(m, fullWords);
        return "result";
    }

    private void calculateProbability(Model m, String words){
        String newWords = words.replaceAll("\\p{Punct}", " ");
        String[] strArr = newWords.split(" ");
        List<String> list = new LinkedList<>();

        for (String word : strArr) {
            String newWord = word.replaceAll("\\s+","");
            if(newWord.isBlank() || newWord.isEmpty()){
                continue;
            }
            list.add(newWord.trim().toLowerCase());
        }

        List<String> uniqueList = getUniqueList(list);

        m.addAttribute("list",uniqueList.toArray());
    }

    // to get unique words in list
    public List<String> getUniqueList(List<String> words){
        List<String> uniqueWords = new ArrayList<>();
        for (String word : words) {
            if(!uniqueWords.contains(word)){
                uniqueWords.add(word);
            }
        }
        return uniqueWords;
    }
    
}
