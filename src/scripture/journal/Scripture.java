/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scripture.journal;

/**
 *
 * Contains a scripture reference
 */
public class Scripture {
    private String book;
    private int chapter;
    private int startVerse;
    private int endVerse;
    
    public Scripture(String bk, int ch) {
        setBook(bk);
        setChapter(ch);
    }
    
    public Scripture(String bk, int ch, int stV) {
        setBook(bk);
        setChapter(ch);
        setStartVerse(stV);
    }
    public Scripture(String bk, int ch, int stV, int endV){
        setBook(bk);
        setChapter(ch);
        setStartVerse(stV);
        setEndVerse(endV);
    }
    
    public String getBook(){
        return book;
    }
    
    public int getChapter(){
        return chapter;
    }
    
    public int getStartVerse(){
        return startVerse;
    }
    
    public int getEndVerse(){
        return endVerse;
    }
    
    public void setBook(String bk){
        book = bk;
    }
    
    public void setChapter(int ch) {
        chapter = ch;
    }
    
    public void setStartVerse(int vs) {
        startVerse = vs;
    }
    
    public void setEndVerse(int ve) {
        endVerse = ve;
    }
    
    @Override
    public String toString(){
        String result;
        result = book + " " + chapter;
        if (startVerse > 0) {
            result = result + ":" + startVerse;
        }
        if (endVerse > 0 && endVerse > startVerse) {
            result = result + "-" + endVerse;
        }
        
        return result;
    }
}
