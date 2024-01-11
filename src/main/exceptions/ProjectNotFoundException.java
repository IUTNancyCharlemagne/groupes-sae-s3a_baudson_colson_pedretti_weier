package main.exceptions;

public class ProjectNotFoundException extends Exception{
    /**
     * Constructeur de ProjectNotFoundException
     */
    public ProjectNotFoundException() {
        super("Le projet n'a pas été trouvé");
    }
}
