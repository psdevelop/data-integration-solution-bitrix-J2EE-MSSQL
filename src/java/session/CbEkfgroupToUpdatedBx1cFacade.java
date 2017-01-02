/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import entity.CbEkfgroupToUpdatedBx1c;
import java.util.ArrayList;
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
public class CbEkfgroupToUpdatedBx1cFacade extends AbstractFacade<CbEkfgroupToUpdatedBx1c> {

    @PersistenceContext(unitName = "EKFExchApiPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CbEkfgroupToUpdatedBx1cFacade() {
        super(CbEkfgroupToUpdatedBx1c.class);
    }
    
    public int updProdsCnt()    {
        try {
            List li = em.createNativeQuery("SELECT COUNT(*) FROM cb_ekfgroup_to_updated_bx_1c").getResultList();
            for(Object lit: li) {
                return Tools.parseInt(lit.toString(), -1); 
            }
            return -1;
        }   catch(Exception e)  {
            return -1;
        }
    }
    
    public boolean updProdMultiply(ArrayList<String> updProdsIds) {
        StringBuilder in_cond = new StringBuilder();
        in_cond.append("(");
        for(int i=0;i<updProdsIds.size();i++)   {
            if(i>0) in_cond.append(",");
            in_cond.append("'").append(updProdsIds.get(i).replace("'", "''")).append("'");
        }
        in_cond.append(")"); 
        if(updProdsIds.isEmpty())
            return true;
        else
            return (updProdsIds.size() == em.createNativeQuery("UPDATE dbo.products SET updated_at=dbo.getLastEkfgrExchngDate() WHERE id IN "+in_cond.toString()+";").executeUpdate());
    }
    
}
