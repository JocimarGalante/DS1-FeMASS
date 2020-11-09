package com.femass.ds1.requerimentosfemass.bean;

import com.femass.ds1.requerimentosfemass.util.SimpleMailTemplete;
import com.femass.ds1.requerimentosfemass.dao.AlunoDao;
import com.femass.ds1.requerimentosfemass.dao.MovimentacaoDao;
import com.femass.ds1.requerimentosfemass.dao.PeriodoLetivoDao;
import com.femass.ds1.requerimentosfemass.dao.RequerimentoDao;
import com.femass.ds1.requerimentosfemass.dao.ResponsavelDao;
import com.femass.ds1.requerimentosfemass.dao.TipoRequerimentoDao;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.omnifaces.util.Messages;
import org.primefaces.event.SelectEvent;

import com.femass.ds1.requerimentosfemass.model.Aluno;
import com.femass.ds1.requerimentosfemass.model.Requerimento;
import com.femass.ds1.requerimentosfemass.model.StatusRequerimento;
import com.femass.ds1.requerimentosfemass.model.TipoRequerimento;
import com.femass.ds1.requerimentosfemass.filter.RequerimentoFilter;
import com.femass.ds1.requerimentosfemass.model.Movimentacao;
import com.femass.ds1.requerimentosfemass.model.PeriodoLetivo;
import com.femass.ds1.requerimentosfemass.model.Responsavel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.outjected.email.api.MailMessage;
import com.outjected.email.impl.MailMessageImpl;
import java.io.IOException;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;
import javax.ejb.EJB;
import javax.faces.bean.ManagedProperty;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import org.apache.velocity.VelocityContext;

@ManagedBean
@ViewScoped
public class RequerimentoBean {

    private Requerimento cadastro;
    private List<Requerimento> lista;
    private List<Requerimento> lipesq;
    private List<TipoRequerimento> listatipo;
    private List<StatusRequerimento> liStatusReq;
    private int size;
    private String acao;
    private RequerimentoFilter filtro;
    private Aluno aluno;
    private List<Aluno> lialuno;
    private boolean liTodos;
    private Movimentacao movimentacao;
    private List<Responsavel> liResponsavel;
    private Responsavel responsavel;
    private PeriodoLetivo pLetivo;
    @ManagedProperty(value = "#{autenticacaoBean}")
    private AutenticacaoBean autenticacaoBean;

    @EJB
    RequerimentoDao dao;

    @EJB
    TipoRequerimentoDao tipoDao;

    @EJB
    AlunoDao alunoDAO;

    @EJB
    MovimentacaoDao movDAO;

    @EJB
    ResponsavelDao respDAO;

    @EJB
    PeriodoLetivoDao pLetDAO;

    /**
     * Metodo de abertura
     */
    public void carregar() {
        try {
            if (liTodos == true) {
                lista = dao.getRequerimentos();
            } else {
                lista = dao.getRequerimentosAbertos();
            }
            verificaRequerimentosACancelar();
            size = lista.size();
            this.listasFormulario();
            liStatusReq = Arrays.asList(StatusRequerimento.values());
        } catch (RuntimeException e) {
            Messages.addGlobalError(">>>> ERRO: Não foi possível carregar os Requerimentos." + "Erro: " + e.getMessage());
        }
    }

    /**
     * Método de Consulta
     */
    public void consultar() {
        try {
            if (autenticacaoBean.getAluLogado().getMatricula() != null) {
                lipesq = dao.pesqRequerimentosAluno(filtro, autenticacaoBean.getAluLogado());
            } else {
                if (filtro.getDataInicio() == null && filtro.getDataFim() == null && filtro.getProtocolo().equals("")) {
                    Messages.addGlobalError(">>>> ERRO: informe algum parametro para pesquisa.");
                    return;
                } else {
                    lipesq = dao.pesqRequerimentos(filtro);
                }
            }
            size = lipesq.size();
        } catch (RuntimeException e) {
            Messages.addGlobalError(">>>> ERRO ao tentar consultar um ou mais processos." + e.getMessage());
        }
    }

    /**
     * Metodo de selecao do data table pela barra de botoes
     *
     * @param evento
     */
    public void onRowSelect(SelectEvent evento) {
        cadastro = (Requerimento) evento.getObject();
        acao = "Editar";
    }

