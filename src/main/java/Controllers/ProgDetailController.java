package Controllers;

import Entities.Programme;
import Services.ServiceProgramme;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.ComboBox;
import javafx.util.StringConverter;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import javafx.application.Platform;
import org.json.JSONObject;

public class ProgDetailController {
    @FXML private Label titleLabel, planLabel, placeLabel, dateLabel;
    @FXML private Button translateButton;
    @FXML private TextField reservationPlacesField;
    @FXML private ComboBox<String> languageComboBox;

    private ServiceProgramme serviceProgramme = new ServiceProgramme();
    private Programme programme;
    private String originalTitle;
    private String originalPlan;
    private String originalPlaces;
    private String originalDate;
    @FXML
    public void initialize() {
        initializeComboBox();
        // Other initialization code (if any)...
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

    public void setProg(Programme programme) {
        this.programme = programme;
        originalTitle = programme.getTitrepro();
        originalPlan = programme.getPlanpro();
        originalPlaces = String.valueOf(programme.getPlacedispo());
        originalDate = programme.getDatepro().toString();
        titleLabel.setText(originalTitle);
        planLabel.setText(originalPlan);
        placeLabel.setText(originalPlaces);
        dateLabel.setText(originalDate);
    }

    @FXML
    private void handleTranslateAction() {
        String selectedLanguageCode = languageComboBox.getValue().split(" - ")[1];

        // Use original text fields for translation instead of the text from the labels
        translateText(originalTitle, "en", selectedLanguageCode).thenAccept(translatedText -> {
            Platform.runLater(() -> titleLabel.setText(translatedText));
        });
        translateText(originalPlan, "en", selectedLanguageCode).thenAccept(translatedText -> {
            Platform.runLater(() -> planLabel.setText(translatedText));
        });
        translateText(originalPlaces, "en", selectedLanguageCode).thenAccept(translatedText -> {
            Platform.runLater(() -> placeLabel.setText(translatedText));
        });
        // Note: The date typically doesn't need translation
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

