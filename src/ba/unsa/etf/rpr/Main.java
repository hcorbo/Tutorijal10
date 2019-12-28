package ba.unsa.etf.rpr;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void glavniGrad() {
        System.out.println("Unesite ime drzave: ");
        Scanner ulaz = new Scanner(System.in);
        String drzava = ulaz.next();
        Grad grad = GeografijaDAO.getInstance().glavniGrad(drzava);

        if(grad == null) {
            System.out.println("Nepostojeća država");
            return;
        }

        System.out.println("Glavni grad države " + drzava + " je " + grad.getNaziv() + ".");
    }

    public static String ispisiGradove() {
        ArrayList<Grad> gradovi = GeografijaDAO.getInstance().gradovi();
        String rezultat = "";
        for(Grad g : gradovi) {
            rezultat += g + "\n";
        }
        return rezultat;
    }

    public static void main(String[] args) {
        System.out.println("Gradovi su:\n" + ispisiGradove());
        glavniGrad();
        GeografijaDAO.removeInstance();
    }
}
