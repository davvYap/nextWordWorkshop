package sg.edu.nus.iss.nextWordWorkshop.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import sg.edu.nus.iss.nextWordWorkshop.model.Word;
import sg.edu.nus.iss.nextWordWorkshop.repository.WordsCountRepo;

@Service
public class WordsCountService {

    @Autowired
    private WordsCountRepo wordsCR;

    public void calculateProbability(Model m, String fullWords){
        String newWords = fullWords.replaceAll("\\p{Punct}", " ");
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
        Map<String,Integer> wordsMap = getWordCount(list);
        Map<String,Map<String,Integer>> finalMap = getFinalMap(uniqueList, fullWords);
        Map<String,Map<String,Double>> answerMap = getSolutionMap(finalMap);

        // Model binding to "result.html"
        //m.addAttribute("list",uniqueList.toArray());
        m.addAttribute("wordsMap",wordsMap);
        m.addAttribute("answerMap",answerMap);

        //save to Redis in the form of <CurrWord, Count>
        //wordsCR.saveCorpusCountResult(wordsMap);

        // save to Redis in the form of <CurrWord, Map<NextWord, Probability>>, however not supported by Redis
        //wordsCR.saveCorpusProbabilityResult(answerMap);

        // to save each Word object into the Redis
        answerMap.forEach((outerKey, innerMap) -> {
            Word newWord = new Word(outerKey);
            Map<String, Double> nextWordMap = new HashMap<>();

            innerMap.forEach((innerKey, innerValue) -> {
                nextWordMap.put(innerKey, innerValue);
            });
            newWord.setNextWordProb(nextWordMap);

            // save to Redis in the form of <CurrWord, Word object>
            wordsCR.saveCorpusCountResult(newWord);
        });
    }

    // get all words from the input into list
    public List<String> getAllWordList(String fullWords){
        String newWords = fullWords.replaceAll("\\p{Punct}", " ");
        String[] strArr = newWords.split(" ");
        List<String> wordList = new ArrayList<>();
        for (String string : strArr) {
            wordList.add(string);
        }
        return wordList;
    }

    // to get unique words in list above and pass it back to another list
    public List<String> getUniqueList(List<String> words){
        List<String> uniqueWords = new ArrayList<>();
        for (String word : words) {
            if(!uniqueWords.contains(word)){
                uniqueWords.add(word);
            }
        }
        return uniqueWords;
    }

    // to get count of each word into a Map
    public Map<String,Integer> getWordCount(List<String> fullWords){
        List<String> words = fullWords;
        Map<String,Integer> wordsMap = new HashMap<>();

        for (String word : words) {
            if(wordsMap.containsKey(word)){
                wordsMap.put(word, wordsMap.get(word)+1);
            }else{
                wordsMap.put(word, 1);
            }
        }
        return wordsMap;
    }

    // get the frequency of next words for a particular word
    public Map<String,Integer> getNextWordFreqCount(String word, List<String> fullWords){
        List<Integer> nextWordIndex = new ArrayList<>();
        List<String> nextWordList = new ArrayList<>();

        // use iterator to iterate the list and get the index
        Iterator<String> iterator = fullWords.iterator();
        int i = 0;
        while(iterator.hasNext() && i < fullWords.size()-1){
            String item = iterator.next();
            if(word.equals(item)){
                nextWordIndex.add(i+1);
            }
            i++;
        }

        for (int position : nextWordIndex) {
            // to remove those words without the next word
            if(!fullWords.get(position).isEmpty() || !fullWords.get(position).isBlank()){
                nextWordList.add(fullWords.get(position));
            }
        }

        // put the next words and frequency in a map
        Map<String,Integer> nextWordFreqMap = new HashMap<>();
        for (String string : nextWordList) {
            if(nextWordFreqMap.containsKey(string)){
                nextWordFreqMap.put(string,nextWordFreqMap.get(string)+1);
            }else{
                nextWordFreqMap.put(string,1);
            }
        }
        return nextWordFreqMap;
    }


    public Map<String,Map<String,Integer>> getFinalMap(List<String> uniqueList, String fullWords){
        Map<String,Map<String,Integer>> finalMap = new HashMap<>();
        
        for (String word : uniqueList) {
            finalMap.put(word, getNextWordFreqCount(word,getAllWordList(fullWords)));
        }
        return finalMap;
    }


    public Map<String,Map<String,Double>> getSolutionMap(Map<String,Map<String,Integer>> finalMap){
        Map<String,Map<String,Double>> answerMap = new HashMap<>();
    
        //calculate the probability
        for(Map.Entry<String, Map<String,Integer>> t :finalMap.entrySet()){
            String key = t.getKey();

            Set<Entry<String,Integer>> subEntry = t.getValue().entrySet();
            List<Entry<String,Integer>> subEntryList = new ArrayList<>(subEntry);
            
            //subEntryList.forEach(e -> System.out.println(e));
            // for each key, put the key.entrySet() after the division of total
            double total = 0;
            Map<String,Double> tempMap = new HashMap<>();
            for (Entry<String,Integer> entry : subEntryList) {
                // System.out.println("value >>> " + entry.getValue());
                total += entry.getValue();
            }
            for (Entry<String,Integer> entry : subEntryList) {
                // System.out.println("value >>> " + entry.getValue());
                tempMap.put(entry.getKey(), entry.getValue()/total);
            }
            answerMap.put(key, tempMap);
        }
        return answerMap;
    }

    public Word findWordFromRepo(String word){
        return wordsCR.findByWord(word);
    };
}
