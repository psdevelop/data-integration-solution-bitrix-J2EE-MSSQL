/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import entity.CbUserGroups;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Poltarokov SP
 */
@Stateless
public class CbUserGroupsFacade extends AbstractFacade<CbUserGroups> {

    @PersistenceContext(unitName = "EKFExchApiPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CbUserGroupsFacade() {
        super(CbUserGroups.class);
    }
    
}
