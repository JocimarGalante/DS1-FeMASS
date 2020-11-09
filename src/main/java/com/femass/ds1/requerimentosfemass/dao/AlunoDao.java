/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.femass.ds1.requerimentosfemass.dao;

import com.femass.ds1.requerimentosfemass.model.Aluno;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Gabriel
 */
@Stateless
public class AlunoDao {

    @PersistenceContext
    EntityManager em;

    public void incluir(Aluno aluno) {
        em.persist(aluno);
    }

    public void alterar(Aluno aluno) {
        em.merge(aluno);
        System.out.println("Deveria ter alterado");
    }

    public void excluir(Aluno aluno) {
        em.remove(aluno);
    }

    // modificar pra buscar somente os matriculados
    public List<Aluno> getAlunos() {
        Query q = em.createQuery("select a from Aluno a order by a.nome");
        return q.getResultList();
    }

    public Aluno porID(Long id) {
        String q = "select a from Aluno a where a.id=:id";
        Aluno aluno = this.em.createQuery(q, Aluno.class)
                .setParameter("id", id)
                .getSingleResult();
        return aluno;
    }

    public Aluno buscarPorMat(String matricula) {
        try {
            String q = "select a FROM Aluno a WHERE a.matricula=:matricula";
            Aluno aluno = this.em.createQuery(q, Aluno.class)
                    .setParameter("matricula", matricula)
                    .getSingleResult();

            return aluno;
        } catch (NoResultException e) {
            return null;
        }
    }

    public Aluno getLogin(String matricula, String senha) {

        Query q = em.createQuery("select a FROM Aluno a WHERE a.matricula=:matricula AND a.senha=:senha");
        q.setParameter("matricula", matricula);
        q.setParameter("senha", senha);
        List<Aluno> l = q.getResultList();

        if (l.isEmpty()) {
            return null;
        }

        return l.get(0);
    }

    public Aluno autenticar(String mat, String senha) {
        try {
            String q = "select a FROM Aluno a WHERE a.matricula=:matricula and a.senha=:senha";
            Aluno aluno = this.em.createQuery(q, Aluno.class)
                    .setParameter("matricula", mat)
                    .setParameter("senha", senha)
                    .getSingleResult();

            System.out.println("Aluno = " + aluno.getId() + " - " + aluno.getNome());

            return aluno;
        } catch (NoResultException e) {
            System.out.println("Aluno = " + e.getMessage());
            return null;
        }
    }

}
