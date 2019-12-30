package ba.unsa.etf.rpr;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

import java.util.regex.Pattern;

public class GradController {
    public TextField fieldNaziv;
    public TextField fieldBrojStanovnika;
    public ChoiceBox<Drzava> choiceDrzava;
    public Button btnOk;
    public Button btnCancel;

    private Grad gradModel;
    public GradController(Grad model) {
        gradModel = model;
    }

    public void validirajAction(ActionEvent actionEvent) {

        if (!fieldNaziv.getText().isEmpty()) {
            fieldNaziv.getStyleClass().removeAll("poljeNijeIspravno");
            fieldNaziv.getStyleClass().add("poljeIspravno");
        } else {
            fieldNaziv.getStyleClass().removeAll("poljeIspravno");
            fieldNaziv.getStyleClass().add("poljeNijeIspravno");
        }
        if (isStringInt(fieldBrojStanovnika.getText())) {
            fieldBrojStanovnika.getStyleClass().removeAll("poljeNijeIspravno");
            fieldBrojStanovnika.getStyleClass().add("poljeIspravno");
        } else {
            fieldBrojStanovnika.getStyleClass().removeAll("poljeIspravno");
            fieldBrojStanovnika.getStyleClass().add("poljeNijeIspravno");
        }
    }

    public boolean isStringInt(String s) {
        try
        {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException ex)
        {
            return false;
        }
    }

    public void krajAction(ActionEvent actionEvent) {
        System.exit(0);
    }
}
