package ba.unsa.etf.rpr;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class GradController {
    public TextField fieldNaziv;
    public TextField fieldBrojStanovnika;
    public ChoiceBox<Drzava> choiceDrzava;
    public Button btnOk;
    public Button btnCancel;

    private Grad gradModel;
    private Grad grad = null;
    public GradController(Grad model, ArrayList<Drzava> listaDrzava) {
        gradModel = model;
//        GeografijaDAO dao = GeografijaDAO.getInstance();
//        choiceDrzava.setItems(FXCollections.observableArrayList(dao.drzave()));
        //choiceDrzava.setItems(FXCollections.observableArrayList(listaDrzava));
    }

    public void azurirajPolja(Grad p) {
        fieldNaziv.setText(p.getNaziv());
        fieldBrojStanovnika.setText(String.valueOf(p.getBrojStanovnika()));
        choiceDrzava.setValue(p.getDrzava());
    }
    @FXML
    public void initialize() {
        GeografijaDAO dao = GeografijaDAO.getInstance();
        choiceDrzava.setItems(FXCollections.observableArrayList(dao.drzave()));
    }
//    @FXML
//    public void initialize() {
//        GeografijaDAO dao = GeografijaDAO.getInstance();
//        choiceDrzava.setItems(dao.());
//
//        azurirajPolja(dao);
//    }
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
        if(!fieldNaziv.getText().isEmpty() && isStringInt(fieldBrojStanovnika.getText())){
            grad = new Grad();
            grad.setNaziv(fieldNaziv.getText());
            grad.setBrojStanovnika(Integer.parseInt(fieldBrojStanovnika.getText()));
            grad.setDrzava(choiceDrzava.getSelectionModel().getSelectedItem());
//            ((Node) (actionEvent.getSource())).getScene().getWindow().hide();
            Stage stage = (Stage) btnOk.getScene().getWindow();
            stage.close();
        }
    }

//    public void izmijeniGradAction(ActionEvent actionEvent) {
//
//        if (!fieldNaziv.getText().isEmpty()) {
//            fieldNaziv.getStyleClass().removeAll("poljeNijeIspravno");
//            fieldNaziv.getStyleClass().add("poljeIspravno");
//        } else {
//            fieldNaziv.getStyleClass().removeAll("poljeIspravno");
//            fieldNaziv.getStyleClass().add("poljeNijeIspravno");
//        }
//        if (isStringInt(fieldBrojStanovnika.getText())) {
//            fieldBrojStanovnika.getStyleClass().removeAll("poljeNijeIspravno");
//            fieldBrojStanovnika.getStyleClass().add("poljeIspravno");
//        } else {
//            fieldBrojStanovnika.getStyleClass().removeAll("poljeIspravno");
//            fieldBrojStanovnika.getStyleClass().add("poljeNijeIspravno");
//        }
//        if(!fieldNaziv.getText().isEmpty() && isStringInt(fieldBrojStanovnika.getText())){
//            grad = new Grad();
//            grad.setNaziv(fieldNaziv.getText());
//            grad.setBrojStanovnika(Integer.parseInt(fieldBrojStanovnika.getText()));
//            grad.setDrzava(choiceDrzava.getSelectionModel().getSelectedItem());
////            ((Node) (actionEvent.getSource())).getScene().getWindow().hide();
//            Stage stage = (Stage) btnOk.getScene().getWindow();
//            stage.close();
//        }
//    }
    public boolean isStringInt(String s) {
        try
        {
            Integer.parseInt(s);
            if (Integer.parseInt(s) < 0) return false;
            return true;
        } catch (NumberFormatException ex)
        {
            return false;
        }
    }

    public void krajAction(ActionEvent actionEvent) {
        System.exit(0);
    }

    public Grad getGrad() {
        return grad;
    }
}
