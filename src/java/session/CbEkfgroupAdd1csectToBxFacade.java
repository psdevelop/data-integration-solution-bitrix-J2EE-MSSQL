/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import entity.CbEkfgroupAdd1csectToBx;
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
public class CbEkfgroupAdd1csectToBxFacade extends AbstractFacade<CbEkfgroupAdd1csectToBx> {

    @PersistenceContext(unitName = "EKFExchApiPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CbEkfgroupAdd1csectToBxFacade() {
        super(CbEkfgroupAdd1csectToBx.class);
    }
    
    public int newCnt()    {
        try {
            List li = em.createNativeQuery("SELECT COUNT(*) FROM cb_ekfgroup_add_1csect_to_bx").getResultList();
            for(Object lit: li) {
                return Tools.parseInt(lit.toString(), -1); 
            }
            return -1;
        }   catch(Exception e)  {
            return -1;
        }
    }
    
}
