package sg.edu.nus.iss.nextWordWorkshop.model;

public class Words {
    private String fullWords = null;
    private String currWord = null;
    private String nextWord = null;
    private int count = 0;

    public String getCurrWord() {
        return currWord;
    }
    public void setCurrWord(String currWord) {
        this.currWord = currWord;
    }
    public String getNextWord() {
        return nextWord;
    }
    public void setNextWord(String nextWord) {
        this.nextWord = nextWord;
    }
    public int getCount() {
        return count;
    }
    public void setCount(int count) {
        this.count = count;
    }
    public String getFullWords() {
        return fullWords;
    }
    public void setFullWords(String fullWords) {
        this.fullWords = fullWords;
    }
}
