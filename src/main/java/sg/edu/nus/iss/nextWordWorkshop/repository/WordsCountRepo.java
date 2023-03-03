package sg.edu.nus.iss.nextWordWorkshop.repository;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class WordsCountRepo {
    @Autowired
    RedisTemplate<String,String> redisTemplate;

    private static final String CORPUS_COUNT_MAP_KEY = "corpusCountMap";
    private static final String CORPUS_PROBABILITY_MAP_KEY = "corpusProbabilityMap";

    public void saveCorpusCountResult(Map<String,Integer> countMap){
        for(String word: countMap.keySet()){
            redisTemplate.opsForHash().put(CORPUS_COUNT_MAP_KEY, word, String.valueOf(countMap.get(word)));
        }
    }

    // Redis does not support storing NestedMap 
    public void saveCorpusProbabilityResult(Map<String,Map<String,Double>> probMap){
        for(String word: probMap.keySet()){
            redisTemplate.opsForHash().put(CORPUS_PROBABILITY_MAP_KEY, word, probMap.get(word));
        }
    }
}
