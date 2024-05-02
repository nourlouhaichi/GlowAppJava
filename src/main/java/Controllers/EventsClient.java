package Controllers;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import Entities.Event;
import Entities.Reservation;
import Entities.User;
import Services.EventServices;
import Services.ReservationServices;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;



public class EventsClient {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Label LocationNameLabel;

    @FXML
    private Label LocationPriceLabel;

    @FXML
    private Label adresseLabel;

    @FXML
    private VBox chosenFruitCard;

    @FXML
    private Label descriptionLabel;

    @FXML
    private GridPane grid;

    @FXML
    private ImageView locationimg;

    @FXML
    private ScrollPane scroll;

    private int selectedLocationId;


    private Image generateQRCodeImage(String content) {
        int width = 300;
        int height = 300;
        BufferedImage qrImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");

        try {
            BitMatrix bitMatrix = new QRCodeWriter().encode(content, com.google.zxing.BarcodeFormat.QR_CODE, width, height, hints);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    qrImage.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
                }
            }
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ImageIO.write(qrImage, "png", byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            return new Image(new ByteArrayInputStream(byteArray));
        } catch (WriterException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    @FXML
    void handleReserveButtonClick(ActionEvent event) {
        if (selectedLocationId != 0) { // Check if a location is selected
            // Create a new reservation in the database with the selected location ID
            ReservationServices reservationService = new ReservationServices();
            try {
                EventServices ev = new EventServices();
                Event x = ev.getEventById(selectedLocationId);
                Reservation y = new Reservation();
                y.setEvent(x);

                // Assuming user details are set elsewhere in your application
                User us = new User();
                us.setCin("14326585");
                y.setUser(us);

                // Show confirmation alert
                Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
                confirmationAlert.setTitle("Confirm Reservation");
                confirmationAlert.setHeaderText(null);
                confirmationAlert.setContentText("Are you sure you want to make this reservation?");

                Optional<ButtonType> result = confirmationAlert.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    // Check if there are available seats
                    if (x.getNbP() > 0) {
                        // Decrement available seats
                        x.setNbP(x.getNbP() - 1);

                        // Update the event in the database
                        ev.modifier(x);

                        // User confirmed reservation, add it to the database
                        reservationService.ajouter(y);
                        // Générer le contenu du QR code
                        String qrContent = "Client ID: " + us.getCin() + "\nEvent Title: " + x.getTitle();
                        // Générer l'image du QR code
                        Image qrCodeImage = generateQRCodeImage(qrContent);

                        // Show success alert
                        Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                        successAlert.setTitle("Success");
                        successAlert.setHeaderText(null);
                        successAlert.setContentText("Reservation successfully added!");
                        // Créer le bouton PDF
                        ButtonType pdfButton = new ButtonType("Save PDF");
                        successAlert.getButtonTypes().add(pdfButton);

                        // Ajouter l'image du QR code à l'alerte
                        ImageView qrCodeImageView = new ImageView(qrCodeImage);
                        qrCodeImageView.setFitWidth(200); // Ajustez la taille selon vos besoins
                        qrCodeImageView.setFitHeight(200);
                        successAlert.getDialogPane().setGraphic(qrCodeImageView);
                        // Générer le document PDF et afficher l'alerte de succès
                        successAlert.showAndWait().ifPresent(buttonType -> {
                            if (buttonType == pdfButton) {
                                BufferedImage img =convertToBufferedImage(qrCodeImage);
                                genererPDF(qrContent,img); // Appeler la méthode pour générer le PDF avec le contenu du QR code
                            }
                        });
                        // Optionally, provide feedback to the user that the reservation was successful
                        // For example: show an alert or update a status label

                    } else {
                        // No more available seats, show message
                        Alert noSeatsAlert = new Alert(Alert.AlertType.WARNING);
                        noSeatsAlert.setTitle("No More Places");
                        noSeatsAlert.setHeaderText(null);
                        noSeatsAlert.setContentText("Sorry, there are no more available seats for this event.");
                        noSeatsAlert.showAndWait();
                    }
                    // User canceled reservation, do nothing or provide feedback
                }
            } catch (SQLException e) {
                // Handle any exceptions that occur during reservation creation
                e.printStackTrace();
                // Optionally, provide feedback to the user about the error
            }
        } else {
            // If no location is selected, display an alert to the user
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Event Selected");
            alert.setHeaderText(null);
            alert.setContentText("Please select an event before making a reservation.");
            alert.showAndWait();
        }
    }

