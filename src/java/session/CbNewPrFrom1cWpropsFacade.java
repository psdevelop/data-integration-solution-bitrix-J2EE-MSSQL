/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import entity.CbNewPrFrom1cWprops;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import resources.Tools;

/**
 *
 * @author Poltarokov SP
 */
@Stateless
public class CbNewPrFrom1cWpropsFacade extends AbstractFacade<CbNewPrFrom1cWprops> {

    @PersistenceContext(unitName = "EKFExchApiPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CbNewPrFrom1cWpropsFacade() {
        super(CbNewPrFrom1cWprops.class);
    }
    
    public int newProdsCnt()    {
        try {
            List li = em.createNativeQuery("SELECT COUNT(*) FROM cb_ekfgroup_new_pr_from_1c_wprops").getResultList();
            for(Object lit: li) {
                return Tools.parseInt(lit.toString(), -1); 
            }
            return -1;
        }   catch(Exception e)  {
            return -1;
        }
    }
    
}
