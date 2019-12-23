package ba.unsa.etf.rpr;

import java.sql.*;
import java.util.ArrayList;

public class GeografijaDAO {
    private static GeografijaDAO instance = null;
    private Connection conn;

    private PreparedStatement dajGradoveUpit, glavniGradUpit, dajDrzavuUpit, obrisiDrzavuUpit, nadjiDrzavuUpit,
            obrisiGradoveZaDrzavuUpit, dodajGradUpit, odrediIdGradaUpit, dodajDrzavuUpit, odrediIdDrzaveUpit, promijeniGradUpit;
    private GeografijaDAO() {
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:baza.db");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            glavniGradUpit = conn.prepareStatement("SELECT grad.id, grad.naziv, grad.broj_stanovnika, grad.drzava FROM grad, drzava WHERE grad.drzava = drzava.id AND drzava.glavni_grad = ?");
        } catch (SQLException e) {
            e.printStackTrace();
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
            dajGradoveUpit = conn.prepareStatement("SELECT * FROM grad ORDER BY broj_stanovnika DESC");
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
            dodajDrzavuUpit = conn.prepareStatement("INSERT INTO drzava VALUES(?,?,?)");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            odrediIdDrzaveUpit = conn.prepareStatement("SELECT max(id)+1 FROM drzava");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            promijeniGradUpit = conn.prepareStatement("UPDATE grad SET naziv=?, broj_stanovnika=?, drzava=? WHERE id=?");
        } catch (SQLException e) {
            e.printStackTrace();
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

    public ArrayList<Grad> gradovi(){
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

   // public Drzava nadjiDrzavu(String drzava) {
   // }

    public void dodajGrad(Grad grad) {
        try {
            ResultSet rs = odrediIdGradaUpit.executeQuery();
            int id = 1;
            if(rs.next()){
                id = rs.getInt(1); //zasto get int
            }
            dodajGradUpit.setInt(1, id);
            dodajGradUpit.setString(2, grad.getNaziv());
            dodajGradUpit.setInt(3, grad.getBrojStanovnika());
            dodajGradUpit.setInt(4, grad.getDrzava().getId());
            dodajGradUpit.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void dodajDrzavu(Drzava drzava) {
        try {
            ResultSet rs = odrediIdDrzaveUpit.executeQuery();
            int id = 1;
            if(rs.next()){
                id = rs.getInt(1); //zasto get int
            }
            dodajDrzavuUpit.setInt(1, id);
            dodajDrzavuUpit.setString(2, drzava.getNaziv());
            dodajDrzavuUpit.setInt(3, drzava.getGlavniGrad().getId());
            dodajDrzavuUpit.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

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


}
