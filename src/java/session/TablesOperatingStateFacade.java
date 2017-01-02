/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import entity.TablesOperatingState;
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
public class TablesOperatingStateFacade extends AbstractFacade<TablesOperatingState> {

    @PersistenceContext(unitName = "EKFExchApiPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public TablesOperatingStateFacade() {
        super(TablesOperatingState.class);
    }
    
    public boolean setEkfGrExchDataUnCorrupt()   {
        boolean res=false;
        try {
            int try_cnt=0;
            while(!res&&try_cnt<10) {
                if(em.createNativeQuery("UPDATE tables_operating_state SET in_use_by_cb=0 WHERE table_name='all'").executeUpdate()>0) {
                    res=true;
                }
                try_cnt++;
            }
        }   catch(Exception e)  {
            return false;
        }
        return res;
    }
    
    public boolean setEkfGrExchDataCorrupt()   {
        boolean res=false;
        try {
            int try_cnt=0;
            while(!res&&try_cnt<10) {
                if(em.createNativeQuery("UPDATE tables_operating_state SET in_use_by_cb=1 WHERE table_name='all'").executeUpdate()>0) {
                    res=true;
                }
                try_cnt++;
            }
        }   catch(Exception e)  {
            return false;
        }
        return res;
    }
    
    public boolean getEkfGrExchDataCorrupt()   {
        boolean res=true;
        try {
            List li = em.createNativeQuery("SELECT TOP(1) in_use_by_1C FROM tables_operating_state WHERE table_name='all'").getResultList();
            for(Object lit: li) {
                res = !(Tools.parseInt(lit.toString(), 1)==0);
                return res; 
            }
            return res;
        }   catch(Exception e)  {
            return true;
        }
        //return res;
    }
    
    public boolean setEkfGrExchDataUnDelta()   {
        boolean res=false;
        try {
            int try_cnt=0;
            while(!res&&try_cnt<10) {
                if(em.createNativeQuery("UPDATE tables_operating_state SET in_use_by_cb=0 WHERE table_name='delta'").executeUpdate()>0) {
                    res=true;
                }
                try_cnt++;
            }
        }   catch(Exception e)  {
            return false;
        }
        return res;
    }
    
    public boolean setEkfGrExchDataDelta()   {
        boolean res=false;
        try {
            int try_cnt=0;
            while(!res&&try_cnt<10) {
                if(em.createNativeQuery("UPDATE tables_operating_state SET in_use_by_cb=1 WHERE table_name='delta'").executeUpdate()>0) {
                    res=true;
                }
                try_cnt++;
            }
        }   catch(Exception e)  {
            return false;
        }
        return res;
    }
    
    public boolean getEkfGrExchDataDelta()   {
        boolean res=true;
        try {
            List li = em.createNativeQuery("SELECT TOP(1) in_use_by_cb FROM tables_operating_state WHERE table_name='delta'").getResultList();
            for(Object lit: li) {
                res = !(Tools.parseInt(lit.toString(), 1)==0);
                return res; 
            }
            return res;
        }   catch(Exception e)  {
            return true;
        }
        //return res;
    }
    
}
