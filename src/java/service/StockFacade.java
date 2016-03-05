/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.Abonne;
import bean.AvoirReception;
import bean.AvoirReceptionItem;
import bean.Famille;
import bean.Magasin;
import bean.Produit;
import bean.Reception;
import bean.ReceptionItem;
import bean.Stock;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJBAccessException;
import javax.ejb.EJBTransactionRolledbackException;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.TransactionalException;

/**
 *
 * @author moulaYounes
 */
@Stateless
public class StockFacade extends AbstractFacade<Stock> {

    @PersistenceContext(unitName = "stock_en_ligne_zouani_v4PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public StockFacade() {
        super(Stock.class);
    }

//    private Stock verifyQteProduitInStock(Abonne aboonne, Produit produit, Magasin magasin, BigDecimal qte) {
//        Stock stock = findStockByProduitAndMagasin(aboonne, produit, magasin);
//        if (stock.getQte().compareTo(qte) > 0) {
//            return stock;
//        }
//        return null;
//    }
    public void updateOrCreateStock(AvoirReceptionItem avoirReceptionItem, boolean isSave, boolean checkMode) {
        Reception reception = avoirReceptionItem.getAvoirReception().getReception();
        updateOrCreateStock(avoirReceptionItem.getMagasin(), reception.getCommande().getAbonne(), avoirReceptionItem.getProduit(), avoirReceptionItem.getQte(), !isSave, checkMode);
    }

    public int updateOrCreateStock(ReceptionItem receptionItem, boolean isSave, boolean checkMode) {
        Reception reception = receptionItem.getReception();
        return updateOrCreateStock(receptionItem.getMagasin(), reception.getCommande().getAbonne(), receptionItem.getProduit(), receptionItem.getQte(), isSave, checkMode);
    }

    private int updateOrCreateStock(Magasin magasin, Abonne abonne, Produit produit, BigDecimal qte, boolean isSave, boolean checkMode) {
        Stock stock = findStockByProduitAndMagasin(abonne, produit, magasin);
        int res = 0;
        if (stock != null && stock.getId() != null) {
            if (isSave) {
                if (!checkMode) {
                    stock.setQte(stock.getQte().add(qte));
                }
                res = 1;
            } else {
                if (stock.getQte().compareTo(qte) >= 0) {
                    if (!checkMode) {
                        stock.setQte(stock.getQte().subtract(qte));
                    }
                    res = 2;
                } else {
                    res = -1;
                }
            }
        } else {
            if (isSave) {
                stock = new Stock(produit, qte, BigDecimal.ZERO, magasin, abonne);
                res = 3;
            } else {
                res = -2;
            }
        }
        if (res > 0 && !checkMode) {
            edit(stock);
        }
        return res;
    }

    public List<Stock> findByCriteres(Abonne abonne, Magasin selectedMagasin, Produit selectedProduit, Famille selectedFamille, int seuilAlert) {
        String requtte = "SELECT st FROM Stock st WHERE 1=1 ";
        System.out.println("hana foste findByCriteres : abonne==> " + abonne);
        if (abonne != null && abonne.getId() != null) {
            boolean isMagasinSelected = false;
            requtte += " and st.abonne.id ='" + abonne.getId() + "'";
            if (selectedMagasin != null && selectedMagasin.getId() != null) {
                requtte += " and st.magasin.id ='" + selectedMagasin.getId() + "'";
                isMagasinSelected = true;
            }
            if (selectedProduit != null && selectedProduit.getId() != null) {
                requtte += " and st.produit.id = '" + selectedProduit.getId() + "'";
            } else if (selectedFamille != null && selectedFamille.getId() != null) {
                requtte += " and st.produit.famille.id = '" + selectedFamille.getId() + "'";
            }
            if (seuilAlert != -1) {
                String alertRequestPart = "";
                if (seuilAlert == 0) {
                    alertRequestPart = ">=";
                } else if (seuilAlert == 1) {
                    alertRequestPart = "<";
                }
                if (isMagasinSelected) {
                    alertRequestPart += "st.qte";
                } else {
                    alertRequestPart += "st.produit.qteGlobalStock";
                }
                requtte += " and st.produit.seuilAlert" + alertRequestPart;
            }
            System.out.println("Requette =" + requtte);
            return em.createQuery(requtte).getResultList();
        } else {
            return new ArrayList();
        }
    }

    public void updateStock(Reception reception, boolean isSave, boolean checkMode) {
        for (ReceptionItem receptionItem : reception.getReceptionItems()) {
            updateOrCreateStock(receptionItem, isSave, checkMode);
        }
    }

    public void updateStock(AvoirReception avoirReception, boolean isSave, boolean checkMode) {
        for (AvoirReceptionItem avoirReceptionItem : avoirReception.getAvoirReceptionItems()) {
            updateOrCreateStock(avoirReceptionItem, isSave, checkMode);
        }
    }

    public List<Stock> findByAbonne(Abonne abonne) {
        if (abonne != null && abonne.getId() != null) {
            String request = "SELECT st FROM Stock st WHERE st.abonne.id=" + abonne.getId();
            return em.createQuery(request).getResultList();
        }
        return new ArrayList();
    }

    public Stock findStockByProduitAndMagasin(Abonne abonne, Produit produit, Magasin magasin) {
        List<Stock> stocks = findByCriteres(abonne, magasin, produit, null, -1);
        if (stocks != null && !stocks.isEmpty()) {
            return stocks.get(0);
        }
        return null;
    }

}
