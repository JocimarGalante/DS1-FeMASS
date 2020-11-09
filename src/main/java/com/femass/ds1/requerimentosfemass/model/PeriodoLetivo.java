package com.femass.ds1.requerimentosfemass.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Alex
 */
@Entity
public class PeriodoLetivo implements Serializable {

    private static final long serialVersionUID = 1L;
      
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String ano;
    private Boolean atual;
    
    private transient Integer codigoPeriodoLetivo;
    private transient Boolean cpaInstitucional;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataFim;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataInicio;
   
    @Temporal(TemporalType.TIMESTAMP)
    private transient Date dataLiberaLancamentoProfessor;
    
    @Temporal(TemporalType.TIMESTAMP)
    private transient Date dataLimiteAlterarInscricaoDisciplina;
    
    private String semestre;
    
    @Column(nullable = true)
    private Integer numAtual;

    
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAno() {
        return ano;
    }

    public void setAno(String ano) {
        this.ano = ano;
    }

    public Boolean getAtual() {
        return atual;
    }

    public void setAtual(Boolean atual) {
        this.atual = atual;
    }

    public Integer getCodigoPeriodoLetivo() {
        return codigoPeriodoLetivo;
    }

    public void setCodigoPeriodoLetivo(Integer codigoPeriodoLetivo) {
        this.codigoPeriodoLetivo = codigoPeriodoLetivo;
    }

    public Boolean getCpaInstitucional() {
        return cpaInstitucional;
    }

    public void setCpaInstitucional(Boolean cpaInstitucional) {
        this.cpaInstitucional = cpaInstitucional;
    }

    public Date getDataFim() {
        return dataFim;
    }

    public void setDataFim(Date dataFim) {
        this.dataFim = dataFim;
    }

    public Date getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(Date dataInicio) {
        this.dataInicio = dataInicio;
    }

    public Date getDataLiberaLancamentoProfessor() {
        return dataLiberaLancamentoProfessor;
    }

    public void setDataLiberaLancamentoProfessor(Date dataLiberaLancamentoProfessor) {
        this.dataLiberaLancamentoProfessor = dataLiberaLancamentoProfessor;
    }

    public Date getDataLimiteAlterarInscricaoDisciplina() {
        return dataLimiteAlterarInscricaoDisciplina;
    }

    public void setDataLimiteAlterarInscricaoDisciplina(Date dataLimiteAlterarInscricaoDisciplina) {
        this.dataLimiteAlterarInscricaoDisciplina = dataLimiteAlterarInscricaoDisciplina;
    }

    public String getSemestre() {
        return semestre;
    }

    public void setSemestre(String semestre) {
        this.semestre = semestre;
    }

    public Integer getNumAtual() {
        return numAtual;
    }

    public void setNumAtual(Integer numAtual) {
        this.numAtual = numAtual;
    }


    @Override
    public int hashCode() {
        int hash = 5;
        hash = 11 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final PeriodoLetivo other = (PeriodoLetivo) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "PeriodoLetivo{" + "ano=" + ano + ", atual=" + atual + ", codigoPeriodoLetivo=" + codigoPeriodoLetivo + ", cpaInstitucional=" + cpaInstitucional + ", dataFim=" + dataFim + ", dataInicio=" + dataInicio + ", dataLiberaLancamentoProfessor=" + dataLiberaLancamentoProfessor + ", dataLimiteAlterarInscricaoDisciplina=" + dataLimiteAlterarInscricaoDisciplina + ", semestre=" + semestre + ", numAtual=" + numAtual + '}';
    }
    
}
