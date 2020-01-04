package ba.unsa.etf.rpr;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

import static javafx.application.Application.launch;
import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;

public class Main extends Application {

    private static GeografijaDAO podaci;
    static {
        podaci = GeografijaDAO.getInstance();
    }
//    @Override
//    public void start(Stage primaryStage) throws Exception{
//        GlavnaController g = new GlavnaController();
//        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/glavna.fxml"));
//        Parent root = loader.load();
//        primaryStage.setTitle("Korisnici");
//        primaryStage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
//        primaryStage.show();
//        //primaryStage.setResizable(false);
//    }
        @Override
    public void start(Stage primaryStage) throws Exception{
        GeografijaDAO.removeInstance();
        File dbfile = new File("baza.db");
        dbfile.delete();
        GeografijaDAO geo = GeografijaDAO.getInstance();
        GlavnaController ctrl = new GlavnaController();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/glavna.fxml"));
        loader.setController(ctrl);
        Parent root = loader.load();
        primaryStage.setTitle("Fakultet");
        primaryStage.setScene(new Scene(root, 400, 300));
        primaryStage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }

//    @Override
//    public void start(Stage primaryStage) throws Exception{
//        Parent root = FXMLLoader.load(getClass().getResource("/fxml/glavna.fxml"));
//        primaryStage.setTitle("Hello World");
//        primaryStage.setScene(new Scene(root, 300, 275));
//        primaryStage.show();
//    }
//    @Override
//    public void start(Stage primaryStage) throws Exception{
//        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/glavna.fxml"));
//        StudentController ctrl = new StudentController();
//        loader.setController(ctrl);
//        Parent root = loader.load();
//        primaryStage.setTitle("Fakultet");
//        primaryStage.setScene(new Scene(root, 300, 200));
//        primaryStage.show();
//    }


//public class Main {
//
//}
//    public static void glavniGrad() {
//        System.out.println("Unesite ime drzave: ");
//        Scanner ulaz = new Scanner(System.in);
//        String drzava = ulaz.next();
//        Grad grad = GeografijaDAO.getInstance().glavniGrad(drzava);
//
//        if(grad == null) {
//            System.out.println("Nepostojeća država");
//            return;
//        }
//
//        System.out.println("Glavni grad države " + drzava + " je " + grad.getNaziv() + ".");
//    }
//
    public static String ispisiGradove() {
        ArrayList<Grad> gradovi = GeografijaDAO.getInstance().gradovi();
        String rezultat = "";
        for(int i=0; i<gradovi.size(); i++){
            Grad g = gradovi.get(i);
            rezultat = rezultat + g.getNaziv() + " (";
            if (g.getDrzava() != null) {
                rezultat = rezultat + g.getDrzava().getNaziv();
            } else { rezultat = rezultat + "nema drzave"; }
            rezultat = rezultat + ") - " + g.getBrojStanovnika() + "\n";
        }
//        for(Grad g : gradovi) {
//            rezultat = g.getNaziv() + " (";
//            if (g.getDrzava() != null) {
//                rezultat = rezultat + g.getDrzava().getNaziv();
//            } else { rezultat = rezultat + "nema drzave"; }
//            rezultat = rezultat + ") - " + g.getBrojStanovnika();
//            rezultat +="\n";
//        }
        return rezultat;
    }
}
//
//    public static void main(String[] args) {
//        launch(args);
//    }
//}
