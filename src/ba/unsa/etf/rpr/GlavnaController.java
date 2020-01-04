package ba.unsa.etf.rpr;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;

public class GlavnaController {
    public TableView<Grad> tableViewGradovi;
    public TableColumn<Grad, Integer> colGradId;
    public TableColumn<Grad, String> colGradNaziv;
    public TableColumn<Grad, Integer> colGradStanovnika;
    public TableColumn<Grad, Drzava> colGradDrzava;
    public Button btnDodajGrad;
    public Button btnDodajDrzavu;
    public Button btnIzmijeniGrad;
    public Button btnObrisiGrad;
    GeografijaDAO dao = GeografijaDAO.getInstance();
    private ObservableList<Grad> gradovi = FXCollections.observableArrayList();

    // Metoda za potrebe testova, vraća bazu u polazno stanje
    public void resetujBazu() {
        GeografijaDAO.removeInstance();
        File dbfile = new File("baza.db");
        dbfile.delete();
        dao = GeografijaDAO.getInstance();
    }

    @FXML
    public void initialize() {
        gradovi.addAll(dao.gradovi());
        System.out.println(gradovi);
        tableViewGradovi.setItems(gradovi);
        colGradId.setCellValueFactory(new PropertyValueFactory<Grad, Integer>("id"));
        colGradNaziv.setCellValueFactory(new PropertyValueFactory<Grad, String>("naziv"));
        colGradStanovnika.setCellValueFactory(new PropertyValueFactory<Grad, Integer>("brojStanovnika"));
        colGradDrzava.setCellValueFactory(new PropertyValueFactory<Grad, Drzava>("drzava"));
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
        GradController ctrl = new GradController(model, dao.drzave());

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/grad.fxml"));
        loader.setController(ctrl);
        Parent root1 = loader.load();
        Stage stage = new Stage();
        stage.setTitle("Korisnici");
        stage.setScene(new Scene(root1, 400, 200));
        stage.setOnHiding(windowEvent -> {
            if(ctrl.getGrad() == null)
                System.out.println("Nista");
            else {
                dao.dodajGrad(ctrl.getGrad());
                gradovi.setAll(dao.gradovi());
            }
        });
        stage.show();
    }

    @FXML
    void izmijeniGradAction(ActionEvent actionEvent) throws IOException {
        Grad grad = tableViewGradovi.getSelectionModel().getSelectedItem();
        if(grad == null) return;
//        model.napuni();
        GradController ctrl = new GradController(grad, dao.drzave());

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/grad.fxml"));
        loader.setController(ctrl);
        Parent root1 = loader.load();
        Stage stage = new Stage();
        stage.setTitle("Grad");
        stage.setScene(new Scene(root1, 400, 200));
        stage.setOnHiding(windowEvent -> {
            Grad pomGrad = ctrl.getGrad();
            if(pomGrad == null)
                System.out.println("Nista");
            else {
                dao.izmijeniGrad(pomGrad);
                gradovi.setAll(dao.gradovi());
            }
        });
        stage.show();
    }
}
