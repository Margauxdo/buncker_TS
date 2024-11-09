package example.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name= "client")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_client")
    private Integer id;

    @Column(nullable = false)
    @NotBlank(message = "Name is required")
    private String name;
    private String adresse;
    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is required")
    @Column(nullable = false, unique = true)
    private String email;
    private String telephoneExploitation;
    private String ville;
    private String PersonnelEtFonction;

    private String ramassage1;
    private String ramassage2;
    private String ramassage3;
    private String ramassage4;
    private String ramassage5;
    private String ramassage6;
    private String ramassage7;
    private String EnvoiparDefaut;
    private String MemoRetourSecurite1;
    private String MemoRetourSecurite2;
    private String typeSuivie; //TODO:? verifier//

    public Client(int i, String s, String s1, String mail, String number, String s2, Object o, Object o1, Object o2, Object o3, Object o4, Object o5, Object o6, Object o7, Object o8, Object o9, Object o10, Object o11) {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephoneExploitation() {
        return telephoneExploitation;
    }

    public void setTelephoneExploitation(String telephoneExploitation) {
        this.telephoneExploitation = telephoneExploitation;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getPersonnelEtFonction() {
        return PersonnelEtFonction;
    }

    public void setPersonnelEtFonction(String personnelEtFonction) {
        PersonnelEtFonction = personnelEtFonction;
    }

    public String getRamassage1() {
        return ramassage1;
    }

    public void setRamassage1(String ramassage1) {
        this.ramassage1 = ramassage1;
    }

    public String getRamassage2() {
        return ramassage2;
    }

    public void setRamassage2(String ramassage2) {
        this.ramassage2 = ramassage2;
    }

    public String getRamassage3() {
        return ramassage3;
    }

    public void setRamassage3(String ramassage3) {
        this.ramassage3 = ramassage3;
    }

    public String getRamassage4() {
        return ramassage4;
    }

    public void setRamassage4(String ramassage4) {
        this.ramassage4 = ramassage4;
    }

    public String getRamassage5() {
        return ramassage5;
    }

    public void setRamassage5(String ramassage5) {
        this.ramassage5 = ramassage5;
    }

    public String getRamassage6() {
        return ramassage6;
    }

    public void setRamassage6(String ramassage6) {
        this.ramassage6 = ramassage6;
    }

    public String getRamassage7() {
        return ramassage7;
    }

    public void setRamassage7(String ramassage7) {
        this.ramassage7 = ramassage7;
    }

    public String getEnvoiparDefaut() {
        return EnvoiparDefaut;
    }

    public void setEnvoiparDefaut(String envoiparDefaut) {
        EnvoiparDefaut = envoiparDefaut;
    }

    public String getMemoRetourSecurité1() {
        return MemoRetourSecurite1;
    }

    public void setMemoRetourSecurité1(String memoRetourSecurité1) {
        MemoRetourSecurite1 = memoRetourSecurité1;
    }

    public String getMemoRetourSecurité2() {
        return MemoRetourSecurite2;
    }

    public void setMemoRetourSecurité2(String memoRetourSecurité2) {
        MemoRetourSecurite2 = memoRetourSecurité2;
    }

    public String getTypeSuivie() {
        return typeSuivie;
    }

    public void setTypeSuivie(String typeSuivie) {
        this.typeSuivie = typeSuivie;
    }

    public String getCodeClient() {
        return codeClient;
    }

    public void setCodeClient(String codeClient) {
        this.codeClient = codeClient;
    }

    public List<Valise> getValises() {
        return valises;
    }

    public void setValises(List<Valise> valises) {
        this.valises = valises;
    }

    public List<Probleme> getProblemes() {
        return problemes;
    }

    public void setProblemes(List<Probleme> problemes) {
        this.problemes = problemes;
    }

    public List<RetourSecurite> getRetourSecuriteList() {
        return retourSecuriteList;
    }

    public void setRetourSecuriteList(List<RetourSecurite> retourSecuriteList) {
        this.retourSecuriteList = retourSecuriteList;
    }

    private String codeClient;//TODO:? verifier//

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Valise> valises = new ArrayList<>();



    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    private List<Probleme> problemes;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<RetourSecurite> retourSecuriteList = new ArrayList<>();




}
