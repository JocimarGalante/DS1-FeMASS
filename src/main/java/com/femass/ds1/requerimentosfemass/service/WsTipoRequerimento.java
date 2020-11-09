/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.femass.ds1.requerimentosfemass.service;

import com.femass.ds1.requerimentosfemass.dao.TipoRequerimentoDao;
import com.femass.ds1.requerimentosfemass.model.TipoRequerimento;
import java.util.List;
import java.util.concurrent.ExecutorService;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author alagalante
 */
@Named
@Path("requerimento")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class WsTipoRequerimento {

    @EJB
    TipoRequerimentoDao daoTipoRequerimento;
    
    @GET
    @Path("tiposRequerimento")
    public List<TipoRequerimento> buscarTiposRequerimento() {
        return daoTipoRequerimento.getTipoRequerimentos();
    }
    
    
    
}

