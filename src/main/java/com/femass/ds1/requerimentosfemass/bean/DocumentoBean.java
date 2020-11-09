/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.femass.ds1.requerimentosfemass.bean;

import com.femass.ds1.requerimentosfemass.dao.DocumentoDao;
import com.femass.ds1.requerimentosfemass.model.Documento;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.omnifaces.util.Messages;

/**
 *
 * @author GMC
 */

@ManagedBean
@ViewScoped
public class DocumentoBean {
    private Documento cadastro;
    private List<Documento> lista;
    private int size;
    private String acao;

    @EJB
    DocumentoDao dao;
    
    /**
     * Metodo de abertura
     */
    public void carregar() {
        try {
            lista = dao.getDocumento();
            size = lista.size();

        } catch (RuntimeException e) {
            Messages.addGlobalError(">>>> ERRO: Não foi possível carregar os documentos.");
        }
    }
    
    //gets e sets

    public Documento getCadastro() {
        return cadastro;
    }

    public void setCadastro(Documento cadastro) {
        this.cadastro = cadastro;
    }

    public List<Documento> getLista() {
        return lista;
    }

    public void setLista(List<Documento> lista) {
        this.lista = lista;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getAcao() {
        return acao;
    }

    public void setAcao(String acao) {
        this.acao = acao;
    }
    
}
