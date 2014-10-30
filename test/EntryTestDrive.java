/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.util.Date;
import java.util.List;
import org.testng.Assert;
import static org.testng.Assert.*;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import scripture.journal.Entry;
import scripture.journal.Scripture;

/**
 *
 * @author David
 */
public class EntryTestDrive {
    
    public EntryTestDrive() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}

    @Test
    public void constructorTest() {
        Entry e = new Entry(new Date(2014, 9, 28), 
                "My good sir, I believe you should consider the faith shown"
                        + "by Nephi in 1 Nephi 7:23.  It's quite remarkable.");
        
        Assert.assertEquals(e.getDate(), new Date(2014, 9, 28));
        Assert.assertEquals(e.getContent(), "My good sir, I believe you should"
                + " consider the faith shown by Nephi in 1 Nephi 7:23.  "
                + "It's quite remarkable.");
        
        List<Scripture> scriptures = e.getScriptures();
        Assert.assertEquals(scriptures.get(0).toString(), "1 Nephi 7:23");
        
        List<String> topics = e.getTopics();
        Assert.assertEquals(topics.get(0), "faith");
    }
    
    @Test
    public void editTest() throws Exception {
        Entry e = new Entry (new Date(2014, 9, 28), "Blah blah blah");
        
        e.setContent("Nerf nerf nerf");
        
        Assert.assertEquals(e.getContent(), "Nerf nerf nerf");
    }
    
    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @BeforeMethod
    public void setUpMethod() throws Exception {
    }

    @AfterMethod
    public void tearDownMethod() throws Exception {
    }
}
