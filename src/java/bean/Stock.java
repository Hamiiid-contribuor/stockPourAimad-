/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 *
 * @author moulaYounes
 */
@Entity
public class Stock implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Produit produit;
    private BigDecimal qte;
    private BigDecimal qteDeffectueux;
    @ManyToOne
    private Magasin magasin;
    @ManyToOne
    private Abonne abonne;

    public Stock() {
    }

    public Stock(Produit produit, BigDecimal qte, BigDecimal qteDeffectueux, Magasin magasin, Abonne abonne) {
        this.produit = produit;
        this.qte = qte;
        this.qteDeffectueux = qteDeffectueux;
        this.magasin = magasin;
        this.abonne = abonne;
    }

    
    public Abonne getAbonne() {
        if (abonne == null) {
            abonne = new Abonne();
        }
        return abonne;
    }

    public void setAbonne(Abonne abonne) {
        this.abonne = abonne;
    }

    public Produit getProduit() {
         if (produit == null) {
            produit = new Produit();
        }
        return produit;
    }

    public void setProduit(Produit produit) {
        this.produit = produit;
    }

    public BigDecimal getQteDeffectueux() {
         if (qteDeffectueux == null) {
            qteDeffectueux = new BigDecimal(0);
        }
        return qteDeffectueux;
    }

    public void setQteDeffectueux(BigDecimal qteDeffectueux) {
        this.qteDeffectueux = qteDeffectueux;
    }

    
    public BigDecimal getQte() {
        if (qte == null) {
            qte = new BigDecimal(0);
        }
        return qte;
    }

    public void setQte(BigDecimal qte) {
        this.qte = qte;
    }

    public Magasin getMagasin() {
           if(magasin==null){
            magasin= new Magasin();
        }
        return magasin;
    }

    public void setMagasin(Magasin magasin) {
        this.magasin = magasin;
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
        if (!(object instanceof Stock)) {
            return false;
        }
        Stock other = (Stock) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "bean.Stock[ id=" + id + " ]";
    }

}