    /**
     * Metodo Bean novo - Cria um novo objeto.
     */
    public void novo() {
        cadastro = new Requerimento();
        acao = "Salvar";
        // fazer forma de numeração automática
//        cadastro.setNumeroProtocolo("001/2018");
        cadastro.setDataAbertura(new Date());
        cadastro.setStatusRequerimento(StatusRequerimento.Aberto);
        System.out.println("Metodo Novo >>>>>>>>");
    }

    /**
     * Metodo Bean novo - Cria um novo objeto.
     */
    public void novoRequerimentoAluno() {
        cadastro = new Requerimento();
        listatipo = tipoDao.getTipoRequerimentos();
        acao = "Salvar";
        // fazer forma de numeração automática
//        cadastro.setNumeroProtocolo(this.getNumProtocolo());
        cadastro.setDataAbertura(new Date());
        cadastro.setStatusRequerimento(StatusRequerimento.Aberto);
        cadastro.setAluno(autenticacaoBean.getAluLogado());
        System.out.println("Metodo NovoRequerimentoAluno() >>>>>>>>");
    }
    
    private void listasFormulario(){
        listatipo = tipoDao.getTipoRequerimentos();
        lialuno = alunoDAO.getAlunos();
    }

    /**
     * Método responsável por verificar se existe um registro de período letivo
     * se não tiver ele consulta no WebService e cria o primeiro registro.
     *
     * @return
     */
    private String getNumProtocolo() {
        System.out.println("Entrei no getNumProtocolo()");
        pLetivo = pLetDAO.getPeridoAtual();

        if (pLetivo == null) {
            pLetivo = new PeriodoLetivo();
            Client cliente = ClientBuilder.newClient();
            String path = "http://200.159.247.135:8083/WebAcademico/rest/usuario/buscarPeriodoLetivoAtual";
            WebTarget caminho = cliente.target(path);

            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();

            // recebendo o json da consulta
            String json = caminho.request().get(String.class);
            System.out.println(json);

            pLetivo = gson.fromJson(json, PeriodoLetivo.class);
            System.out.println(pLetivo);
            pLetivo.setNumAtual(0);
            pLetDAO.incluir(pLetivo);
        }

        Integer num = pLetivo.getNumAtual() + 1;
        System.out.println(num + "/" + pLetivo.getAno() + "-" + pLetivo.getSemestre());
        return num + "/" + pLetivo.getAno() + "-" + pLetivo.getSemestre();
    }

    /**
     * Método responsável por incrementar a numeração caso o requirimento seja
     * salvo
     *
     * @param pletivo
     */
    private void IncrementaNumAtual(PeriodoLetivo pletivo) {
        System.out.println("Entrei no IncrementaNumAtual(PeriodoLetivo pletivo)");
        pletivo.setNumAtual(pletivo.getNumAtual() + 1);
        pLetDAO.alterar(pletivo);
    }

    /**
     * Método Salva ou edita
     */
    public void merge() {
        try {
            if (acao.equals("Salvar")) {
                if (verificaDataLimite() == -1) {
                    Messages.addGlobalError(">>>> ERRO: Data limite atingida, não é possível abrir requerimento pra esse tipo: " + cadastro.getTipoRequerimento().getNome());
                    return;
                } else {
                    cadastro.setNumeroProtocolo(this.getNumProtocolo());
                    dao.incluir(cadastro);
                    IncrementaNumAtual(pLetivo);
                    Messages.addGlobalInfo("Requerimento Nº " +cadastro.getNumeroProtocolo()+ " Salvo com sucesso!");
                }
            } else {
                dao.alterar(cadastro);
                Messages.addGlobalInfo("Requerimento Nº " +cadastro.getNumeroProtocolo()+ " Editado com sucesso!");
            }
            carregar();
            fechar();
            if(autenticacaoBean.getAluLogado() != null){
                this.novoRequerimentoAluno();
            }
            
        } catch (Exception e) {
            Messages.addGlobalError(">>>> ERRO: Não foi possivel Salvar o Requerimento: " + cadastro.getNumeroProtocolo());
        }
    }

    /**
     * Metodo de exclusão alterado para cancelamento de requerimento
     */
    public void cancelar() {
        try {
            cadastro.setStatusRequerimento(StatusRequerimento.Cancelado);
            dao.alterar(cadastro);
            carregar();
            Messages.addGlobalInfo("Requerimento Cancelado com sucesso!");
        } catch (RuntimeException e) {
            Messages.addGlobalError(">>>> ERRO: Não foi possivel Cancelar o Requerimento: " + cadastro.getNumeroProtocolo() + " - " + e.getMessage());
        }
    }

