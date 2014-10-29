/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scripture.journal;

import java.util.Calendar;
import java.util.List;

/**
 *
 * Contains a journal entry
 */
public class Entry implements Comparable<Entry> {
    private Calendar date;
    private String content;
    private List<Scripture> scriptures;
    private List<String> topics;
    
    public Entry(Calendar d, String text, List<Scripture> scrList, List<String> topicList) {
        date = d;
        content = text;
        scriptures = scrList;
        topics = topicList;
    }
    
    public Calendar getDate(){
        return date;
    }
    
    public String getContent(){
        return content;
    }
    
    public List<String> getTopics(){
        return topics;
    }
    
    public List<Scripture> getScriptures(){
        return scriptures;
    }
    
    int getNumScriptures() {
        return scriptures.size();
    }
    
    int getNumTopics() {
        return topics.size();
    }
    
    public void setContent(String text) {
        content = text;
    }
    
    public void setDate(Calendar theDate) {
        date = theDate;
    }
    
    public void setScriptures(List<Scripture> theScriptures) {
        scriptures = theScriptures;
    }
    
    public void setTopics(List<String> theTopics) {
        topics = theTopics;
    }

    @Override
    public int compareTo(Entry o) {
        return this.getDate().compareTo(o.getDate());
    }
    
    @Override
    public String toString() {
        String result = "";
        result += JournalService.printDate(date) + "\n";
        result += content + "\n";
        return result;
    }
}
