/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import entity.CbLogs;
import java.util.Date;
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
public class CbLogsFacade extends AbstractFacade<CbLogs> {

    @PersistenceContext(unitName = "EKFExchApiPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CbLogsFacade() {
        super(CbLogs.class);
    }
    
    public void insertLog(String type, String caption, String text) {
        try {
            CbLogs log = new CbLogs();
            log.setId(-1);
            log.setDt(new Date());
            log.setCaption(caption);
            log.setLogText(text);
            log.setLogType(type);
            this.create(log);
        } catch(Exception lge)  {
                
        }
    }
    
    public void insertBoldLog(String type, String caption, String text) {
        try {
            CbLogs log = new CbLogs();
            log.setId(-1);
            log.setDt(new Date());
            log.setCaption("<b>"+caption+"</b>");
            log.setLogText("<b>"+text+"</b>");
            log.setLogType("<b>"+type+"</b>");
            this.create(log);
        } catch(Exception lge)  {
                
        }
    }
    
    public List<CbLogs> getLast200Logs()   {
        return em.createNamedQuery("CbLogs.findAll").setMaxResults(200).getResultList();
    }
    
    public List<CbLogs> getCriticalInfo()   {
        return em.createNamedQuery("CbLogs.findByLogType").setParameter("logType", "CRITICAL_INFO").getResultList();
    }
    
    public List<CbLogs> getCriticalErrs()   {
        return em.createNamedQuery("CbLogs.findByLogType").setParameter("logType", "CRITICAL_ERRS").getResultList();
    }
    
    public int ekfProdCount()    {
        try {
            List li = em.createNativeQuery("SELECT COUNT(*) FROM cb_ekfgroup_toload_view").getResultList();
            for(Object lit: li) {
                return Tools.parseInt(lit.toString(), -1); 
            }
            return -1;
        }   catch(Exception e)  {
            return -1;
        }
    }
    
    public void updProductByVendorCode(String vcode)    {
        try {
            em.createNativeQuery("UPDATE products SET updated_at=GETDATE()").executeUpdate();// WHERE vendor_code='"+vcode+"'
        }   catch(Exception e)  {

        }
    }
    
    public int checkEkfProdLogsCount()    {
        try {
            List li = em.createNativeQuery("SELECT COUNT(*) FROM cb_logs").getResultList();
            for(Object lit: li) {
                int lcnt = Tools.parseInt(lit.toString(), -1); 
                if(lcnt>=700)  {
                    CbLogs cbl = (CbLogs)em.createNamedQuery("CbLogs.findAll").setMaxResults(200).getResultList().get(199);
                    em.createNativeQuery("DELETE FROM cb_logs where id<"+cbl.getId()).executeUpdate();
                }
            }
            return -1;
        }   catch(Exception e)  {
            return -1;
        }
    }
    
    public int ekfProdSectCount()    {
        try {
            List li = em.createNativeQuery("SELECT COUNT(*) FROM cb_ekfgroup_cattree").getResultList();
            for(Object lit: li) {
                return Tools.parseInt(lit.toString(), -1); 
            }
            return -1;
        }   catch(Exception e)  {
            return -1;
        }
    }
    
}
