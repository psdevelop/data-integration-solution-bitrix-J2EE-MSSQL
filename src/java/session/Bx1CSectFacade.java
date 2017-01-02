/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import entity.Bx1CSect;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Poltarokov SP
 */
@Stateless
public class Bx1CSectFacade extends AbstractFacade<Bx1CSect> {

    @PersistenceContext(unitName = "EKFExchApiPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public Bx1CSectFacade() {
        super(Bx1CSect.class);
    }
    
    public void clearBx1CSect()  {
        //em.getTransaction().begin();
        //em.
        em.createNativeQuery("DELETE FROM dbo.bx_1csect").executeUpdate();
        //em.createNativeQuery(sqlString)
        //em.getTransaction().commit();
    }
    
    public boolean insertBx1SectMultiply(String instruction, int pcount) {
        return (pcount == em.createNativeQuery("INSERT INTO dbo.bx_1csect ( bx_id, name, parent_bx_id, f1c_id, parent_1c_id, full_description, type_completing, char_gabarits, short_desription, documentation, description, picture, video_description, mcatalog, collapse_vc, advants, filter_props, descripts_json, gabarits_json, docs_json, sort_order, seo_alias_url, seo_title, seo_h1 ) VALUES "+instruction+";").executeUpdate());
    }
    
}
