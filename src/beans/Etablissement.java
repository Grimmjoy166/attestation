/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 *
 * @author Sinponzakra
 */
@Entity
public class Etablissement {
    @Id
    @GeneratedValue
    private int id;
    private String nom;
    private String type;
    private String region;
    @OneToMany(mappedBy = "etablissement")
    private List<Etudiant> etudiants;
    @OneToMany(mappedBy = "etablissement")
    private List<Employe> employes;
    private String telephone;
    private String codeEtablissement;
    private String ville;
    private String direction;
    
    
    public Etablissement() {
    }

    public Etablissement(int id, String nom, String type, String region, String telephone, String codeEtablissement, String ville, String direction) {
        this.id = id;
        this.nom = nom;
        this.type = type;
        this.region = region;
        this.telephone = telephone;
        this.codeEtablissement = codeEtablissement;
        this.ville = ville;
        this.direction = direction;
    }

    public Etablissement(String nom, String type, String region, String telephone, String codeEtablissement, String ville, String direction) {
        this.nom = nom;
        this.type = type;
        this.region = region;
        this.telephone = telephone;
        this.codeEtablissement = codeEtablissement;
        this.ville = ville;
        this.direction = direction;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public List<Etudiant> getEtudiants() {
        return etudiants;
    }

    public void setEtudiants(List<Etudiant> etudiants) {
        this.etudiants = etudiants;
    }

    public List<Employe> getEmployes() {
        return employes;
    }

    public void setEmployes(List<Employe> employes) {
        this.employes = employes;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getCodeEtablissement() {
        return codeEtablissement;
    }

    public void setCodeEtablissement(String codeEtablissement) {
        this.codeEtablissement = codeEtablissement;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }
    

    @Override
    public String toString() {
        return this.nom;
    }
    
    
    
    
    
    
    
}
