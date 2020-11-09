/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.femass.ds1.requerimentosfemass.dao;

import com.femass.ds1.requerimentosfemass.model.Endereco;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Alex Santos
 */
@Stateless
public class EnderecoDao {

    @PersistenceContext
    EntityManager em;

    public void incluir(Endereco endereco) {
        em.persist(endereco);
    }

    public void alterar(Endereco endereco) {
        em.merge(endereco);
    }

    public void excluir(Endereco endereco) {
        em.remove(endereco);
    }

    public List<Endereco> getEnderecos() {
        Query q = em.createQuery("select e from Endereco e order by e.logradouro");
        return q.getResultList();
    }

    /**
     * Busca pelos parametros abaixo se existe um cadastro de endereço no banco de dados
     * @param logradouro
     * @param num
     * @param bairro
     * @return 
     */
    public Endereco BuscarPorLogradouroBairroAndNumero(String logradouro, String num, String bairro) {
        try {
            String q = "select e from Endereco e where e.logradouro=:log AND e.numero=:num AND e.bairro=:bairro";

            return this.em.createQuery(q, Endereco.class)
                    .setParameter("log", logradouro)
                    .setParameter("num", num)
                    .setParameter("bairro", bairro)
                    .getSingleResult();
            
        } catch (NoResultException e) {
            System.out.println("Endereço NoResultException = " + e.getMessage());
            return null;
        }
    }
}
