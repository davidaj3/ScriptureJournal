/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scripture.journal;

import javafx.application.Platform;
import javafx.scene.control.TextArea;

/**
 *
 * @author David
 */
public class TextAreaUpdater implements Updater {

    private TextArea text;
    
    public TextAreaUpdater(TextArea txt) {
        this.text = txt;
    }
    
    @Override
    public void update(int numScriptures, int numTopics) {
        Platform.runLater(new Runnable() {

            @Override
            public void run() {
                text.setText(text.getText() + "Loaded new entry\n"
                             + "\tFound " + numScriptures + " scriptures and "
                             + numTopics + " topics.\n");
            }
            
        });
    }
    
}
