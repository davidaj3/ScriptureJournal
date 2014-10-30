/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.util.List;
import org.testng.Assert;
import static org.testng.Assert.*;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import scripture.journal.TopicFinder;

/**
 *
 * @author David
 */
public class TopicFinderTestDrive {
    
    public TopicFinderTestDrive() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}

    @Test
    public static void parseSingleTest() throws Exception {
        TopicFinder finder = new TopicFinder();
        
        List<String> topics = finder.parseContent("Blah blah blah faith blah blah");
        Assert.assertEquals(topics.get(0), "Faith");
    }
    
    @Test
    public static void parseSingleTest2() throws Exception {
        TopicFinder finder = new TopicFinder();
        
        List<String> topics = finder.parseContent("Blah blah Testimony");
        Assert.assertEquals(topics.get(0), "Testimony");
    }
    
    @Test
    public static void parseLineTest() throws Exception {
        TopicFinder finder = new TopicFinder();
        
        List<String> topics = finder.parseContent("Faith blah blah prayer blah");
        Assert.assertEquals(topics.get(0), "Faith");
        Assert.assertEquals(topics.get(1), "Seek");
        Assert.assertEquals(topics.get(2), "Prayer");
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
