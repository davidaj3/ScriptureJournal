/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scripture.journal;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Finds topic keywords within an entry.
 * Topic keywords are defined in a separate file.
 */
public class TopicFinder {
    
    public List<String> parseContent(String text){
        List<String> result = new ArrayList<>();
        
        for (String topic : JournalService.TOPIC_LIST.keySet()) {
            for (String keyWord : JournalService.TOPIC_LIST.get(topic)) {
                if (text.toLowerCase().contains(keyWord)) {
                    result.add(topic);
                    break;
                }
            }
        }
        
        return result;
    }
}
