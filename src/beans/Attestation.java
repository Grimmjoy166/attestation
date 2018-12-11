
package beans;

import java.time.ZoneId;
import java.util.prefs.Preferences;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;



/**
 *
 * @author Sinponzakra
 */

@Entity
public class Attestation {
    
    @EmbeddedId
    private AttestationPK id;
    @JoinColumn(name = "employe_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne
    private Employe employe;
    @JoinColumn(name = "etudiant_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne
    private Etudiant etudiant;
    private int numero;
    private String code;

    public Attestation(AttestationPK id, Employe employe, Etudiant etudiant) {
        this.id = id;
        this.employe = employe;
        this.etudiant = etudiant;
        this.numero = Preferences.userRoot().getInt("nextNumero", 1);
        this.code = this.numero+"/"+this.id.getDateEdition().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getYear();
    }

    public Attestation() {
    }

    public AttestationPK getId() {
        return id;
    }

    public void setId(AttestationPK id) {
        this.id = id;
    }

    public Employe getEmploye() {
        return employe;
    }

    public void setEmploye(Employe employe) {
        this.employe = employe;
    }

    public Etudiant getEtudiant() {
        return etudiant;
    }

    public void setEtudiant(Etudiant etudiant) {
        this.etudiant = etudiant;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
    
    
}
