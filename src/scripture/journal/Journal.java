
package scripture.journal;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 *
 *  Holds a list of entries
 */
public class Journal {
    private List<Entry> entries;
    private int numEntries;
   
    public Journal() {
        entries = new ArrayList<>();
    }
    
    public void addEntry(Calendar date, String content,
            List<Scripture> scriptures, List<String> topics) {
        Entry newEntry = new Entry(date, content, scriptures, topics);
        entries.add(newEntry);
    }
    
    public void deleteEntry(Calendar date) {
        for (int i = 0; i < entries.size(); i++) {
            if (entries.get(i).getDate().equals(date)) {
                entries.remove(i);
            }
        }
    }
    
    public List<Entry> getEntries() {
        return entries;
    }
}
