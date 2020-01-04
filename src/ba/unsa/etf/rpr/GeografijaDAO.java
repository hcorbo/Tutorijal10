package ba.unsa.etf.rpr;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;


public class GeografijaDAO {
    private static GeografijaDAO instance = null;
    private Connection conn;

    private PreparedStatement dajGradoveUpit, dajDrzaveUpit, dajGradoveUpit2, glavniGradUpit, dajDrzavuUpit, obrisiDrzavuUpit, nadjiDrzavuUpit, nadjiDrzavu1,
            obrisiGradoveZaDrzavuUpit, dodajGradUpit, odrediIdGradaUpit, odrediIdDrzaveUpit, dodajDrzavuUpit, promijeniGradUpit, pomDrzavaUpit,
            obrisiGradUpit, dodajDrzavuUpit2, nadjiGradUpit;
    private GeografijaDAO() {
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:baza.db");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            glavniGradUpit = conn.prepareStatement("SELECT grad.id, grad.naziv, grad.broj_stanovnika, grad.drzava FROM grad, drzava WHERE grad.drzava = drzava.id AND drzava.naziv = ?");
        } catch (SQLException e) {
            regenerisiBazu();
            try {
                glavniGradUpit = conn.prepareStatement("SELECT grad.id, grad.naziv, grad.broj_stanovnika, grad.drzava FROM grad, drzava WHERE grad.drzava = drzava.id AND drzava.naziv = ?");
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

        }
        try {
            dajDrzavuUpit = conn.prepareStatement("SELECT * FROM drzava WHERE id=?");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            obrisiDrzavuUpit = conn.prepareStatement("DELETE FROM drzava WHERE naziv=?");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            obrisiGradUpit = conn.prepareStatement("DELETE FROM grad WHERE naziv=?");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            dajGradoveUpit = conn.prepareStatement("SELECT * FROM grad ORDER BY broj_stanovnika DESC");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            dajGradoveUpit2 = conn.prepareStatement( "SELECT g.id, g.naziv, g.broj_stanovnika, g.drzava FROM grad g ORDER BY g.broj_stanovnika DESC" );
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            nadjiDrzavuUpit = conn.prepareStatement("SELECT * FROM drzava WHERE naziv=?");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            obrisiGradoveZaDrzavuUpit = conn.prepareStatement("DELETE FROM grad WHERE drzava=?");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            dodajGradUpit = conn.prepareStatement("INSERT INTO grad VALUES(?,?,?,?)");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            odrediIdGradaUpit = conn.prepareStatement("SELECT max(id)+1 FROM grad");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            odrediIdDrzaveUpit = conn.prepareStatement("SELECT max(id)+1 FROM drzava");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            dodajDrzavuUpit = conn.prepareStatement("INSERT INTO drzava VALUES(?,?,?)");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            promijeniGradUpit = conn.prepareStatement("UPDATE grad SET naziv=?, broj_stanovnika=?, drzava=? WHERE id=?");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            nadjiDrzavu1 = conn.prepareStatement("SELECT d.id, d.naziv, d.glavni_grad, g.id, g.naziv, g.broj_stanovnika, g.drzava FROM drzava d, grad g WHERE g.id = d.glavni_grad AND d.naziv=?");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            pomDrzavaUpit = conn.prepareStatement("SELECT d.id, d.naziv, d.glavni_grad FROM drzava d WHERE d.id = ?");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            dajDrzaveUpit = conn.prepareStatement("SELECT d.id, d.naziv, d.glavni_grad, g.id, g.naziv, g.broj_stanovnika, g.drzava FROM drzava d, grad g WHERE d.glavni_grad = g.id");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            nadjiGradUpit = conn.prepareStatement("SELECT g.id, g.naziv, g.broj_stanovnika, g.drzava, d.id, d.naziv, d.glavni_grad " +
                    "FROM drzava d, grad g " +
                    "WHERE g.id = d.glavni_grad AND g.naziv=?");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // dodajDrzavuUpit2 = conn.prepareStatement("")
    }

    private void regenerisiBazu() {
        Scanner ulaz = null;
        try {
            ulaz = new Scanner(new FileInputStream("baza.db.sql"));
            String sqlUpit = "";
            while (ulaz.hasNext()) {
                sqlUpit += ulaz.nextLine();
                if ( sqlUpit.length() > 1 && sqlUpit.charAt( sqlUpit.length()-1 ) == ';') {
                    try {
                        Statement stmt = conn.createStatement();
                        stmt.execute(sqlUpit);
                        sqlUpit = "";
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
            ulaz.close();
    } catch (FileNotFoundException e) {
        System.out.println("Ne postoji SQL datoteka… nastavljam sa praznom bazom");
        }
    }

    public static GeografijaDAO getInstance() {
        if(instance == null) instance = new GeografijaDAO();
        return instance;
    }

    public static void removeInstance() {
        if (instance != null) {
            try {
                instance.conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        instance = null;
    }

    public Grad glavniGrad(String drzava){
        try {
            glavniGradUpit.setString(1, drzava);
            ResultSet rs = glavniGradUpit.executeQuery();
            if(!rs.next()) return null;
            return dajGradIzResultSeta(rs);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Grad dajGradIzResultSeta(ResultSet rs) throws SQLException {
        Grad grad = new Grad(rs.getInt(1), rs.getString(2), rs.getInt(3), null);
        grad.setDrzava(dajDrzavu(rs.getInt(4), grad));
        return grad;
    }

    private Drzava dajDrzavu(int id, Grad grad) {
        try {
            dajDrzavuUpit.setInt(1, id);
            ResultSet rs = dajDrzavuUpit.executeQuery();
            if(!rs.next()) return null;
            return dajDrzavuIzResultSeta(rs, grad);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Drzava dajDrzavuIzResultSeta(ResultSet rs, Grad grad) throws SQLException {
        Drzava drzava = new Drzava(rs.getInt(1), rs.getString(2), grad);
        return drzava;
    }

    public ArrayList<Grad> gradovi2(){
        ArrayList<Grad> rezultat = new ArrayList<>();
        try {
         ResultSet rs = dajGradoveUpit.executeQuery();
         while(rs.next()){
             Grad grad = dajGradIzResultSeta(rs);
             rezultat.add(grad);
         }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rezultat;
    }

    public ArrayList<Grad> gradovi() {
        ArrayList<Grad> rezultat = new ArrayList<>();
        ResultSet rs = null;
        try {
            rs = dajGradoveUpit2.executeQuery();
            while(rs.next()){
                Integer idDrzave = rs.getInt(4);
                pomDrzavaUpit.setInt(1, idDrzave);
                ResultSet rsDrzava = pomDrzavaUpit.executeQuery();
                Drzava d = new Drzava(rsDrzava.getInt(1), rsDrzava.getString(2), glavniGrad(rsDrzava.getString(2)));
                Grad g = new Grad(rs.getInt(1), rs.getString(2), rs.getInt(3), d);
                rezultat.add(g);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rezultat;
    }

    public void obrisiDrzavu(String nazivDrzave){
        try {
            nadjiDrzavuUpit.setString(1, nazivDrzave);
            ResultSet rs = nadjiDrzavuUpit.executeQuery();
            if(!rs.next()) return;
            Drzava drzava = dajDrzavuIzResultSeta(rs, null);

            obrisiGradoveZaDrzavuUpit.setInt(1, drzava.getId());
            obrisiGradoveZaDrzavuUpit.executeUpdate();

            obrisiDrzavuUpit.setInt(1, drzava.getId());
            obrisiDrzavuUpit.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Drzava nadjiDrzavu(String nazivDrzave){
        Drzava drzava = null;
        try {
            nadjiDrzavu1.clearParameters();
            nadjiDrzavu1.setString(1, nazivDrzave);
            ResultSet rs = nadjiDrzavu1.executeQuery();
            if(!rs.next()) return null;
            drzava = new Drzava();
            drzava.setId(rs.getInt(1));
            drzava.setNaziv(rs.getString(2));
           // "SELECT d.id, d.naziv, d.glavni_grad, g.id, g.naziv, g.broj_stnovnika, g.drzava FROM drzava d, grad g WHERE g.id = d.glavni_gard AND d.naziv=?");
            Grad g = glavniGrad(nazivDrzave);
            drzava.setGlavniGrad(g);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return drzava;
    }

    public void dodajGrad(Grad grad) {
        try {
            ResultSet rs = odrediIdGradaUpit.executeQuery();
            int id = 1;
            if(rs.next()){
                id = rs.getInt(1);
            }
            dodajGradUpit.setInt(1, id);
            dodajGradUpit.setString(2, grad.getNaziv());
            dodajGradUpit.setInt(3, grad.getBrojStanovnika());
            if(grad.getDrzava()!=null)
            dodajGradUpit.setInt(4, grad.getDrzava().getId());
            else dodajGradUpit.setInt(4, 1);
            dodajGradUpit.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void dodajDrzavu(Drzava drzava) {
        try {
            dodajDrzavuUpit.setInt(1, drzava.getId());
            dodajDrzavuUpit.setString(2, drzava.getNaziv());
            dodajDrzavuUpit.setInt(3, drzava.getGlavniGrad().getId());
            dodajDrzavuUpit.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
//    public void dodajDrzavu2(String naziv, Grad glavniGrad) {
//        try {
//            ResultSet rs = odrediIdDrzaveUpit.executeQuery();
//            int id = 1;
//            if(rs.next()){
//                id = rs.getInt(1);
//            }
//            dodajDrzavuUpit2.setInt(1, id);
//            dodajDrzavuUpit2.setString(2, naziv);
//            dodajDrzavuUpit2.setObject(3, glavniGrad);
//
//            dodajDrzavuUpit2.executeUpdate();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
    public void izmijeniGrad(Grad grad) {
        try {
            promijeniGradUpit.setString(1, grad.getNaziv());
            promijeniGradUpit.setInt(3, grad.getDrzava().getId());
            promijeniGradUpit.setInt(2, grad.getBrojStanovnika());
            promijeniGradUpit.setInt(4, grad.getId());
            promijeniGradUpit.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Drzava> drzave() {
        ArrayList<Drzava> rezultat = new ArrayList<>();
        ResultSet rs = null;
        try {
            rs = dajDrzaveUpit.executeQuery();
            while(rs.next()){
                Integer idDrzave = rs.getInt(1);
                String nazivDrzave = rs.getString(2);
                Drzava d = new Drzava(rs.getInt(1), nazivDrzave, glavniGrad(rs.getString(2)));
                //Grad g = new Grad(rs.getInt(4), rs.getString(5), rs.getInt(6), null);
                //Drzava d1 = dajDrzavu(rs.getInt(4), g);
                System.out.println(d.getNaziv());
                rezultat.add(d);

//                pomDrzavaUpit.setInt(1, idDrzave);
//                ResultSet rsDrzava = pomDrzavaUpit.executeQuery();
//                Drzava d = new Drzava(rsDrzava.getInt(1), rsDrzava.getString(2), glavniGrad(rsDrzava.getString(2)));
//                Grad g = new Grad(rs.getInt(1), rs.getString(2), rs.getInt(3), d);
//                rezultat.add(d);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rezultat;
    }

    public void obrisiGrad(Grad g){
        try {
            obrisiGradUpit.setString(1, g.getNaziv());
            obrisiGradUpit.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public Grad nadjiGrad(String nazivGrada){
        try {
            nadjiGradUpit.setString(1, nazivGrada);
            ResultSet rs = nadjiGradUpit.executeQuery();
            Drzava d = dajDrzavu(rs.getInt(5), glavniGrad(rs.getString(6)));
            Grad rezultat = new Grad(rs.getInt(1), rs.getString(2), rs.getInt(3), d);
            return rezultat;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
