/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import bean.DevisCommande;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import net.sf.jasperreports.engine.JRException;

/**
 *
 * @author Hamid
 */
public class DevisCommandePdf {
    public static  void generateTransactionPdf(DevisCommande devisCommande ) throws JRException, IOException{
        
        Map<String, Object> params = new HashMap<>();
       //********* parametres *********
        params.put("dateDevisCommande", devisCommande.getDateDevisCommande());
        params.put("responsable", devisCommande.getResponsable().getNom());
        params.put("fournisseur", devisCommande.getFournisseur().getNom());
        params.put("projet", devisCommande.getProjet().getNom());
        params.put("montantTotal", devisCommande.getMontantTotal());
      
        
        
        String fileName = "BilanDevisCommande-id" + devisCommande.getId();
        PdfUtil.generatePdf(devisCommande.getDevisCommandeItems(), params, fileName);
    }
}
