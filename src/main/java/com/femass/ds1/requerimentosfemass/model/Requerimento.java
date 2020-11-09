/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.femass.ds1.requerimentosfemass.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Gabriel
 */
@Entity
public class Requerimento implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.DATE)
    private Date dataAbertura;

    private String numeroProtocolo;
    private String descricao;
    private boolean revisao;

   

    @Temporal(TemporalType.DATE)
    private Date dataEncerramento;

    @ManyToOne
    private Aluno aluno;
    
    @ManyToOne
    private TipoRequerimento tipoRequerimento;
    private StatusRequerimento statusRequerimento;

    public String GerarNumeroProtocolo() {

        return id.toString();
    }

    public Aluno getAluno() {
        return aluno;
    }

    public void setAluno(Aluno aluno) {
        this.aluno = aluno;
    }

    public TipoRequerimento getTipoRequerimento() {
        return tipoRequerimento;
    }

    public void setTipoRequerimento(TipoRequerimento tipoRequerimento) {
        this.tipoRequerimento = tipoRequerimento;
    }

    public StatusRequerimento getStatusRequerimento() {
        return statusRequerimento;
    }

    public void setStatusRequerimento(StatusRequerimento statusRequerimento) {
        this.statusRequerimento = statusRequerimento;
    }

    public Date getDataAbertura() {
        return dataAbertura;
    }

    public void setDataAbertura(Date dataAbertura) {
        this.dataAbertura = dataAbertura;
    }

    public String getNumeroProtocolo() {
        return numeroProtocolo;
    }

    public void setNumeroProtocolo(String numeroProtocolo) {
        this.numeroProtocolo = numeroProtocolo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public boolean isRevisao() {
        return revisao;
    }

    public void setRevisao(boolean revisao) {
        this.revisao = revisao;
    }

    public Date getDataEncerramento() {
        return dataEncerramento;
    }

    public void setDataEncerramento(Date dataEncerramento) {
        this.dataEncerramento = dataEncerramento;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Requerimento)) {
            return false;
        }
        Requerimento other = (Requerimento) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.Requerimento[ id=" + id + " ]";
    }

}
