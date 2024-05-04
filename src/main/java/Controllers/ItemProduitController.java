package Controllers;

import java.net.URL;
import java.util.ResourceBundle;


import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class ItemProduitController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ImageView img;

    @FXML
    private Label nameLabel;

    @FXML
    private Label priceLabel;

    @FXML
    private Label priceLable;

    @FXML
    void click(MouseEvent event) {

    }

    @FXML
    void initialize() {

    }
    // Method to set location information in the UI
    public void setLocation(String name,Double price ,Image image) {
        nameLabel.setText(name);
        priceLabel.setText(String.valueOf(price)); // Convert Double to String
        img.setImage(image);
    }

}

