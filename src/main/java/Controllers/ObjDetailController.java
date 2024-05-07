package Controllers;

import Entities.Objectif;
import Entities.Programme;
import Services.ServiceObjectif;
import Services.ServiceProgramme;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;
import org.json.JSONObject;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public class ObjDetailController {
    @FXML
    private Label objectifLabel, descriptionLabel, weightLabel, heightLabel;
    @FXML private Button translateButton;
    @FXML private TextField reservationPlacesField;
    @FXML private ComboBox<String> languageComboBox;

    private ServiceObjectif serviceObjectif= new ServiceObjectif();
    private Objectif objectif;
    private String originalObjectif;
    private String originalDescription;
    private String originalWeight;
    private String originalHeight;
    @FXML
    public void initialize() {
        initializeComboBox();
    }

    public void initializeComboBox() {
        languageComboBox.getItems().addAll(
                "Italian - it",
                "French - fr",
                "Spanish - es",
                "German - de" // Add more languages as needed
        );
        languageComboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(String object) {
                return object.split(" - ")[0];
            }

            @Override
            public String fromString(String string) {
                return string.split(" - ")[1];
            }
        });
        languageComboBox.getSelectionModel().selectFirst(); // Select the first language by default
    }

    public void setObj(Objectif objectif) {
        this.objectif = objectif;
        originalObjectif = objectif.getObjectifO();
        originalDescription = objectif.getDescriptionO();
        originalWeight = String.valueOf(objectif.getPoidO());
        originalHeight=String.valueOf(objectif.getTailleO());
        objectifLabel.setText(originalObjectif);
        descriptionLabel.setText(originalDescription);
        weightLabel.setText(originalWeight);
        heightLabel.setText(originalHeight);
    }

    @FXML
    private void handleTranslateAction() {
        String selectedLanguageCode = languageComboBox.getValue().split(" - ")[1];

        // Use original text fields for translation instead of the text from the labels
        translateText(originalObjectif, "en", selectedLanguageCode).thenAccept(translatedText -> {
            Platform.runLater(() -> objectifLabel.setText(translatedText));
        });
        translateText(originalDescription, "en", selectedLanguageCode).thenAccept(translatedText -> {
            Platform.runLater(() -> descriptionLabel.setText(translatedText));
        });
        translateText(originalWeight, "en", selectedLanguageCode).thenAccept(translatedText -> {
            Platform.runLater(() -> weightLabel.setText(translatedText));
        });
        translateText(originalHeight, "en", selectedLanguageCode).thenAccept(translatedText -> {
            Platform.runLater(() -> heightLabel.setText(translatedText));
        });

    }


    private java.util.concurrent.CompletableFuture<String> translateText(String text, String fromLang, String toLang) {
        return java.util.concurrent.CompletableFuture.supplyAsync(() -> {
            try {
                String encodedText = URLEncoder.encode(text, StandardCharsets.UTF_8);
                String encodedLangPair = URLEncoder.encode(fromLang + "|" + toLang, StandardCharsets.UTF_8);
                String url = "https://api.mymemory.translated.net/get?q=" + encodedText + "&langpair=" + encodedLangPair + "&key=b54f43b43d24798b32b3";

                HttpRequest request = HttpRequest.newBuilder()
                        .uri(new URI(url))
                        .GET()
                        .build();

                HttpClient client = HttpClient.newHttpClient();
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

                return parseTranslation(response.body());
            } catch (Exception e) {
                e.printStackTrace();
                return "Translation error";
            }
        });
    }

    private String parseTranslation(String jsonResponse) {
        JSONObject jsonObject = new JSONObject(jsonResponse);
        return jsonObject.getJSONObject("responseData").getString("translatedText");
    }
}




//    @FXML
//    private void handleReserveAction() {
//
//        try {
//            int requestedPlaces = Integer.parseInt(reservationPlacesField.getText());
//            if (requestedPlaces > evenement.getPlaceDispo()) {
//                messageLabel.setText("Error: Not enough places available.");
//                return;
//            }
//
//            Reservation reservation = new Reservation(0, requestedPlaces, evenement);
//            serviceEvenement.addReservation(reservation);
//
//            // Updating UI
//            placesLabel.setText("Available Places: " + evenement.getPlaceDispo());
//            messageLabel.setText("Success: Reservation made.");
//            reservationPlacesField.clear();
//        } catch (NumberFormatException e) {
//            messageLabel.setText("Error: Please enter a valid number.");
//        } catch (Exception e) {
//            messageLabel.setText("An unexpected error occurred.");
//            e.printStackTrace();
//        }
//    }
