package Controllers;


import Entities.Programme;
import Services.ServiceProgramme;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.*;


public class ProgDetailController {
    @FXML
    private Label titleLabel, planLabel, placeLabel,  dateLabel;

    @FXML private TextField reservationPlacesField;

    private ServiceProgramme serviceProgramme = new ServiceProgramme();

    private Programme programme;


    public void setProg(Programme programme) {
        this.programme = programme;
        titleLabel.setText("Title: " + programme.getTitrepro());
        planLabel.setText("Plan: " + programme.getPlanpro());
        placeLabel.setText("Places dispo: " + programme.getPlacedispo());
        dateLabel.setText("Date: " + programme.getDatepro());
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
}
