package main;

import java.util.List;

public interface Liste {
    public String getNom();
    public void setNom();
    public List<Composant> getComposants();
    public void ajouterComposant(Composant c);
    public void retirerComposant(Composant c);
}
