/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.femass.ds1.requerimentosfemass.bean;

import com.femass.ds1.requerimentosfemass.dao.CursoDao;
import com.femass.ds1.requerimentosfemass.model.Curso;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.omnifaces.util.Messages;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author Alex
 */
@ManagedBean
@ViewScoped
public class CursoBean {
    private Curso cadastro;
    private List<Curso> lista;
    private int size;
    private String acao;

    @EJB
    CursoDao dao;
    
    /**
     * Metodo de abertura
     */
    public void carregar() {
        try {
            lista = dao.getCursos();
            size = lista.size();

        } catch (RuntimeException e) {
            Messages.addGlobalError(">>>> ERRO: Não foi possível carregar os Cursos.");
        }
    }

    /**
     * Metodo de selecao do data table pela barra de botoes
     *
     * @param evento
     */
    public void onRowSelect(SelectEvent evento) {
        cadastro = (Curso) evento.getObject();
        acao = "Editar";
    }

    /**
     * Metodo Bean novo - Cria um novo objeto.
     */
    public void novo() {
        cadastro = new Curso();
        acao = "Salvar";
    }
    
    /**
     * Metodo salva e edita 
     */
    public void merge(){
        try{
            if(acao.equals("salvar")){
               dao.incluir(cadastro);
            }else{
                dao.alterar(cadastro);
            }
            carregar();
            novo();
            Messages.addGlobalInfo("Curso Salvo com sucesso!");
        } catch (RuntimeException e) {
            e.printStackTrace();
            Messages.addGlobalError(">>>> ERRO: Não foi possivel Salvar o Curso: "+cadastro.getNome());
        }
    }
    
    /**
     * Metodo de exclusão
     */
    public void excluir(){
        try{
            dao.excluir(cadastro);
            carregar();
            Messages.addGlobalInfo("Curso excluído com sucesso!");
        } catch (RuntimeException e) {
            e.printStackTrace();
            Messages.addGlobalError(">>>> ERRO: Não foi possivel excluir o Curso: "+cadastro.getNome()+" - "+ e.getMessage());
        }
    }
    
    public void fechar(){
        novo();
    }
    
    //gets e sets

    public Curso getCadastro() {
        if(cadastro == null){
            cadastro = new Curso();
        }
        return cadastro;
    }

    public void setCadastro(Curso cadastro) {
        this.cadastro = cadastro;
    }

    public List<Curso> getLista() {
        return lista;
    }

    public void setLista(List<Curso> lista) {
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
