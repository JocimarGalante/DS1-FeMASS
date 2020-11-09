package com.femass.ds1.requerimentosfemass.util;

import com.femass.ds1.requerimentosfemass.dao.CursoDao;
import com.femass.ds1.requerimentosfemass.dao.EnderecoDao;
import com.femass.ds1.requerimentosfemass.model.Aluno;
import com.femass.ds1.requerimentosfemass.model.AlunoSerFemass;
import com.femass.ds1.requerimentosfemass.model.Endereco;
import com.google.gson.Gson;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import org.apache.commons.codec.digest.DigestUtils;

/**
 *
 * @author Alex
 */
@Stateless
public class WsWebAcademico {

    @EJB
    EnderecoDao enderecoDao;

    @EJB
    CursoDao cursoDao;
    
    /**
     * Método responsável por consumir o web service do web academico com o
     * envio de matricula e senha e retornar com os dados deste aluno
     *
     * @param matricula
     * @param senha
     * @return
     */
    public AlunoSerFemass jsonAluno(String matricula, String senha) {
        try {
            Client cliente = ClientBuilder.newClient();
            String path = "http://200.159.247.135:8083/WebAcademico/rest/usuario/buscarPorLoginSenha/" + matricula + "/" + senha;
            WebTarget caminho = cliente.target(path);

            // recebendo o json da consulta
            String json = caminho.request().get(String.class);
//            System.out.println(json);

            // convertendo em classe de objeto
            Gson gson = new Gson();
            AlunoSerFemass aluno = gson.fromJson(json, AlunoSerFemass.class);
//            System.out.println(aluno);

            return aluno;
        } catch (RuntimeException ex) {
            System.out.println("Erro ao tentar Acessar o WebService da Femass - " + ex.getMessage());
            return null;
        }
    }

    /**
     * Método atualiza o Aluno na base local visto que o mesmo digitou uma senha diferente da conhecida pelo banco local
     * @param aluno
     * @param alunoFemass
     * @param matricula
     * @param senha
     * @return 
     */
    public Aluno atualizaAluno(Aluno aluno, AlunoSerFemass alunoFemass, String matricula, String senha) {
        
        aluno.setNome(alunoFemass.getNome());
        // convertendo a senha para MD5
        System.out.println("Senha criptografada = " + DigestUtils.md5Hex(senha));
        aluno.setSenha(DigestUtils.md5Hex(senha));
        aluno.setMatricula(matricula);
        aluno.setCpf(alunoFemass.getCpf());
        aluno.setCr(alunoFemass.getCr());
        aluno.setEmail(alunoFemass.getEmail());
        aluno.setCurso(cursoDao.BuscarPorNome(alunoFemass.getCurso()));

        Endereco endereco = enderecoDao.BuscarPorLogradouroBairroAndNumero(alunoFemass.getEndereco(), alunoFemass.getEnderecoNumero(), alunoFemass.getEnderecoBairro());
        if (endereco == null) {
            endereco = new Endereco();
            endereco.setLogradouro(alunoFemass.getEndereco());
            endereco.setNumero(alunoFemass.getEnderecoNumero());
            endereco.setBairro(alunoFemass.getEnderecoBairro());
            endereco.setCep(alunoFemass.getEnderecoCep());
            endereco.setCidade(alunoFemass.getEnderecoCidade());
            endereco.setComplemento(alunoFemass.getEnderecoComplemento());
            enderecoDao.incluir(endereco);
            System.out.println(endereco);
            aluno.setEndereco(endereco);
        }else{
            aluno.setEndereco(endereco);
        }
        return aluno;
    }

}
