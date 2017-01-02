/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import entity.CbEkfgroupDel1csectFromBx;
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
public class CbEkfgroupDel1csectFromBxFacade extends AbstractFacade<CbEkfgroupDel1csectFromBx> {

    @PersistenceContext(unitName = "EKFExchApiPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CbEkfgroupDel1csectFromBxFacade() {
        super(CbEkfgroupDel1csectFromBx.class);
    }
    
        public int delCnt()    {
        try {
            List li = em.createNativeQuery("SELECT COUNT(*) FROM cb_ekfgroup_del_1csect_from_bx").getResultList();
            for(Object lit: li) {
                return Tools.parseInt(lit.toString(), -1); 
            }
            return -1;
        }   catch(Exception e)  {
            return -1;
        }
    }
    
}
