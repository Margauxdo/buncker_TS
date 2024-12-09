package example.DTO;

import java.time.LocalDateTime;

public class ErrorResponseDTO {
    private String erreur; // Type de l'erreur
    private String message; // Message d'erreur détaillé
    private int statut; // Code HTTP
    private LocalDateTime horodatage; // Horodatage de l'erreur

    public ErrorResponseDTO(String erreur, String message, int statut) {
        this.erreur = erreur;
        this.message = message;
        this.statut = statut;
        this.horodatage = LocalDateTime.now();
    }

    // Getters et setters
    public String getErreur() {
        return erreur;
    }

    public void setErreur(String erreur) {
        this.erreur = erreur;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatut() {
        return statut;
    }

    public void setStatut(int statut) {
        this.statut = statut;
    }

    public LocalDateTime getHorodatage() {
        return horodatage;
    }

    public void setHorodatage(LocalDateTime horodatage) {
        this.horodatage = horodatage;
    }
}
