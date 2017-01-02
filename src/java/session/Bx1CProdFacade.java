/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import entity.Bx1CProd;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Poltarokov SP
 */
@Stateless
public class Bx1CProdFacade extends AbstractFacade<Bx1CProd> {

    @PersistenceContext(unitName = "EKFExchApiPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public Bx1CProdFacade() {
        super(Bx1CProd.class);
    }
    
    public void clearBx1CProd()  {
        //em.getTransaction().begin();
        em.createNativeQuery("DELETE FROM dbo.bx_1cprod").executeUpdate();
        //em.createNativeQuery(sqlString)
        //em.getTransaction().commit();
    }
    
    public int insertBx1ProdMultiply() {
        String ins = "";
        for(int i=0;i<900;i++) ins+=" ,(45,'test test test test test test test test test test test продукция',0,'33333-4444','5555-66666','ккп54ее-рр',104.2,10,120.7)";
        return em.createNativeQuery("INSERT INTO dbo.bx_1cprod ( bx_id, name, parent_bx_id, f1c_id, parent_1c_id, artikul, price, amount, bprice) VALUES (0,'test',0,'-','-','-',0,0,0)"+ins+";").executeUpdate();
    }
    
    public boolean insertBx1ProdMultiply(String instruction, int pcount) {
        return (pcount == em.createNativeQuery("INSERT INTO dbo.bx_1cprod ( bx_id, name, parent_bx_id, f1c_id, parent_1c_id, artikul, price, amount, bprice, sort_order, main_pict, prop_cnt) VALUES "+instruction+";").executeUpdate());
    }
    
}
