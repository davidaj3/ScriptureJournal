/*
 * Scripture Journal
 * @author David Jones
 * Collaborated with Kyle Eslick
 */
package scripture.journal;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Element;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * JournalService
 * Overhead class: handles main functions of the program
 */
public class JournalService {
    
    //the journal currently being displayed/edited
    private Journal activeJournal = new Journal();
    
    //constants for use with properties
    public static final String SCRIPTURES = "scriptures";
    public static final String TOPICS = "topics";
    
    //constant lists containing books w/ numbers of chapters and
    //topics with corresponding terms.
    public static final Map<String, List<String>> TOPIC_LIST;
    public static final Map<String, Integer> SCRIPTURE_LIST;
    
    //static initializer for reading in constant topic and scripture lists
    static {
        PropertiesManager propManager = new PropertiesManager();
        Properties props = null;
        
        try {
            props = propManager.getProperties();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }
        
        //initialize scriptures
        Map<String, Integer> scrMap = new HashMap<>();
        
        String scriptureFilename = props.getProperty(SCRIPTURES);
        try {
            FileReader fileIn = new FileReader(scriptureFilename);
            BufferedReader buffy = new BufferedReader(fileIn);
            String line = "";
            String delim = ":";
            
            while ((line = buffy.readLine()) != null) {
                String[] split = line.split(delim);
                scrMap.put(split[0], Integer.parseInt(split[1]));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        SCRIPTURE_LIST = scrMap;
        
        //initialize topics
        Map<String, List<String>> tpcMap = new HashMap<>();
        
        String topicFilename = props.getProperty(TOPICS);
        try {
            FileReader fileIn = new FileReader(topicFilename);
            BufferedReader buffy = new BufferedReader(fileIn);
            String line = "";
            String delim = ":";
            String keywordDelim = ",";
            
            while ((line = buffy.readLine()) != null) {
                String[] topicAndKeywords = line.split(delim);
                String[] keywordsStr = topicAndKeywords[1].split(keywordDelim);
                List<String> keywords = new ArrayList<>(Arrays.asList(keywordsStr));
                
                tpcMap.put(topicAndKeywords[0], keywords);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        TOPIC_LIST = tpcMap;
    }
    
    public void newJournal() {
        activeJournal = new Journal();
    }
    
    public void addNewEntry(Calendar date, String content) {
        ScriptureFinder sFinder = new ScriptureFinder();
        TopicFinder tFinder = new TopicFinder();
        activeJournal.addEntry(date, content, sFinder.parseContent(content), tFinder.parseContent(content));
    }
    
    /*
    * loadFile
    *    Loads a file from the XML document provided.
    *  Input:
    *    filename: the XML file to be loaded.
    *  Output:
    *    Parses the file and assigns the resulting Journal object to activeJournal
    */
    public void loadFile(String filename) {
        activeJournal = new Journal();
        
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance(); 
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            
            Document dom = builder.parse(new File(filename));
            dom.getDocumentElement().normalize();
            
            NodeList entries = dom.getElementsByTagName("entry");
            
            for (int i = 0; i < entries.getLength(); i++) {
                Node node = entries.item(i);
                
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element elem = (Element) node;
                    
                    //Set the date
                    Calendar date = Calendar.getInstance();
                    String[] dateStr = elem.getAttribute("date").split("-");
                    date.set(Calendar.YEAR, Integer.parseInt(dateStr[0]));
                    date.set(Calendar.MONTH, Integer.parseInt(dateStr[1]) - 1);
                    date.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dateStr[2]));
                    
                    //Set the content
                    String content = elem.getElementsByTagName("content").item(0).getTextContent();
                    content = content.trim();
                    content = content.replaceAll("\\n\\s+", "\n");
                    
                    //Set the scriptures
                    List<Scripture> scriptures = new ArrayList<>();
                    
                    NodeList scriptureNodes = elem.getElementsByTagName("scripture");
                    
                    for (int j = 0; j < scriptureNodes.getLength(); j++) {
                        if (scriptureNodes.item(j).getNodeType() == Node.ELEMENT_NODE) {
                            Element scriptureElem = (Element) scriptureNodes.item(j);
                            String book = scriptureElem.getAttribute("book");
                            String chapter = scriptureElem.getAttribute("chapter");
                            String startVerse = scriptureElem.getAttribute("startverse");
                            String endVerse = scriptureElem.getAttribute("endverse");
                            
                            Scripture scripture = new Scripture(book, Integer.parseInt(chapter));
                            
                            if (!startVerse.isEmpty()) {
                                scripture.setStartVerse(Integer.parseInt(startVerse));
                            }
                            
                            if (!endVerse.isEmpty()) {
                                scripture.setEndVerse(Integer.parseInt(endVerse));
                            }
                            scriptures.add(scripture);
                        }
                    }
                    
                    //Set the topics
                    List<String> topics = new ArrayList<>();
                    
                    NodeList topicNodes = elem.getElementsByTagName("topic");
                    
                    for (int j = 0;  j < topicNodes.getLength(); j++) {
                        String topic = topicNodes.item(j).getTextContent();
                        topics.add(topic);
                    }
                    
                    activeJournal.addEntry(date, content, scriptures, topics);
                }
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }   
    }
    
    public void saveFile(String filename) {
        try {
            DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            
            Document doc = dBuilder.newDocument();
            Element rootElement = doc.createElement("journal");
            doc.appendChild(rootElement);
            
            for (Entry entry : activeJournal.getEntries()) {
                Element entryElement = doc.createElement("entry");
                rootElement.appendChild(entryElement);
                entryElement.setAttribute("date", printDate(entry.getDate()));
                
                for (Scripture scripture : entry.getScriptures()) {
                    Element scriptureElem = doc.createElement("scripture");
                    entryElement.appendChild(scriptureElem);
                    
                    scriptureElem.setAttribute("book", scripture.getBook());
                    scriptureElem.setAttribute("chapter", Integer.toString(scripture.getChapter()));
                    
                    if (scripture.getStartVerse() != 0) {
                        scriptureElem.setAttribute("startverse", Integer.toString(scripture.getStartVerse()));
                    }
                    if (scripture.getEndVerse() != 0) {
                        scriptureElem.setAttribute("endverse", Integer.toString(scripture.getEndVerse()));
                    }
                }
                
                for (String topic : entry.getTopics()) {
                    Element topicElem = doc.createElement("topic");
                    entryElement.appendChild(topicElem);
                    
                    topicElem.appendChild(doc.createTextNode(topic));
                }
                
                Element content = doc.createElement("content");
                content.appendChild(doc.createTextNode(entry.getContent()));
                entryElement.appendChild(content);
            }
            
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(filename));
            
            transformer.transform(source, result);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
    
    public void importTextFile(String filename, Updater updater) throws Exception {
        activeJournal = new Journal();
        BufferedReader buffy = new BufferedReader(new FileReader(filename));
        String line = "";
        Calendar date = Calendar.getInstance();
        String content = "";
        boolean first = true;
        
        ScriptureFinder scrFinder = new ScriptureFinder();
        TopicFinder tpcFinder = new TopicFinder();
       
        while ((line = buffy.readLine()) != null) {
            if (line.equals("-----")) {
                if (!first) {
                    //add previous entry
                    List<Scripture> scriptures = scrFinder.parseContent(content);
                    List<String> topics = tpcFinder.parseContent(content);
                    
                    activeJournal.addEntry(date, content, scriptures, topics);
                    updater.update(scriptures.size(), topics.size());
                    Thread.sleep(500);
                    
                    //reset content and date fields for new entry
                    content = "";
                    date = Calendar.getInstance();
                } else {
                    first = false;
                }
            } else if (line.matches("\\d{4}-\\d{2}-\\d{2}")) {
                String[] dateData = line.split("-");
                date.set(Calendar.YEAR, Integer.parseInt(dateData[0]));
                date.set(Calendar.MONTH, Integer.parseInt(dateData[1]) - 1);
                date.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dateData[2]));
                
            } else {
                content = content + line + "\n";
            }
        }
        
        //add the last entry
        List<Scripture> scriptures = scrFinder.parseContent(content);
        List<String> topics = tpcFinder.parseContent(content);
        activeJournal.addEntry(date, content, scriptures, topics);
        updater.update(scriptures.size(), topics.size());
            
    }
    
    
    public void exportTextFile(String filename) {
        BufferedWriter out = null;
        
        try {
            out = new BufferedWriter(new FileWriter(filename));
            for (Entry entry : activeJournal.getEntries()) {
                out.write("-----\n");
                out.write(printDate(entry.getDate()) + "\n");
                out.write(entry.getContent() + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    /*
    *  displayEntries
    *     Displays all entries in the active journal, in order of appearance.
    */
    public String displayEntriesByDate() {
        List<Entry> entryList = activeJournal.getEntries();
        String result = "";
        
        Collections.sort(entryList);
        Collections.reverse(entryList);
        
        for (Entry entry : entryList) {
            result += entry.toString();
            result += "Scriptures: \n";
            for (Scripture scr : entry.getScriptures()) {
                result += "\t" + scr.toString() + "\n";
            }
            result += "Topics: \n";
            for (String topic : entry.getTopics()) {
                result += "\t" + topic + "\n";
            }
            result += "\n";
        }
            
        return result;
    }
    
    public String displayEntriesByScripture() {
        String result = "";
        
        for (String book : SCRIPTURE_LIST.keySet()) {
            List<Entry> entriesContainingScripture = new ArrayList<>();
            
            for (Entry entry : activeJournal.getEntries()) {
                
                for (Scripture scr : entry.getScriptures()) {
                    if (scr.getBook().equals(book)) {
                        entriesContainingScripture.add(entry);
                        break;
                    }
                }
            }
            
            if (!entriesContainingScripture.isEmpty()) {
                result = result + book + "\n\n";
                for (Entry entry : entriesContainingScripture) {
                    result += entry.toString();
                }
                result += "\n";
            }
        }
        result = result + "\n";
        return result;
    }
    
    public String displayEntriesByTopic() {
        String result = "";
        for (String topic : JournalService.TOPIC_LIST.keySet()) {
            List<Entry> entriesContainingTopic = new ArrayList<>();
            
            for (Entry entry : activeJournal.getEntries()) {
                for (String tpc : entry.getTopics()) {
                    if (tpc.equals(topic)) {
                        entriesContainingTopic.add(entry);
                        break;
                    }
                }
            }
            
            if (!entriesContainingTopic.isEmpty()) {
                result += topic + "\n\n";
                for (Entry entry : entriesContainingTopic) {
                    result += entry.toString() + "\n";
                }
            }
        }
        result += "\n";
        return result;
    }
    
    public static String printDate(Calendar date) {
        String result = "";
        
        int year = date.get(Calendar.YEAR);
        int month = date.get(Calendar.MONTH) + 1;
        int day = date.get(Calendar.DAY_OF_MONTH);
        result = year + "-" + month + "-" + day;
        return result;
    }
    
    public static void main(String[] args) {
        
        if (args.length < 3) {
            System.err.println("ERROR! Please enter an input txt file, an ouput xml file,"
                    + "and an output txt file on the command line.");
            System.exit(1);
        }
        
        JournalService js = new JournalService();
        try {
            js.importTextFile(args[0], null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        js.addNewEntry(Calendar.getInstance(), "I really like 2 Nephi 32:3-4, because prayer.\n");
        System.out.println(js.displayEntriesByTopic());
    }
}
