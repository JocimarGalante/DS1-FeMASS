package com.femass.ds1.requerimentosfemass.bean;

import com.femass.ds1.requerimentosfemass.util.Constantes;
import com.femass.ds1.requerimentosfemass.util.SimpleMailTemplete;
import com.femass.ds1.requerimentosfemass.dao.AlunoDao;
import com.femass.ds1.requerimentosfemass.dao.CursoDao;
import com.femass.ds1.requerimentosfemass.dao.EnderecoDao;
import com.femass.ds1.requerimentosfemass.dao.ResponsavelDao;
import com.femass.ds1.requerimentosfemass.model.Aluno;
import com.femass.ds1.requerimentosfemass.model.AlunoSerFemass;
import com.femass.ds1.requerimentosfemass.model.Endereco;
import com.femass.ds1.requerimentosfemass.model.Responsavel;
import com.femass.ds1.requerimentosfemass.util.WsWebAcademico;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.outjected.email.api.MailMessage;
import com.outjected.email.impl.MailMessageImpl;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Locale;
import java.util.UUID;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.velocity.VelocityContext;
import org.omnifaces.util.Messages;

/**
 *
 * @author Alex
 */
@ManagedBean
@SessionScoped
public class AutenticacaoBean {

    private Responsavel respLogado;
    private Aluno aluLogado;
    boolean responsavel;
    private String cpf_mat;
    private String senha;
    private Constantes constante;

    @EJB
    ResponsavelDao respDao;

    @EJB
    AlunoDao aluDao;

    @EJB
    WsWebAcademico wsWebAcademico;
    
    /**
     * Metodo sair
     *
     * @return
     */
    public String sair() {
        try {
            System.out.println("Usuário Deslogado...");
            respLogado = null;
            aluLogado = null;
            System.out.println("Responsavel ou aluno nulo");
            return "/comum/autenticacao/autenticacao.xhtml?faces-redirect=true";
        } catch (RuntimeException ex) {
            Messages.addGlobalError("Erro ao tentar sair do sistema: " + ex.getMessage());
            return null;
        }
    }

    /**
     * Metodo Autenticar trocado de void para string
     *
     * @return
     */
    public String autenticar() {
        try {
            if (responsavel) {
                respLogado = respDao.autenticar(cpf_mat, DigestUtils.md5Hex(senha));

                if (respLogado == null) {
                    Messages.addGlobalError("CPF e/ou Senha inválidos.");
                    return null;
                } else {
                    Messages.addGlobalInfo("Usuário autenticado com sucesso.");
                    cpf_mat = "";
                    senha = "";
                    return "/comum/entrada/principal.xhtml?faces-redirect=true";
                }
            } else {
                aluLogado = aluDao.autenticar(cpf_mat, DigestUtils.md5Hex(senha));

                if (aluLogado == null) {
                    // tentar buscar do web service do web academico da femass
                    AlunoSerFemass alunoFemass = wsWebAcademico.jsonAluno(cpf_mat, senha);

                    if (alunoFemass == null) {
                        Messages.addGlobalError("Matrícula e/ou Senha inválidos.");
                        return null;
                    } else {
                        // verifica se existe aluno no banco interno
                        Aluno aluno = aluDao.buscarPorMat(cpf_mat);
                        if (aluno == null) {
                            aluno = new Aluno();
                        }

                        // atualizando do objeto AlunoFemass para o objeto Aluno que será atualizado inclusive o endereço
                        aluno = wsWebAcademico.atualizaAluno(aluno, alunoFemass, cpf_mat, senha);

                        aluDao.alterar(aluno);
                        aluLogado = aluno;

                        Messages.addGlobalInfo("Usuário autenticado com sucesso.");
                        cpf_mat = "";
                        senha = "";
                        return "/comum/entrada/principal_aluno.xhtml?faces-redirect=true";
                    }
                } else {
                    Messages.addGlobalInfo("Usuário autenticado com sucesso.");
                    cpf_mat = "";
                    senha = "";
                    return "/comum/entrada/principal_aluno.xhtml?faces-redirect=true";
                }
            }
        } catch (RuntimeException ex) {
            Messages.addGlobalError("Erro ao tentar entrar no sistema: " + ex.getMessage());
            return null; // permanecer na pagina onde estou
        }
    }

    /**
     * Envio de senha ppor email.
     *
     * @throws IOException
     */
    public void recuperarSenha() throws IOException {
        String email;
        // envia variaveis para o template
        VelocityContext context = new VelocityContext();

        if (cpf_mat == null || cpf_mat.equals("")) {
            Messages.addGlobalError("ERRO: >>>> Favor informar o CPF ou MATRÍCULA e clicar em 'ESQUECI MINHA SENHA'.");
            System.out.println("faltou informar o CPF OU MATRÍCULA.");
            return;
        } else {
            senha = getRandomId();
            if (responsavel) {
                Responsavel resp = new Responsavel();
                resp = respDao.buscarPorCPF(cpf_mat);

                if (resp != null) {
                    System.out.println("Senha: " + senha);
                    resp.setSenha(DigestUtils.md5Hex(senha));
                    System.out.println("MD5 senha: " + DigestUtils.md5Hex(senha));
                    respDao.alterar(resp);

                    email = resp.getEmail();
                    context.put("usuario", resp);
                    context.put("senha", senha);
                    if (resp.getEmail().equals("")) {
                        Messages.addGlobalError("ERRO: >>>> Não possui email em seu cadastro, favor procurar o Desenvolvedor do Sistema.");
                        return;
                    }
                } else {
                    Messages.addGlobalError("ERRO: >>>> Este CPF não possui cadastro no sistema.");
                    return;
                }
                // é um aluno tentando
            } else {
                Messages.addGlobalError("ERRO: >>>> Este Sistema é integrado com o Web Academico, altere sua senha lá e tente novamente.");
                return;
            }

            //configuração do email return configsession
            SimpleMailTemplete smt = new SimpleMailTemplete();
            MailMessage message = new MailMessageImpl(smt.enviarEmail());

            //prepara o conteúdo do email em html com codificação
            StringWriter writer = smt.escreveTempate("recupera_senha.html", context);

            message.to(email)
                    .subject("Pedido de Envio de senha.")
                    .bodyHtml(writer.toString())
                    .put("locale", new Locale("pt", "BR"))
                    .send();
            Messages.addGlobalInfo("Sua senha foi enviada por Email com sucesso!");
        }
    }

    /**
     * Método que gera uma senha randomica para poder ser alterar posteriormente
     * @return 
     */
    private String getRandomId() {
        return UUID.randomUUID().toString().substring(0, 8);
    }

    public Responsavel getRespLogado() {
        if (respLogado == null) {
            respLogado = new Responsavel();
        }
        return respLogado;
    }

    public void setRespLogado(Responsavel respLogado) {
        this.respLogado = respLogado;
    }

    public Constantes getConstante() {
        if (constante == null) {
            constante = new Constantes();
        }
        return constante;
    }

    public Aluno getAluLogado() {
        if (aluLogado == null) {
            aluLogado = new Aluno();
        }
        return aluLogado;
    }

    public void setAluLogado(Aluno aluLogado) {
        this.aluLogado = aluLogado;
    }

    public boolean isResponsavel() {
        return responsavel;
    }

    public void setResponsavel(boolean responsavel) {
        this.responsavel = responsavel;
    }

    public String getCpf_mat() {
        return cpf_mat;
    }

    public void setCpf_mat(String cpf_mat) {
        this.cpf_mat = cpf_mat;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
