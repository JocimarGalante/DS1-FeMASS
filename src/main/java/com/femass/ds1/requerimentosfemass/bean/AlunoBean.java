/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.femass.ds1.requerimentosfemass.bean;

import com.femass.ds1.requerimentosfemass.dao.AlunoDao;
import com.femass.ds1.requerimentosfemass.model.Aluno;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.omnifaces.util.Messages;

/**
 *
 * @author Alex
 */
@ManagedBean
@ViewScoped
public class AlunoBean {
    private Aluno cadastro;
    private List<Aluno> lista;
    private int size;
    private String acao;

    @EJB
    AlunoDao dao;
    
    /**
     * Metodo de abertura
     */
    public void carregar() {
        try {
            lista = dao.getAlunos();
            size = lista.size();

        } catch (RuntimeException e) {
            Messages.addGlobalError(">>>> ERRO: Não foi possível carregar os Alunos.");
        }
    }
    
    //gets e sets

    public Aluno getCadastro() {
        return cadastro;
    }

    public void setCadastro(Aluno cadastro) {
        this.cadastro = cadastro;
    }

    public List<Aluno> getLista() {
        return lista;
    }

    public void setLista(List<Aluno> lista) {
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
