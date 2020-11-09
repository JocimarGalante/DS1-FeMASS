/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.femass.ds1.requerimentosfemass.dao;

import com.femass.ds1.requerimentosfemass.filter.RequerimentoFilter;
import com.femass.ds1.requerimentosfemass.model.Aluno;
import com.femass.ds1.requerimentosfemass.model.Requerimento;
import com.femass.ds1.requerimentosfemass.model.StatusRequerimento;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Gabriel
 */
@Stateless
public class RequerimentoDao {

    @PersistenceContext
    EntityManager em;

    public void incluir(Requerimento requerimento) {
        em.persist(requerimento);
    }

    public void alterar(Requerimento requerimento) {
        em.merge(requerimento);
    }

    public void excluir(Requerimento requerimento) {
        if (!em.contains(requerimento)) {
            requerimento = em.merge(requerimento);
        }
        em.remove(requerimento);
    }

    public List<Requerimento> getRequerimentos() {
        Query q = em.createQuery("select r from Requerimento r order by r.numeroProtocolo");
        return q.getResultList();
    }

    public List<Requerimento> getRequerimentosAbertos() {
        String q = "select r from Requerimento r where r.statusRequerimento=:Aberto order by r.numeroProtocolo";

        return this.em.createQuery(q, Requerimento.class)
                .setParameter("Aberto", StatusRequerimento.Aberto)
                .getResultList();
    }

    public List<Requerimento> pesqRequerimentosAluno(RequerimentoFilter filtro, Aluno aluno) {
        String q = "select r from Requerimento r WHERE";
        Query query = null;

        if (filtro.getDataInicio() != null && filtro.getDataFim() != null && !filtro.getProtocolo().equals("") && aluno.getMatricula() != null) {
            // tenho os 3 campos preenchidos
            q += " r.dataAbertura BETWEEN :startDate AND :endDate AND r.numeroProtocolo LIKE :numProtocolo AND r.aluno=:aluno";
            query = em.createQuery(q, Requerimento.class)
                    .setParameter("numProtocolo", "%" + filtro.getProtocolo() + "%")
                    .setParameter("aluno", aluno)
                    .setParameter("startDate", filtro.getDataInicio())
                    .setParameter("endDate", filtro.getDataFim());
            
        } else if (filtro.getDataInicio() != null && filtro.getDataFim() != null && aluno.getMatricula() != null) {
            q += " r.dataAbertura BETWEEN :startDate AND :endDate AND r.aluno=:aluno";
            query = em.createQuery(q, Requerimento.class)
                    .setParameter("aluno", aluno)
                    .setParameter("startDate", filtro.getDataInicio())
                    .setParameter("endDate", filtro.getDataFim());
        } else if (!filtro.getProtocolo().equals("") && aluno != null) {
            q += " r.numeroProtocolo LIKE :numProtocolo AND r.aluno=:aluno";
            query = em.createQuery(q, Requerimento.class)
                    .setParameter("aluno", aluno)
                    .setParameter("numProtocolo", "%" + filtro.getProtocolo() + "%");
        }else if(aluno.getMatricula() != null){
            q += " r.aluno=:aluno";
            query = em.createQuery(q, Requerimento.class)
                    .setParameter("aluno", aluno);
        }
        
        System.out.println("Query: " + q);
        return query.getResultList();
    }
    
    public List<Requerimento> pesqRequerimentos(RequerimentoFilter filtro) {
        String q = "select r from Requerimento r WHERE";
        Query query = null;

        if (filtro.getDataInicio() != null && filtro.getDataFim() != null && !filtro.getProtocolo().equals("")) {
            // tenho os 3 campos preenchidos
            q += " r.dataAbertura BETWEEN :startDate AND :endDate AND r.numeroProtocolo LIKE :numProtocolo";
            query = em.createQuery(q, Requerimento.class)
                    .setParameter("numProtocolo", "%" + filtro.getProtocolo() + "%")
                    .setParameter("startDate", filtro.getDataInicio())
                    .setParameter("endDate", filtro.getDataFim());
            
        } else if (filtro.getDataInicio() != null && filtro.getDataFim() != null) {
            q += " r.dataAbertura BETWEEN :startDate AND :endDate";
            query = em.createQuery(q, Requerimento.class)
                    .setParameter("startDate", filtro.getDataInicio())
                    .setParameter("endDate", filtro.getDataFim());
        } else if (!filtro.getProtocolo().equals("")) {
            q += " r.numeroProtocolo LIKE :numProtocolo";
            query = em.createQuery(q, Requerimento.class)
                    .setParameter("numProtocolo", "%" + filtro.getProtocolo() + "%");
        }
        System.out.println("Query: " + q);
        return query.getResultList();
    }
}
