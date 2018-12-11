/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Sinponzakra
 */
@Embeddable
public class AttestationPK implements Serializable{
    @Temporal(TemporalType.DATE)
    private Date dateEdition;
    private int employe_id;
    private int etudiant_id;

    public AttestationPK(Date dateEdition, int employe_id, int etudiant_id) {
        this.dateEdition = dateEdition;
        this.employe_id = employe_id;
        this.etudiant_id = etudiant_id;
    }

    public AttestationPK() {
    }

    public Date getDateEdition() {
        return dateEdition;
    }

    public void setDateEdition(Date dateEdition) {
        this.dateEdition = dateEdition;
    }

    public int getEmploye_id() {
        return employe_id;
    }

    public void setEmploye_id(int employe_id) {
        this.employe_id = employe_id;
    }

    public int getEtudiant_id() {
        return etudiant_id;
    }

    public void setEtudiant_id(int etudiant_id) {
        this.etudiant_id = etudiant_id;
    }

    @Override
    public String toString() {
        return this.dateEdition.toString();
    }
    
    
    
    
}
