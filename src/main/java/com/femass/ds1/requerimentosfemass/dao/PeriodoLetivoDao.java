package com.femass.ds1.requerimentosfemass.dao;

import com.femass.ds1.requerimentosfemass.model.Aluno;
import com.femass.ds1.requerimentosfemass.model.PeriodoLetivo;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Alex
 */
@Stateless
public class PeriodoLetivoDao {
    @PersistenceContext
    EntityManager em;

    public void incluir(PeriodoLetivo periodoLetivo) {
        em.persist(periodoLetivo);
    }

    public void alterar(PeriodoLetivo periodoLetivo) {
        em.merge(periodoLetivo);
    }

    public void excluir(PeriodoLetivo periodoLetivo) {
        em.remove(periodoLetivo);
    }

    public List<PeriodoLetivo> getPeriodoLetivos() {
        Query q = em.createQuery("select r from PeriodoLetivo r order by r.ano");
        return q.getResultList();
    }
    
    public PeriodoLetivo getPeridoAtual() {
        try {
            String q = "select r FROM PeriodoLetivo r WHERE r.atual=:atual";
            PeriodoLetivo pLetivo = this.em.createQuery(q, PeriodoLetivo.class)
                    .setParameter("atual", true)
                    .getSingleResult();

            return pLetivo;
        } catch (NoResultException e) {
            return null;
        }
    }
}