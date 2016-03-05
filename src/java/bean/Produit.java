/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
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
public class Produit implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;
    private String reference;
    private String libelle;
    @ManyToOne
    private UniteMesure uniteMesure;
    private BigDecimal qteGlobalStock;
    private BigDecimal qteParStock;
    @ManyToOne
    private Abonne abonne;
    @ManyToOne
    private Famille famille;
    private double seuilAlert;
    

    public Produit() {
    }

  

    public Produit(Long id, String reference, String libelle,String uniteMesureNom ,BigDecimal qteGlobalStock, BigDecimal qteParStock) {
        this(id, reference, libelle, uniteMesureNom, qteGlobalStock);
        this.qteParStock = qteParStock;
    }
      public Produit(Long id, String reference, String libelle,String uniteMesureNom ,BigDecimal qteGlobalStock) {
        this.id = id;
        this.reference = reference;
        this.libelle = libelle;
        getUniteMesure().setNom(uniteMesureNom);
        this.qteGlobalStock = qteGlobalStock;
    }

   

    public BigDecimal getQteParStock() {
         if (qteParStock == null) {
            qteParStock = new BigDecimal(0);
        }
        return qteParStock;
    }

    public void setQteParStock(BigDecimal qteParStock) {
        this.qteParStock = qteParStock;
    }

  

    public UniteMesure getUniteMesure() {
        if (uniteMesure == null) {
            uniteMesure = new UniteMesure();
        }
        return uniteMesure;
    }

    public void setUniteMesure(UniteMesure uniteMesure) {
        this.uniteMesure = uniteMesure;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public BigDecimal getQteGlobalStock() {
         if (qteGlobalStock == null) {
            qteGlobalStock = new BigDecimal(0);
        }
        return qteGlobalStock;
    }

    public void setQteGlobalStock(BigDecimal qteGlobalStock) {
        this.qteGlobalStock = qteGlobalStock;
    }

    public Famille getFamille() {
        if (famille == null) {
            famille = new Famille();
        }
        return famille;
    }

    public void setFamille(Famille famille) {
        this.famille = famille;
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

    public double getSeuilAlert() {
        return seuilAlert;
    }

    public void setSeuilAlert(double seuilAlert) {
        this.seuilAlert = seuilAlert;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Produit other = (Produit) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return libelle;
    }

    public String afficher() {
        return "Produit{" + "id=" + id + ", reference=" + reference + ", libelle=" + libelle + ", qteGlobalStock=" + qteGlobalStock + ", qteParStock=" + qteParStock + '}';
    }

}
