import java.util.List;

public abstract class Composant {
    private String nom;
    private String description;
    private boolean estArchive;
    private List<Tag> tags;

    public abstract void operation();

    public String getNom() {
        return nom;
    }

    public String getDescription() {
        return description;
    }

    public boolean getEstArchive() {
        return estArchive;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setEstArchive(boolean estArchive) {
        this.estArchive = estArchive;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void addTag(Tag tag){
        tags.add(tag);
    }

    public void removeTag(Tag tag){
        tags.remove(tag);
    }
}