    public void atribuir() throws IOException {
        try {
            responsavel = respDAO.buscarPorCargoCurso(cadastro.getTipoRequerimento().getSetor(),
                    cadastro.getAluno().getCurso());
            if (responsavel != null) {
                movimentacao.setResponsavel(responsavel);
                movimentacao.setDataMovimentacao(new Date());
                movimentacao.setRequerimento(cadastro);
                movimentacao.setUsuario(autenticacaoBean.getRespLogado());

                Messages.addGlobalInfo("Requerimento Atribuído com sucesso!");
                salvarMovimentacao();
            } else {
                Messages.addGlobalError(">>>> ERRO: Não foi possivel Identificar o responsável do curso " + cadastro.getAluno().getCurso().getNome());
                return;
            }
        } catch (RuntimeException e) {
            Messages.addGlobalError(">>>> ERRO: Não foi possivel Atribuir o Requerimento: " + cadastro.getNumeroProtocolo() + " - " + e.getMessage());
        }
    }

    private void salvarMovimentacao() throws IOException {
        try {
            if (movimentacao.getResponsavel() != null) {
                movDAO.incluir(movimentacao);
                Requerimento req = movimentacao.getRequerimento();
                req.setStatusRequerimento(StatusRequerimento.Em_Analise);
                dao.alterar(req);
                Messages.addGlobalInfo("Movimentação registrada com sucesso!");

                notificarResponsavel(cadastro, responsavel);

                fechar();
                carregar();
            } else {
                Messages.addGlobalError(">>>> ERRO: Não foi possivel registrar a movimentação! - (Não possui responsável) ");
                return;
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
            Messages.addGlobalError(">>>> ERRO: Não foi possivel registrar a movimentação do processo:" + cadastro.getNumeroProtocolo() + " - " + e.getMessage());
        }
    }

    /**
     * Método para revisão
     */
    public void revisar() {
        if (cadastro != null) {
            if (verificaDataLimite() == -1) {
                Messages.addGlobalError("ERRO: Data Limite atingida! não é possível solicitar revisão para este Requerimento Nº " + cadastro.getNumeroProtocolo());
                return;
            } else {
                cadastro.setRevisao(true);
                cadastro.setStatusRequerimento(StatusRequerimento.Aberto);
                dao.alterar(cadastro);
                Messages.addGlobalInfo("Solicitação de revisão gerada com sucesso! para o requerimento Nº " + cadastro.getNumeroProtocolo());
            }
        }
    }

    /**
     * Método fechar
     */
    public void fechar() {
        cadastro = new Requerimento();
        movimentacao = new Movimentacao();
        acao = "";
    }

    /**
     * Método que verifica os requerimentos em aberto com a data limite do tipo
     * de requerimento se essa data for ultrapassada o sistema cancelará o
     * requerimento automaticamente.
     *
     * se for -1 a data final é menor que a data Atual se for 0 a data final é
     * igual a data Atual se for 1 a data final é maior que a data Atual
     */
    private void verificaRequerimentosACancelar() {
        final DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        Calendar hoje = Calendar.getInstance();

        for (Requerimento req : lista) {
            if (req.getStatusRequerimento().equals(StatusRequerimento.Aberto)) {
                String reqdata = df.format(req.getTipoRequerimento().getDataLimite().getTime());
                String hj = df.format(hoje.getTime());
                System.out.println("está aberto Requerimento n: " + req.GerarNumeroProtocolo() + " - " + reqdata);
                int ret = reqdata.compareTo(hj);
                System.out.println("Retorno da Comparação: " + ret + " - reqdata: " + reqdata + " - hj: " + hj);

                if (reqdata.compareTo(hj) < 0) {
                    System.out.println("é menor : " + reqdata + " - " + hj);
                    req.setStatusRequerimento(StatusRequerimento.Cancelado);
                    dao.alterar(req);
                }
            }
        }
    }

    /**
     * Verifica se pode abrir um requerimento comparando a data.
     */
    private int verificaDataLimite() {
        final DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        Calendar hoje = Calendar.getInstance();
        String reqdata = df.format(cadastro.getTipoRequerimento().getDataLimite().getTime());
        String hj = df.format(hoje.getTime());
        int ret = reqdata.compareTo(hj);
        System.out.println("Retorno da Comparação: " + ret + " - reqdata: " + reqdata + " - hj: " + hj);

        if (reqdata.compareTo(hj) < 0) {
            System.out.println("é menor : " + reqdata + " - " + hj);
//            Messages.addGlobalError(">>>> ERRO: Data limite atingida, não é possível abrir requerimento pra esse tipo: " + cadastro.getTipoRequerimento().getNome());
            return -1;
        } else {
            return 0;
        }
    }

    /**
     * Método que notifica por email o responsável a existência de um
     * requerimento atribuido a ele.
     *
     * @param req
     * @param resp
     * @throws IOException
     */
    private void notificarResponsavel(Requerimento req, Responsavel resp) throws IOException {
        if (resp.getEmail() != null) {
            //configuração do email return configsession
            SimpleMailTemplete smt = new SimpleMailTemplete();
            MailMessage message = new MailMessageImpl(smt.enviarEmail());

            // envia variaveis para o template
            VelocityContext context = new VelocityContext();
            context.put("requerimento", req);

            //prepara o conteúdo do email em html com codificação
            StringWriter writer = smt.escreveTempate("notifica_responsavel.html", context);

            message.to(resp.getEmail())
                    .subject("Requerimento Nº " + req.getNumeroProtocolo() + " Encaminhado.")
                    .bodyHtml(writer.toString())
                    .put("locale", new Locale("pt", "BR"))
                    .send();

            Messages.addGlobalInfo("Envio de notificação para o responsável enviada com sucesso para o Requerimento Nº " + req.getNumeroProtocolo());
        } else {
            Messages.addGlobalError("ERRO: >>>> Responsável Não possui email em seu cadastro, favor cadastrar para o envio das notificações.");
            return;
        }
    }

    public Aluno getAluno() {
        return aluno;
    }

    public void setAluno(Aluno aluno) {
        this.aluno = aluno;
    }

    public Requerimento getCadastro() {
        if (cadastro == null) {
            cadastro = new Requerimento();
        }
        return cadastro;
    }

    public void setCadastro(Requerimento cadastro) {
        this.cadastro = cadastro;
    }

    public List<Requerimento> getLista() {
        return lista;
    }

    public void setLista(List<Requerimento> lista) {
        this.lista = lista;
    }

    public List<Requerimento> getLipesq() {
        return lipesq;
    }

    public void setLipesq(List<Requerimento> lipesq) {
        this.lipesq = lipesq;
    }

    public List<StatusRequerimento> getLiStatusReq() {
        return liStatusReq;
    }

    public void setLiStatusReq(List<StatusRequerimento> liStatusReq) {
        this.liStatusReq = liStatusReq;
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

    public RequerimentoFilter getFiltro() {
        if (filtro == null) {
            filtro = new RequerimentoFilter();
        }
        return filtro;
    }

    public void setFiltro(RequerimentoFilter filtro) {
        this.filtro = filtro;
    }

    public List<TipoRequerimento> getListatipo() {
        return listatipo;
    }

    public void setListatipo(List<TipoRequerimento> listatipo) {
        this.listatipo = listatipo;
    }

    public List<Aluno> getLialuno() {
        return lialuno;
    }

    public void setLialuno(List<Aluno> lialuno) {
        this.lialuno = lialuno;
    }
    
    public boolean isLiTodos() {
        return liTodos;
    }

    public void setLiTodos(boolean liTodos) {
        this.liTodos = liTodos;
    }

    public Movimentacao getMovimentacao() {
        if (movimentacao == null) {
            movimentacao = new Movimentacao();
        }
        return movimentacao;
    }

    public void setMovimentacao(Movimentacao movimentacao) {
        this.movimentacao = movimentacao;
    }

    public List<Responsavel> getLiResponsavel() {
        return liResponsavel;
    }

    public void setLiResponsavel(List<Responsavel> liResponsavel) {
        this.liResponsavel = liResponsavel;
    }

    public Responsavel getResponsavel() {
        if (responsavel == null) {
            responsavel = new Responsavel();
        }
        return responsavel;
    }

    public AutenticacaoBean getAutenticacaoBean() {
        return autenticacaoBean;
    }

    public void setAutenticacaoBean(AutenticacaoBean autenticacaoBean) {
        this.autenticacaoBean = autenticacaoBean;
    }

}
