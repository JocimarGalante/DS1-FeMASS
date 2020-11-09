package com.femass.ds1.requerimentosfemass.bean;

import com.femass.ds1.requerimentosfemass.util.SimpleMailTemplete;
import com.femass.ds1.requerimentosfemass.dao.CursoDao;
import com.femass.ds1.requerimentosfemass.dao.ResponsavelDao;
import com.femass.ds1.requerimentosfemass.model.Cargo;
import com.femass.ds1.requerimentosfemass.model.Curso;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.event.SelectEvent;

import com.femass.ds1.requerimentosfemass.model.Responsavel;
import com.outjected.email.api.MailMessage;
import com.outjected.email.impl.MailMessageImpl;
import com.outjected.email.impl.templating.velocity.VelocityTemplate;
import java.io.IOException;
import java.util.Arrays;
import java.util.Locale;
import javax.ejb.EJB;
import org.apache.commons.codec.digest.DigestUtils;
import org.omnifaces.util.Messages;

@ManagedBean
@ViewScoped
public class ResponsavelBean {

    private Responsavel cadastro;
    private List<Responsavel> lista;
    private List<Cargo> licargos;
    private List<Curso> licursos;
    private int size;
    private String acao;

    @EJB
    ResponsavelDao dao;

    @EJB
    CursoDao cursoDao;

    /**
     * Metodo de abertura
     */
    public void carregar() {
        try {
            lista = dao.getResponsaveis();
            size = lista.size();

            licargos = Arrays.asList(Cargo.values());
            licursos = cursoDao.getCursos();

        } catch (RuntimeException e) {
            Messages.addGlobalError(">>>> ERRO: Não foi possível carregar os Responsáveis.");
        }
    }

    /**
     * Metodo de selecao do data table pela barra de botoes
     *
     * @param evento
     */
    public void onRowSelect(SelectEvent evento) {
        cadastro = (Responsavel) evento.getObject();
        acao = "Editar";
//        System.out.println("Senha capturada na seleção da linha = " +cadastro.getSenha());
    }

    /**
     * Metodo Bean novo - Cria um novo objeto.
     */
    public void novo() {
        cadastro = new Responsavel();
        cadastro.setStatus(true);
        acao = "Salvar";
    }

    /**
     * Metodo salva e edita
     */
    public void merge() {
        try {
            
            if (acao.equals("salvar")) {
                cadastro.setSenha(DigestUtils.md5Hex(cadastro.getSenha()));
                dao.incluir(cadastro);
            } else {
                // criar uma verificação pra saber se a senha foi alterada
//                System.out.println("Senha capturada no formulário edição = " +cadastro.getSenha());
                if(cadastro.getSenha().equals("")){
                    Messages.addGlobalError(">>>> Erro: Favor Informar uma Senha para Continuar!");
                    return;
                }
                cadastro.setSenha(DigestUtils.md5Hex(cadastro.getSenha()));
                dao.alterar(cadastro);
            }
            carregar();
            novo();
            Messages.addGlobalInfo("Responsável Salvo com sucesso!");
        } catch (RuntimeException e) {
            e.printStackTrace();
            Messages.addGlobalError(">>>> ERRO: Não foi possivel Salvar o Responsável: " + cadastro.getNome());
        }
    }

    /**
     * Metodo de exclusão
     */
    public void excluir() {
        try {
            dao.excluir(cadastro);
            carregar();
            Messages.addGlobalInfo("Responsável excluído com sucesso!");
        } catch (RuntimeException e) {
            e.printStackTrace();
            Messages.addGlobalError(">>>> ERRO: Não foi possivel excluir o Responsávelo: " + cadastro.getNome() + " - " + e.getMessage());
        }
    }

    public void fechar() {
        novo();
    }

    /**
     * Envio de email com Template do apacheVelocity
     *
     * @throws IOException
     */
    public void enviarSenha() throws IOException {
        if (cadastro.getEmail() != null) {
            SimpleMailTemplete smt = new SimpleMailTemplete();
            MailMessage message = new MailMessageImpl(smt.enviarEmail());
            
            message.to(cadastro.getEmail())
                    .subject("Pedido de Envio de senha.")
                    .bodyHtml(new VelocityTemplate(getClass().getResourceAsStream("/emails/recupera_senha.template")))
                    .put("responsavel", cadastro)
                    .put("locale", new Locale("pt", "BR"))
                    .send();

            Messages.addGlobalInfo("Email enviado com sucesso!");
        } else {
            Messages.addGlobalError("ERRO: >>>> Email não pode ser enviado porque o usuário não possui email cadastrado.");
        }
    }

    //gets e sets
    public Responsavel getCadastro() {
        if (cadastro == null) {
            cadastro = new Responsavel();
        }
        return cadastro;
    }

    public void setCadastro(Responsavel cadastro) {
        this.cadastro = cadastro;
    }

    public List<Responsavel> getLista() {
        return lista;
    }

    public void setLista(List<Responsavel> lista) {
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

    public List<Cargo> getLicargos() {
        return licargos;
    }

    public void setLicargos(List<Cargo> licargos) {
        this.licargos = licargos;
    }

    public List<Curso> getLicursos() {
        return licursos;
    }

    public void setLicursos(List<Curso> licursos) {
        this.licursos = licursos;
    }

}
