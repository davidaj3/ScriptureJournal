/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scripture.journal;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * Finds scripture references in text and stores them in Scriptures
 */
public class ScriptureFinder {
    public Scripture parseSingle(String text){
        Pattern pattern = Pattern.compile("(\\d )?[a-zA-Z]+ ([Cc]hapter )?\\d+:?\\d*-?\\d*");
        Matcher m = pattern.matcher(text);
        String book = "";
        int chapter = 0;
        int startVerse = 0;
        int endVerse = 0;
        
        if (m.find()) {
            String ref = text.substring(m.start(), m.end());
            ref = ref.replaceAll("[Cc]hapter", "");
            Pattern bookPattern = Pattern.compile("^(\\d )?[a-zA-Z]+");
            Pattern versesPattern = Pattern.compile("\\d+:?\\d*-?\\d*$");
            Matcher mBook = bookPattern.matcher(ref);
            Matcher mVerses = versesPattern.matcher(ref);
            
            if (mBook.find()) {
                book = ref.substring(mBook.start(), mBook.end());
                if (book.equals("Covenants") || book.equals("C")) {
                    book = "D&C";
                }
            }
            
            if (mVerses.find()) {
                String chapterAndVerses = ref.substring(mVerses.start(), mVerses.end());
                if (chapterAndVerses.contains(":")) {
                    String[] chapterVersesSplit = chapterAndVerses.split(":");
                    chapter = Integer.parseInt(chapterVersesSplit[0]);
                    if (chapterVersesSplit[1].contains("-")) {
                        String[] versesSplit = chapterVersesSplit[1].split("-");
                        startVerse = Integer.parseInt(versesSplit[0]);
                        endVerse = Integer.parseInt(versesSplit[1]);
                    }
                    else {
                        startVerse = Integer.parseInt(chapterVersesSplit[1]);
                    }
                } else {
                    chapter = Integer.parseInt(chapterAndVerses);
                }
            }
        }
        
        Scripture result = new Scripture(book, chapter, startVerse, endVerse);
        
        if (isRealScripture(result)) {
            return result;
        } else {
            return null;
        }
    }
    
    public List<Scripture> parseContent(String text){
        List<Scripture> result = new ArrayList<>();
        
        Pattern referencePattern = Pattern.compile("(\\d )?[A-Z][a-z]* ([Cc]hapter )?\\d+:?\\d*-?\\d*");
        Pattern bookPattern = Pattern.compile("^(\\d )?[a-zA-Z]+");
        Pattern versesPattern = Pattern.compile("\\d+:?\\d*-?\\d*$");
        Matcher mReference = referencePattern.matcher(text);
        
        
        
        while (mReference.find()) {
            String ref = text.substring(mReference.start(), mReference.end());
            String book = "";
            int chapter = 0;
            int startVerse = 0;
            int endVerse = 0;
            ref = ref.replaceAll("[Cc]hapter", "");
            Matcher mBook = bookPattern.matcher(ref);
            Matcher mVerses = versesPattern.matcher(ref);
            
            if (mBook.find()) {
                book = ref.substring(mBook.start(), mBook.end());
                if (book.equals("Covenants") || book.equals("C")) {
                    book = "D&C";
                }
            }
            
            if (mVerses.find()) {
                String chapterAndVerses = ref.substring(mVerses.start(), mVerses.end());
                if (chapterAndVerses.contains(":")) {
                    String[] chapterVersesSplit = chapterAndVerses.split(":");
                    chapter = Integer.parseInt(chapterVersesSplit[0]);
                    if (chapterVersesSplit[1].contains("-")) {
                        String[] versesSplit = chapterVersesSplit[1].split("-");
                        startVerse = Integer.parseInt(versesSplit[0]);
                        endVerse = Integer.parseInt(versesSplit[1]);
                    }
                    else {
                        startVerse = Integer.parseInt(chapterVersesSplit[1]);
                    }
                } else {
                    chapter = Integer.parseInt(chapterAndVerses);
                }
            }
            
            Scripture scripture = new Scripture(book, chapter, startVerse, endVerse);
            if (isRealScripture(scripture)) {
                result.add(scripture);
            }
        }
        
        return result;
    }
    
    private boolean isRealScripture(Scripture scripture) {
        boolean result = false;
        
        for (String book : JournalService.SCRIPTURE_LIST.keySet()) {
            if (scripture.getBook().equals(book) &&
                    scripture.getChapter() <= JournalService.SCRIPTURE_LIST.get(book)) {
                result = true;
                break;
            }
        }
        
        return result;
    }
    
}
