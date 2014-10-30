/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.File;
import static org.testng.Assert.*;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.FileAssert;
import org.testng.annotations.Test;
import scripture.journal.JournalService;

/**
 *
 * @author David
 */
public class JournalServiceTestDrive {
    
    public JournalServiceTestDrive() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}

    @Test
    public void saveFileTest() throws Exception {
        JournalService js = new JournalService();
        
        js.newJournal();
        js.saveFile("banana.txt");
        
        FileAssert.assertFile(new File("banana.txt"));
    }
    
    @Test
    public void scriptureMapTest() throws Exception {
        for (String item : JournalService.SCRIPTURE_LIST.keySet()) {
            System.out.println(item);
        }
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
