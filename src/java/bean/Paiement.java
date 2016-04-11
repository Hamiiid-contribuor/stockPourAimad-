/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Temporal;

/**
 *
 * @author Younes
 */
@Entity
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)

public class Paiement implements Serializable {

    protected static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;
    protected BigDecimal montant;
    protected int mode; // 1=espece,2=cheque,3=letttre de change
    protected String description;
    protected String numeroEffet;
    protected String photoEffet;
    @Temporal(javax.persistence.TemporalType.DATE)
    protected Date dateEcheance;
    @Temporal(javax.persistence.TemporalType.DATE)
    protected Date datePaiement;
    protected boolean encaisser;
    @Temporal(javax.persistence.TemporalType.DATE)
    protected Date dateEncaissement;
   

    public Date getDateEncaissement() {
        return dateEncaissement;
    }

    public String getPhotoEffet() {
        return photoEffet;
    }

    public void setPhotoEffet(String photoEffet) {
        this.photoEffet = photoEffet;
    }

    public void setDateEncaissement(Date dateEncaissement) {
        this.dateEncaissement = dateEncaissement;
    }

    public boolean getEncaisser() {
        return encaisser;
    }

    public void setEncaisser(boolean encaisser) {
        this.encaisser = encaisser;
    }

    public BigDecimal getMontant() {
        if (montant == null) {
            montant = new BigDecimal(0);
        }
        return montant;
    }

    public void setMontant(BigDecimal montant) {
        this.montant = montant;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDateEcheance() {
        return dateEcheance;
    }

    public void setDateEcheance(Date dateEcheance) {
        this.dateEcheance = dateEcheance;
    }

    public Date getDatePaiement() {
        return datePaiement;
    }

    public void setDatePaiement(Date datePaiement) {
        this.datePaiement = datePaiement;
    }

    public String getNumeroEffet() {
        return numeroEffet;
    }

    public void setNumeroEffet(String numeroEffet) {
        this.numeroEffet = numeroEffet;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Paiement)) {
            return false;
        }
        Paiement other = (Paiement) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "bean.Paiement[ id=" + id + " ]";
    }

}
