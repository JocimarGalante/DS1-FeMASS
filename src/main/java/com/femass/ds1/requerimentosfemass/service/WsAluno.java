/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.femass.ds1.requerimentosfemass.service;

import com.femass.ds1.requerimentosfemass.dao.AlunoDao;
import com.femass.ds1.requerimentosfemass.model.Aluno;
import com.femass.ds1.requerimentosfemass.model.AlunoSerFemass;
import com.femass.ds1.requerimentosfemass.util.WsWebAcademico;
import com.google.gson.Gson;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.commons.codec.digest.DigestUtils;

/**
 *
 * @author gabriel Webservice de dados do aluno, usando o nosso banco de dados;
 */
@Named
@Path("aluno")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class WsAluno {

    @EJB
    WsWebAcademico wsWebAcademico;

    @EJB
    AlunoDao aluDao;

    /**
     * Método responsavel por fazer a autenticação pela aplicação mobile
     * @param json
     * @return 
     */
    @POST
    @Path("autenticar")
    public Response buscarDadosAluno(String json) {
        try {
//            System.out.println("Mostra o json recebido: " + json);

            // convertendo em classe de objeto
            Gson gson = new Gson();
            Aluno aluJsonPost = gson.fromJson(json, Aluno.class);

            // tenta autenticar no banco local do sistema
            Aluno aluLogado = aluDao.autenticar(aluJsonPost.getMatricula(), DigestUtils.md5Hex(aluJsonPost.getSenha()));

            if (aluLogado == null) {
                // tentar buscar do web service do web academico da femass
                AlunoSerFemass alunoFemass = wsWebAcademico.jsonAluno(aluJsonPost.getMatricula(), aluJsonPost.getSenha());

                if (alunoFemass == null) {
                    System.out.println("Matrícula e/ou Senha inválidos.");
                    return Response.status(Response.Status.BAD_REQUEST).entity("Matrícula e/ou Senha inválidos.").build();
                } else {
                    // verifica se existe aluno no banco interno
                    Aluno aluno = aluDao.buscarPorMat(aluJsonPost.getMatricula());
                    if (aluno == null) {
                        aluno = new Aluno();
                    }
                    // atualizando do objeto AlunoFemass para o objeto Aluno que será atualizado inclusive o endereço
                    aluno = wsWebAcademico.atualizaAluno(aluno, alunoFemass, aluJsonPost.getMatricula(), aluJsonPost.getSenha());
                    aluDao.alterar(aluno);
                    aluLogado = aluno;
                    System.out.println("Usuário autenticado com sucesso.");
                }
            } else {
                System.out.println("Usuário autenticado com sucesso.");
            }
            return Response.ok().entity(aluLogado).build();

        } catch (RuntimeException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
    }

}
