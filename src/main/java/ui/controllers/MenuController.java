package ui.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;

/**
 * Created by aidan on 16/10/16.
 */
public class MenuController {

    @FXML
    private MenuItem copyMenuItem;

    public MenuController(){

    }

    /*
    File Menu
     */
    private void close(ActionEvent event)
    {

    }

    /*
    Edit menu
     */
    @FXML
    private String copy(ActionEvent event){
        System.out.println("Copy method");
        return "Some string to clipboard";
    }

    @FXML
    private void paste(ActionEvent event){
        System.out.println("Paste method");
    }

    @FXML
    private String cut(ActionEvent event){
        System.out.println("Cut method");
        return "Some string to clipboard";
    }

    @FXML
    private void delete(ActionEvent actionEvent){
        System.out.println("delete method");
    }

    @FXML
    private void redo(ActionEvent actionEvent){
        System.out.println("redo method");
    }
    @FXML
    private void undo(ActionEvent actionEvent){
        System.out.println("undo method");
    }

    public void insert(ActionEvent actionEvent) {

        System.out.println("insert method");
    }
}
