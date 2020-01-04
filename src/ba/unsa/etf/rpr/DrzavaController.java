package ba.unsa.etf.rpr;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

import static javax.lang.model.SourceVersion.isName;

public class DrzavaController {
    public TextField fieldNaziv;
    public ChoiceBox<Grad> choiceGrad;
    public Button btnOk;
    public Button btnCancel;

    private Drzava drzavaModel;
    public DrzavaController(Drzava model) {
        drzavaModel = model;
    }

    @FXML
    public void initialize() {
        GeografijaDAO dao = GeografijaDAO.getInstance();
        choiceGrad.setItems(FXCollections.observableArrayList(dao.gradovi()));

    }
//    private void azurirajPolja(GeografijaDAO dao) {
//        fieldNaziv.setText(dao.dodajGrad().getIme());
//    }

    public void validirajAction(ActionEvent actionEvent) {

        if (!fieldNaziv.getText().isEmpty()) {
                fieldNaziv.getStyleClass().removeAll("poljeNijeIspravno");
                fieldNaziv.getStyleClass().add("poljeIspravno");
            } else {
                fieldNaziv.getStyleClass().removeAll("poljeIspravno");
                fieldNaziv.getStyleClass().add("poljeNijeIspravno");
            }
//        fieldNaziv.textProperty().addListener((obs, oldIme, newIme) -> {
//            if (!newIme.isEmpty()) {
//                fieldNaziv.getStyleClass().removeAll("poljeNijeIspravno");
//                fieldNaziv.getStyleClass().add("poljeIspravno");
//            } else {
//                fieldNaziv.getStyleClass().removeAll("poljeIspravno");
//                fieldNaziv.getStyleClass().add("poljeNijeIspravno");
//            }
//        });
    }


    public void krajAction(ActionEvent actionEvent) {
        System.exit(0);
    }
}