    private BufferedImage convertToBufferedImage(Image image) {
        int width = (int) image.getWidth();
        int height = (int) image.getHeight();

        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        // Get the pixel reader from the JavaFX image
        PixelReader pixelReader = image.getPixelReader();

        // Iterate over each pixel and set the corresponding pixel in the buffered image
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                // Get the color of the pixel from the JavaFX image
                javafx.scene.paint.Color color = pixelReader.getColor(x, y);

                // Convert the JavaFX Color to an AWT Color
                int alpha = (int) (color.getOpacity() * 255);
                int red = (int) (color.getRed() * 255);
                int green = (int) (color.getGreen() * 255);
                int blue = (int) (color.getBlue() * 255);

                // Set the corresponding pixel in the buffered image
                int argb = (alpha << 24) | (red << 16) | (green << 8) | blue;
                bufferedImage.setRGB(x, y, argb);
            }
        }

        return bufferedImage;
    }

    private void genererPDF(String qrContent, BufferedImage qrCodeImage) {
        try {
            // Créer un nouveau document PDF
            PDDocument document = new PDDocument();
            PDPage page = new PDPage();
            document.addPage(page);

            // Initialiser le flux de contenu de la page
            PDPageContentStream contentStream = new PDPageContentStream(document, page);

            // Charger l'image du logo
            InputStream logoStream = getClass().getResourceAsStream("/image/logoApp.png");
            if (logoStream == null) {
                System.out.println("Logo image not found!");
                return;
            }
            BufferedImage logoImage = ImageIO.read(logoStream);

            // Convertir l'image du logo en PDImageXObject
            PDImageXObject logoPDImage = LosslessFactory.createFromImage(document, logoImage);

            // Définir la position et la taille du logo
            final float margin = 0; // Définir la marge selon vos besoins
            float logoWidth = 125;
            float logoHeight = logoWidth * logoImage.getHeight() / logoImage.getWidth();
            float logoX = page.getMediaBox().getWidth() - 2 * margin - logoWidth;
            float logoY = page.getMediaBox().getHeight() - 2 * margin - logoHeight;

            // Afficher le logo sur la page
            contentStream.drawImage(logoPDImage, logoX, logoY, logoWidth, logoHeight);
            qrContent = qrContent.replaceAll("\\n", " ");

            // Ajouter le contenu du QR code
            PDType1Font font = PDType1Font.HELVETICA_BOLD; // Or another font that supports line feed
            contentStream.setFont(font, 12);
            contentStream.beginText();
            contentStream.newLineAtOffset(100, 700); // Position du texte (ajustez selon vos besoins)
            contentStream.showText(qrContent);
            contentStream.endText();

            // Convert qrCodeImage to PDImageXObject
            PDImageXObject qrCodePDImage = LosslessFactory.createFromImage(document, qrCodeImage);

            // Position and size of the QR code image
            float qrCodeX = 100;
            float qrCodeY = 500;
            float qrCodeWidth = 200;
            float qrCodeHeight = 200;

            // Draw the QR code image onto the page
            contentStream.drawImage(qrCodePDImage, qrCodeX, qrCodeY, qrCodeWidth, qrCodeHeight);

            // Fermer le flux de contenu
            contentStream.close();

            // Sauvegarder le document PDF
            File file = new File("Reservation.pdf");
            document.save(file);
            document.close();

            // Ouvrir le document PDF avec l'application par défaut
            Desktop.getDesktop().open(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    @FXML
    private void handleItemClick(MouseEvent event, int locationId) {
        // Store the selected location ID
        selectedLocationId = locationId;

        // Fetch location details using locationId
        EventServices locationService = new EventServices();
        Event clickedLocation = null;
        try {
            clickedLocation = locationService.getEventById(locationId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // Update chosen location
        updateChosenLocation(clickedLocation);

    }
    private void updateChosenLocation(Event location) {
        Label locationNameLabel = (Label) chosenFruitCard.lookup("#LocationNameLabel");
        Label locationPriceLabel = (Label) chosenFruitCard.lookup("#LocationPriceLabel");
        ImageView locationImageView = (ImageView) chosenFruitCard.lookup("#locationimg");
        Label adresseLabel = (Label) chosenFruitCard.lookup("#adresseLabel"); // Add this line
        Label descriptionLabel = (Label) chosenFruitCard.lookup("#descriptionLabel"); // Add this line

        // Update labels with location information
        locationNameLabel.setText(location.getTitle());

        adresseLabel.setText(location.getLocation()); // Set address label text
        descriptionLabel.setText(location.getDescription()); // Set description label text

        // Fetch the image for the selected location

        // Load the image using the URL
        javafx.scene.image.Image fxImage = new javafx.scene.image.Image(location.getImageUrl());
        locationImageView.setImage(fxImage);

        // Handle case where no image is available

    }


    @FXML
    void initialize() throws SQLException {
        // Fetch locations from the database
        EventServices locationService = new EventServices();
        List<Event> locations = locationService.afficher();

        // Initialize column and row counters
        int column = 0;
        int row = 1; // Start displaying images from the second row

        // Populate the grid with items representing each location
        for (Event location : locations) {
            // Fetch the associated image for the location
            // Assuming location.getId() gives the location ID

            if (location != null) {
                String imageUrl = location.getTitle();
                if (imageUrl != null) {
                    imageUrl = location.getImageUrl();
                    if (imageUrl != null) {
                        try {

                            javafx.scene.image.Image fxImage = new javafx.scene.image.Image(imageUrl);

                            // Create a new item controller for the location
                            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ItemEvent.fxml"));
                            AnchorPane itemPane;
                            try {
                                itemPane = fxmlLoader.load();
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            ItemEventController itemEventController = fxmlLoader.getController();

                            // Set the location information in the item controller
                            itemEventController.setLocation(location.getTitle(), fxImage);

                            // Set size constraints for the itemPane
                            itemPane.setPrefSize(300, 300); // Adjust size as needed

                            // Add event handler for item click
                            // Add event handler for item click
                            itemPane.setOnMouseClicked(event -> handleItemClick(event, location.getId())); // Pass location ID to handleItemClick

                            // Add the item to the grid
                            grid.add(itemPane, column++, row);
                            grid.setMinWidth(Region.USE_COMPUTED_SIZE);
                            grid.setPrefWidth(Region.USE_COMPUTED_SIZE);
                            grid.setMaxWidth(Region.USE_COMPUTED_SIZE);

                            grid.setMinHeight(Region.USE_COMPUTED_SIZE);
                            grid.setPrefHeight(Region.USE_COMPUTED_SIZE);
                            grid.setMaxHeight(Region.USE_COMPUTED_SIZE);
                        } catch (IllegalArgumentException e) {
                            System.out.println("Invalid image URL: " + imageUrl);
                            // Handle the case where the image URL is invalid
                        }
                    } else {
                        System.out.println("Image URL is null");
                        // Handle the case where the image URL is null
                    }
                } else {
                    System.out.println("Image URL is null");
                    // Handle the case where the image URL is null
                }
            } else {
                // Print "not available" in the console
                System.out.println("Image not available for location: " + location.getTitle());
            }


            // Check if column exceeds the maximum allowed (3)
            if (column == 3) {
                column = 0;
                row++;
            }
        }
    }
}


