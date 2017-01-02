/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import entity.CbEkfroupDelFromBxView;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import resources.Tools;

/**
 *
 * @author arzugurbanova
 */
@Stateless
public class CbEkfroupDelFromBxViewFacade extends AbstractFacade<CbEkfroupDelFromBxView> {

    @PersistenceContext(unitName = "EKFExchApiPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CbEkfroupDelFromBxViewFacade() {
        super(CbEkfroupDelFromBxView.class);
    }
    
    public int delProdsCnt()    {
        try {
            List li = em.createNativeQuery("SELECT COUNT(*) FROM cb_ekfroup_del_from_bx_view").getResultList();
            for(Object lit: li) {
                return Tools.parseInt(lit.toString(), -1); 
            }
            return -1;
        }   catch(Exception e)  {
            return -1;
        }
    }
    
}
