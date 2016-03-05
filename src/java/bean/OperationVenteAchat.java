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
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Temporal;

/**
 *
 * @author Younes
 */
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class OperationVenteAchat implements Serializable {

    protected static final long serialVersionUID = 1L;
    @Id
    protected Long id;
    protected Long referenceIndex;
    protected String reference;
    protected String referenceSuffix;
    protected String referencePriffix;
    protected String commentaire;
    @Temporal(javax.persistence.TemporalType.DATE)
    protected Date dateOperation;
    protected BigDecimal montantTotal;
    protected BigDecimal paiement;
    protected BigDecimal paiementEffetEnCour;
    protected BigDecimal tva = new BigDecimal(20);

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getReferenceIndex() {
         if (referenceIndex == null) {
            referenceIndex = new Long(0);
        }
        return referenceIndex;
    }

    public void setReferenceIndex(Long referenceIndex) {
        this.referenceIndex = referenceIndex;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getReferenceSuffix() {
        return referenceSuffix;
    }

    public void setReferenceSuffix(String referenceSuffix) {
        this.referenceSuffix = referenceSuffix;
    }

    public String getReferencePriffix() {
        return referencePriffix;
    }

    public void setReferencePriffix(String referencePriffix) {
        this.referencePriffix = referencePriffix;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public Date getDateOperation() {
        return dateOperation;
    }

    public void setDateOperation(Date dateOperation) {
        this.dateOperation = dateOperation;
    }

    public BigDecimal getMontantTotal() {
         if (montantTotal == null) {
            montantTotal = new BigDecimal(0);
        }
        return montantTotal;
    }

    public void setMontantTotal(BigDecimal montantTotal) {
        this.montantTotal = montantTotal;
    }

    public BigDecimal getPaiement() {
          if (paiement == null) {
            paiement = new BigDecimal(0);
        }
        return paiement;
    }

    public void setPaiement(BigDecimal paiement) {
        this.paiement = paiement;
    }

    public BigDecimal getPaiementEffetEnCour() {
         if (paiementEffetEnCour == null) {
            paiementEffetEnCour = new BigDecimal(0);
        }
        return paiementEffetEnCour;
    }

    public void setPaiementEffetEnCour(BigDecimal paiementEffetEnCour) {
        this.paiementEffetEnCour = paiementEffetEnCour;
    }

    public BigDecimal getTva() {
         if (tva == null) {
            tva = new BigDecimal(0);
        }
        return tva;
    }

    public void setTva(BigDecimal tva) {
        this.tva = tva;
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
        if (!(object instanceof OperationVenteAchat)) {
            return false;
        }
        OperationVenteAchat other = (OperationVenteAchat) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "bean.Achat[ id=" + id + " ]";
    }

}
