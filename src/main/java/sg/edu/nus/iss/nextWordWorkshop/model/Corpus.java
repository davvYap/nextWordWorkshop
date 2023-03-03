package sg.edu.nus.iss.nextWordWorkshop.model;

import java.io.Serializable;
import java.util.Map;

public class Corpus implements Serializable {
    private Map<String,Integer> countMap;
    private Map<String,Map<String,Double>> probabilityMap;

    public Map<String, Integer> getCountMap() {
        return countMap;
    }
    public void setCountMap(Map<String, Integer> countMap) {
        this.countMap = countMap;
    }
    public Map<String, Map<String, Double>> getProbabilityMap() {
        return probabilityMap;
    }
    public void setProbabilityMap(Map<String, Map<String, Double>> probabilityMap) {
        this.probabilityMap = probabilityMap;
    }    
}
