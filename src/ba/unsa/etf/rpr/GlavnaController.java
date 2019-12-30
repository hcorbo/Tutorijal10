package ba.unsa.etf.rpr;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;

public class GlavnaController {
    public TableView<Grad> tableViewGradovi;
    public TableColumn colGradId;
    public TableColumn colGradNaziv;
    public TableColumn colGradStanovnika;
    public TableColumn colGradDrzava;
    public Button btnDodajGrad;
    public Button btnDodajDrzavu;
    public Button btnIzmijeniGrad;
    public Button btnObrisiGrad;

    // Metoda za potrebe testova, vraća bazu u polazno stanje
//    public void resetujBazu() {
//        GeografijaDAO.removeInstance();
//        File dbfile = new File("baza.db");
//        dbfile.delete();
//        dao = GeografijaDAO.getInstance();
//    }

    @FXML
    public void initialize() {
        GeografijaDAO dao = GeografijaDAO.getInstance();
    }

    @FXML
    void otvoriProzorAction(ActionEvent actionEvent) throws IOException {
        Drzava model = new Drzava();
//        model.napuni();
        DrzavaController ctrl = new DrzavaController(model);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/drzava.fxml"));
        loader.setController(ctrl);
        Parent root1 = loader.load();
        Stage stage = new Stage();
        stage.setTitle("Korisnici");
        stage.setScene(new Scene(root1, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
        stage.show();
    }

    @FXML
    void otvoriGradAction(ActionEvent actionEvent) throws IOException {
        Grad model = new Grad();
//        model.napuni();
        GradController ctrl = new GradController(model);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/grad.fxml"));
        loader.setController(ctrl);
        Parent root1 = loader.load();
        Stage stage = new Stage();
        stage.setTitle("Korisnici");
        stage.setScene(new Scene(root1, 400, 200));
        stage.show();
    }
}
