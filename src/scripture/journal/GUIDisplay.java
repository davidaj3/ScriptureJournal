/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scripture.journal;

import java.io.File;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 *
 * @author David
 */
public class GUIDisplay extends Application {
    
    public enum DisplayType {
        DATE, SCRIPTURE, TOPIC
    }
    
    private DisplayType displayType = DisplayType.DATE;
    private JournalService js;
    
    /**
     * start
     *      Displays the GUI
     * @param primaryStage 
     */
    @Override
    public void start(Stage primaryStage) {
        js = new JournalService();
        
        TextArea display = new TextArea();
        display.setPrefRowCount(50);
        display.setEditable(false);
        display.setWrapText(true);
        display.setText("Content will appear here.  Click 'Open' or 'Import' to view an "
                + "existing journal, or use the textbox below to start a new journal.");
        
        TextArea status = new TextArea();
        status.setPrefRowCount(15);
        status.setEditable(false);
        status.setWrapText(true);
        
        
        MenuBar menuBar = new MenuBar();
        Menu menuFile = new Menu("File");
        MenuItem open = new MenuItem("Open (XML)");
        open.setOnAction(new EventHandler<ActionEvent> () {
            @Override
            public void handle(ActionEvent event) {
                FileChooser chooser = new FileChooser();
                File file = chooser.showOpenDialog(primaryStage);
                if (file != null) {
                    try {
                        js.loadFile(file.getPath());
                        status.setText("Journal Loaded");
                        display.setText(displayJournal());
                    } catch (Exception ex) {
                        status.setText("Error loading file: " + file.getPath());
                    }

                    
                }
            }
            
        });
        
        MenuItem save = new MenuItem("Save");
        save.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                FileChooser chooser = new FileChooser();
                File file = chooser.showSaveDialog(primaryStage);
                
                if (file != null) {
                    js.saveFile(file.getPath());
                    status.setText("File saved!");
                }
            }
            
        });
        
        MenuItem importTxt = new MenuItem("Import .txt File");
        importTxt.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                FileChooser chooser = new FileChooser();
                File file = chooser.showOpenDialog(primaryStage);
                
                if (file != null) {
                    
                    Updater updater = new TextAreaUpdater(status);
                    Thread thread = new Thread(new Runnable() {
                        
                        @Override
                        public void run() {
                            try {
                                js.importTextFile(file.getPath(), updater);   
                            } catch (Exception e) {
                                Platform.runLater(new Runnable() {

                                    @Override
                                    public void run() {
                                        status.setText("Error reading file: " + file.getPath());
                                    }
                                    
                                });
                            }
                            
                            Platform.runLater(new Runnable() {

                                @Override
                                public void run() {
                                    status.setText(status.getText() + "Journal imported.\n");
                                    display.setText(displayJournal());
                                }
                                
                            });
                        }
                    });
                    thread.start();
                }
            }
        });
        
        MenuItem exportTxt = new MenuItem("Export .txt File");
        exportTxt.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                FileChooser chooser = new FileChooser();
                File file = chooser.showSaveDialog(primaryStage);
                
                if (file != null) {
                    
                    js.exportTextFile(file.getPath());
                    
                    status.setText("File successfully exported to text!");
                }
            }
        });
        
        menuFile.getItems().addAll(open, importTxt, save, exportTxt);
        
        // "View" menu: contains a toggle menu to set to display by
        // date, scripture, or topic.
        Menu menuView = new Menu("View");
        
        final ToggleGroup groupDisplay = new ToggleGroup();
        RadioMenuItem displayByDate = new RadioMenuItem("Display by date");
        displayByDate.setToggleGroup(groupDisplay);
        displayByDate.setUserData(DisplayType.DATE);
        displayByDate.setSelected(true);
        
        RadioMenuItem displayByScripture = new RadioMenuItem("Display by scripture");
        displayByScripture.setToggleGroup(groupDisplay);
        displayByScripture.setUserData(DisplayType.SCRIPTURE);
        
        RadioMenuItem displayByTopic = new RadioMenuItem("Display by topic");
        displayByTopic.setToggleGroup(groupDisplay);
        displayByTopic.setUserData(DisplayType.TOPIC);
        
        menuView.getItems().addAll(displayByDate, displayByScripture, displayByTopic);
        
        groupDisplay.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {

            @Override
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
                if (groupDisplay.getSelectedToggle() != null) {
                    displayType = (DisplayType) groupDisplay.getSelectedToggle().getUserData();
                    display.setText(displayJournal());
                }
                    
            }
            
        });
     
        menuBar.getMenus().addAll(menuFile, menuView);
        
        TextArea addEntryText = new TextArea();
        addEntryText.setPrefRowCount(50);
        addEntryText.setPrefColumnCount(75);
        addEntryText.setWrapText(true);
        addEntryText.setPromptText("Type content here and click the 'Add Entry' button"
                + " to add an entry.");
        
        TextField addEntryDate = new TextField();
        addEntryDate.setMaxWidth(200);
        addEntryDate.setPromptText("Enter a date here: YYYY-MM-DD");
        
        Button addEntry = new Button("Add New Entry");
        addEntry.setMinWidth(100);
        addEntry.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                Pattern datePtrn = Pattern.compile("\\d{4}-\\d{2}-\\d{2}");
                Matcher dateMtchr = datePtrn.matcher(addEntryDate.getText());
                if (!addEntryText.getText().equals("") && !addEntryDate.getText().equals("")
                        && dateMtchr.find()) {
                    Calendar date = Calendar.getInstance();
                    String[] dateStr = addEntryDate.getText().substring(dateMtchr.start(), dateMtchr.end()).split("-");
                    date.set(Integer.parseInt(dateStr[0]), Integer.parseInt(dateStr[1]) - 1, 
                            Integer.parseInt(dateStr[2]));
                    
                    js.addNewEntry(date, addEntryText.getText());
                    display.setText(displayJournal());
                    addEntryText.setText("");
                    addEntryDate.setText("");
                }
            }
        });
        
        VBox root = new VBox();
        HBox addEntryStuff = new HBox();
        addEntryStuff.setAlignment(Pos.CENTER);
        addEntryStuff.setPadding(new Insets(2, 2, 2, 2));
        addEntryStuff.setSpacing(10);
        VBox addEntryTextFields = new VBox();
        addEntryTextFields.getChildren().addAll(addEntryDate, addEntryText);
        
        Label statusLabel = new Label("Status:");
        VBox statusBox = new VBox();
        statusBox.getChildren().addAll(statusLabel, status);
        addEntryStuff.getChildren().addAll(addEntryTextFields, addEntry, statusBox);
        root.getChildren().addAll(menuBar, display, addEntryStuff);
        
        
        Scene scene = new Scene(root, 1000, 600);
        
        primaryStage.setTitle("Scripture Journal");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public void stop() {
        System.exit(0);
    }
    
    /**
     * DisplayJournal
     *      Returns a string with a display of the entries in the journal,
     *      sorted according to the current displayType.
     * @return 
     */
    private String displayJournal() {
        switch (displayType) {
            case DATE:
                return js.displayEntriesByDate();
                
            case SCRIPTURE:
                return js.displayEntriesByScripture();
                
            case TOPIC:
                return js.displayEntriesByTopic();
            default:
                return "";
        }
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
