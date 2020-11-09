/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.femass.ds1.requerimentosfemass.dao;

import com.femass.ds1.requerimentosfemass.model.Documento;
import com.femass.ds1.requerimentosfemass.model.TipoRequerimento;
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
public class TipoRequerimentoDao {

    @PersistenceContext
    EntityManager em;

    public void incluir(TipoRequerimento tipoRequerimento) {
        em.persist(tipoRequerimento);
    }

    public void mergeTipDoc(TipoRequerimento tipoReq, List<Documento> itensDoc) {
        try {
            // Salvando ou editando
            em.merge(tipoReq);

            if (itensDoc != null) {
                // salva ou edita os itens da solicitação de material
                for (int posicao = 0; posicao < itensDoc.size(); posicao++) {
                    Documento itemdoc = itensDoc.get(posicao);

                    if (itemdoc.getTipoRequerimento() == null) {
                        itemdoc.setTipoRequerimento(tipoReq);
                    }
                    em.merge(itemdoc);
                }
            }

        } catch (RuntimeException e) {

        }
    }

    public void alterar(TipoRequerimento tipoRequerimento) {
        em.merge(tipoRequerimento);
    }

    public void excluir(TipoRequerimento tipoRequerimento) {
        // resolve erro de: Entity must be managed to call remove: com.femass.ds1.requerimentosfemass.model.TipoRequerimento[ id=4 ], try merging the detached and try the remove again.
        if (!em.contains(tipoRequerimento)) {
            tipoRequerimento = em.merge(tipoRequerimento);
        }
        em.remove(tipoRequerimento);
    }

    public List<TipoRequerimento> getTipoRequerimentos() {
        Query q = em.createQuery("select t from TipoRequerimento t order by t.nome");
        return q.getResultList();
    }

}
