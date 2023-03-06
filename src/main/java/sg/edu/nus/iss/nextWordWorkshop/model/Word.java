package sg.edu.nus.iss.nextWordWorkshop.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Word implements Serializable {

    private String currWord;

    private Map<String, Double> nextWordProb = new HashMap<>();

    public String getCurrWord() {
        return currWord;
    }

    public void setCurrWord(String currWord) {
        this.currWord = currWord;
    }
    
    public Map<String, Double> getNextWordProb() {
        return nextWordProb;
    }
    
    public void setNextWordProb(Map<String, Double> nextWordProb) {
        this.nextWordProb = nextWordProb;
    }
    
    // construtor
    public Word(String currWord) {
        this.currWord = currWord;
    }
    
}
