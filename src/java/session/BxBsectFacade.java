/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import entity.BxBsect;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.transaction.UserTransaction;

/**
 *
 * @author Poltarokov SP
 */
@Stateless
public class BxBsectFacade extends AbstractFacade<BxBsect> {

    @PersistenceContext(unitName = "EKFExchApiPU") //, type = PersistenceContextType.TRANSACTION
    private EntityManager em; 
    
    //@Resource
    //private UserTransaction userTransaction;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public BxBsectFacade() {
        super(BxBsect.class);
    }
    
    public void clearBxBsect()  {
        //em.getTransaction().begin();
        //try {
        //    userTransaction.begin();
        //    try {
                em.createNativeQuery("DELETE FROM dbo.bx_bsect").executeUpdate();
        //    } catch(Exception e)    {
        //        userTransaction.rollback();
        //    } finally   {
        //        userTransaction.commit();
        //    }
        //} catch(Exception ex)   {
            
        //}
        //em.createNativeQuery(sqlString)
        //em.getTransaction().commit();
    }
    
    public void addBxBsectNative()    {
        //em.getTransaction().begin();
        //try {
        //    userTransaction.begin();
        //    try {
            em.createNativeQuery("INSERT INTO dbo.bx_bsect (bx_id, name, parent_bx_id) VALUES (-1,'aaa', -1)").executeUpdate();
        //    } catch(Exception e)    {
        //        userTransaction.rollback();
        //    } finally   {
        //        userTransaction.commit();
        //    }
        //} catch(Exception ex)   {
            
        //}
        //em.getTransaction().commit();
    }
    
}
