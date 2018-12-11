/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Sinponzakra
 */
@Entity
public class Etudiant {
    @Id
    @GeneratedValue
    private int id;
    private String nomComplet;
    @Temporal(TemporalType.DATE)
    private Date dateNaissance;
    private String lieuNaissance;
    private String cne;
    private String niveauEtude;
    private String numInscription;
    private String decision;
    private int numDossier;
    @Temporal(TemporalType.DATE)
    private Date dateSortie;
    @ManyToOne
    private Etablissement etablissement;

    public Etudiant() {
    }

    public Etudiant(int id,String nomComplet, Date dateNaissance, String lieuNaissance, String cne, String niveauEtude, String numInscription, String decision, int numDossier, Date dateSortie, Etablissement etablissement) {
        this.id = id;
        this.nomComplet = nomComplet;
        this.dateNaissance = dateNaissance;
        this.lieuNaissance = lieuNaissance;
        this.cne = cne;
        this.niveauEtude = niveauEtude;
        this.numInscription = numInscription;
        this.decision = decision;
        this.numDossier = numDossier;
        this.dateSortie = dateSortie;
        this.etablissement = etablissement;
    }

    public Etudiant(String nomComplet, Date dateNaissance, String lieuNaissance, String cne, String niveauEtude,String numInscription , String decision, int numDossier, Date dateSortie, Etablissement etablissement) {
        this.nomComplet = nomComplet;
        this.dateNaissance = dateNaissance;
        this.lieuNaissance = lieuNaissance;
        this.cne = cne;
        this.niveauEtude = niveauEtude;
        this.numInscription = numInscription;
        this.decision = decision;
        this.numDossier = numDossier;
        this.dateSortie = dateSortie;
        this.etablissement = etablissement;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNomComplet() {
        return nomComplet;
    }

    public void setNomComplet(String nomComplet) {
        this.nomComplet = nomComplet;
    }

    public Date getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(Date dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public String getLieuNaissance() {
        return lieuNaissance;
    }

    public void setLieuNaissance(String lieuNaissance) {
        this.lieuNaissance = lieuNaissance;
    }

    public String getCne() {
        return cne;
    }

    public void setCne(String cne) {
        this.cne = cne;
    }

    public String getNiveauEtude() {
        return niveauEtude;
    }

    public void setNiveauEtude(String niveauEtude) {
        this.niveauEtude = niveauEtude;
    }

    public String getNumInscription() {
        return numInscription;
    }

    public void setNumInscription(String numInscription) {
        this.numInscription = numInscription;
    }

    public String getDecision() {
        return decision;
    }

    public void setDecision(String decision) {
        this.decision = decision;
    }

    public int getNumDossier() {
        return numDossier;
    }

    public void setNumDossier(int numDossier) {
        this.numDossier = numDossier;
    }

    public Date getDateSortie() {
        return dateSortie;
    }

    public void setDateSortie(Date dateSortie) {
        this.dateSortie = dateSortie;
    }
    
    public Etablissement getEtablissement() {
        return etablissement;
    }

    public void setEtablissement(Etablissement etablissement) {
        this.etablissement = etablissement;
    }

    @Override
    public String toString() {
        return this.nomComplet;
    }
    
    
    
}
