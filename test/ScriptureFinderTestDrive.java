/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.util.List;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import scripture.journal.Scripture;
import scripture.journal.ScriptureFinder;

/**
 *
 * @author David
 */
public class ScriptureFinderTestDrive {
    
    public ScriptureFinderTestDrive() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
    
    @Test
    public void parseLineTest1() throws Exception {
        ScriptureFinder finder = new ScriptureFinder();
        
        List<Scripture> scrList = finder.parseContent(
                "Blah blah blah 2 Nephi 2:34 blah blah Moses 1:39 blah");
        
        Assert.assertEquals(scrList.get(0).toString(), "2 Nephi 2:34");
        Assert.assertEquals(scrList.get(1).toString(), "Moses 1:39");
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
