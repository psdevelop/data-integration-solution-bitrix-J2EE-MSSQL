/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import entity.CbSettings;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Станислав
 */
@Stateless
public class CbSettingsFacade extends AbstractFacade<CbSettings> {

    @PersistenceContext(unitName = "EKFExchApiPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CbSettingsFacade() {
        super(CbSettings.class);
    }
    
    public boolean updExchanheLastDt() {
        return (1 == em.createNativeQuery("UPDATE dbo.cb_settings SET dt_val=GETDATE() WHERE name='last_ekfgr_exchng_date';").executeUpdate());
    }
    
}
