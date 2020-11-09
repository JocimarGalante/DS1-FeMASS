package com.femass.ds1.requerimentosfemass.bean;

import com.femass.ds1.requerimentosfemass.dao.DocumentoDao;
import com.femass.ds1.requerimentosfemass.dao.TipoRequerimentoDao;
import com.femass.ds1.requerimentosfemass.model.Cargo;
import com.femass.ds1.requerimentosfemass.model.Documento;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.event.SelectEvent;
import com.femass.ds1.requerimentosfemass.model.TipoRequerimento;
import java.util.Arrays;
import javax.ejb.EJB;
import javax.faces.event.ActionEvent;
import org.omnifaces.util.Messages;

@ManagedBean
@ViewScoped
public class TiporequerimentoBean {

    private TipoRequerimento cadastro;
    private List<TipoRequerimento> lista;
    private List<Documento> lidocs;
    private List<Documento> lidocsalterar;
    private List<Documento> lidocsexcluir;
    private List<Cargo> lisetor;
    private int size;
    private Documento documento;
    private String acao;

    @EJB
    TipoRequerimentoDao dao;
    
    @EJB
    DocumentoDao docDAO;

    /**
     * Metodo de abertura
     */
    public void carregar() {
        try {
            lista = dao.getTipoRequerimentos();
            size = lista.size();
            lisetor = Arrays.asList(Cargo.values());
            lidocs = new ArrayList<>();
            lidocsalterar = new ArrayList<>();
            lidocsexcluir = new ArrayList<>();

        } catch (RuntimeException e) {
            Messages.addGlobalError(">>>> ERRO: Não foi possível carregar os tipos de Requerimento.");
        }
    }

    /**
     * Metodo de selecao do data table pela barra de botoes
     *
     * @param evento
     */
    public void onRowSelect(SelectEvent evento) {
        cadastro = (TipoRequerimento) evento.getObject();
        lidocs = cadastro.getDocumentos();
        acao = "Editar";
    }

    /**
     * Metodo Bean novo - Cria um novo objeto.
     */
    public void novo() {
        cadastro = new TipoRequerimento();
        documento = new Documento();
        lidocs = new ArrayList<>();
        acao = "Salvar";
        cadastro.setAtivo(Boolean.TRUE);
    }

    /**
     * Metodo salva e edita
     */
//    public void merge() {
//        try {
//            if (acao.equals("salvar")) {
//                dao.incluir(cadastro);
//            } else {
//                dao.alterar(cadastro);
//            }
//            carregar();
//            novo();
//            Messages.addGlobalInfo("Tipo de Requerimento Salvo com sucesso!");
//        } catch (RuntimeException e) {
//            Messages.addGlobalError(">>>> ERRO: Não foi possivel Salvar o Tipo de Requerimento: " + cadastro.getNome());
//        }
//    };

    public void merge() {
        try {
            // cadastrando
            if (acao.equals("Salvar") && cadastro.getId() == null) {
                dao.mergeTipDoc(cadastro, lidocs);

                Messages.addGlobalInfo("Tipo de Requerimento: " + cadastro.getNome() + " Salvo com Sucesso.");

                // zerando a lista
                lidocs = new ArrayList<>();

                // Editando
            } else if ((acao.equals("Editar") && cadastro != null)) {
                verificaLista(); // identifica quem será alterado

                if (lidocsalterar.isEmpty()) {
                    System.out.println(">>>>>>> Lista de documentos alterar está vazia >>>>>>>");
                    dao.mergeTipDoc(cadastro, null);
                } else {
                    System.out.println(">>>>>>> chamar método para alterar lista de documentos >>>>>>>");
                    dao.mergeTipDoc(cadastro, lidocsalterar);
                }

                if (lidocsexcluir.isEmpty()) {
                    System.out.println(">>>>>>> Lista de documentos excluir está vazia >>>>>>>");
                } else {
                    System.out.println(">>>>>>> chamar método para excluir lista de documentos >>>>>>>");
                    this.excluirLidoc(lidocsexcluir);
                }
                Messages.addGlobalInfo("Tipo de Requerimento: " + cadastro.getNome() + " Editado com Sucesso.");
            }
            fechar();

        } catch (RuntimeException e) {
            Messages.addGlobalError("Erro ao tentar incluir ou alterar um Tipo de Requerimento"
                    + cadastro.getNome() + e.getMessage());
        }
    }

    /**
     * Varre a lista de itens a excluir e exclui os itens.
     * @param lidocEx 
     */
    private void excluirLidoc(List<Documento> lidocEx) {
        for (int posicao = 0; posicao < lidocEx.size(); posicao++) {
            Documento doc = lidocEx.get(posicao);
            docDAO.excluir(doc);
        }
    }

    /**
     * Metodo de exclusão
     */
    public void excluir() {
        try {
            dao.excluir(cadastro);
            carregar();
            Messages.addGlobalInfo("Tipo de Requerimento excluído com sucesso!");
        } catch (RuntimeException e) {
            Messages.addGlobalError(">>>> ERRO: Não foi possivel excluir o Tipo de Requerimento: " + cadastro.getNome() + " - " + e.getMessage());
        }
    }

    public void fechar() {
        novo();
        lidocs = new ArrayList<>();
        lidocsalterar = new ArrayList<>();
        lidocsexcluir = new ArrayList<>();
    }

    public void adicionar() {
        lidocs.add(documento);
        System.out.println("Adicionei na lista " + documento.getTitulo());
        documento = new Documento();
    }

    public void remover(ActionEvent evento) {
        Documento doc = (Documento) evento.getComponent().getAttributes().get("docSel");
        System.out.println("Documento linha " + doc.getTitulo() + " - " + doc.getLink());

        int achou = -1;
        for (int posicao = 0; posicao < lidocs.size(); posicao++) {
            if (lidocs.get(posicao).getTitulo().equals(doc.getTitulo())) {
                System.out.println("Item Encontrado e será removido " + lidocs.get(posicao).getTitulo());
                achou = posicao;
            }
        }
        if (achou > -1) {
            lidocs.remove(achou);
        }
    }

    private void verificaLista() {
        List<Documento> listaBanco = docDAO.listarDocsTP(cadastro);
        Documento itemBanco, itemEditado;
        // deixa as duas listas com o mesmo tamanho e mesmos itens
        lidocsexcluir = listaBanco;
        int parar = 0;

        // varrendo a lista que veio nova com cada item do banco
        for (int posEdit = 0; posEdit < lidocs.size(); posEdit++) {
            itemEditado = lidocs.get(posEdit);

            if (parar == 0 && lidocsexcluir.size() == 0) {
                lidocsalterar.add(itemEditado);
                System.out.println("Item da Posição Edição: " + posEdit + " - cod: " + itemEditado.getId() + " - desc: " + itemEditado.getTitulo());
            }

            parar = lidocsexcluir.size();

            for (int posBanco = 0; posBanco < parar; posBanco++) {
                itemBanco = listaBanco.get(posBanco);

                // significa que existe nesta posição
                if (itemEditado.getId().equals(itemBanco.getId())) {
                    lidocsexcluir.remove(itemBanco);
                    System.out.println(">>>>>>> iguais removido >>>>>>>");

                    if ((!itemBanco.getTitulo().equals(itemEditado.getTitulo()))
                            || (!itemBanco.getLink().equals(itemEditado.getLink()))) {

                        lidocsalterar.add(itemEditado);
                        System.out.println(">>>>>>> adicionado para alterar >>>>>>>");
                    }
                    parar = posBanco;
                } else {
                    // significa que pode existir em outra posição ou pode ser item novo
                    if (itemEditado.getId() == null) {
                        lidocsalterar.add(itemEditado);
                        System.out.println(">>>>>>> adicionado para alterar >>>>>>>");
                        parar = posBanco;
                    }
                }
            }
        }
    }

    //gets e sets 
    public TipoRequerimento getCadastro() {
        if (cadastro == null) {
            cadastro = new TipoRequerimento();
        }
        return cadastro;
    }

    public void setCadastro(TipoRequerimento cadastro) {
        this.cadastro = cadastro;
    }

    public List<TipoRequerimento> getLista() {
        return lista;
    }

    public void setLista(List<TipoRequerimento> lista) {
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

    public List<Documento> getLidocs() {
        return lidocs;
    }

    public void setLidocs(List<Documento> lidocs) {
        this.lidocs = lidocs;
    }

    public Documento getDocumento() {
        if (documento == null) {
            documento = new Documento();
        }
        return documento;
    }

    public void setDocumento(Documento documento) {
        this.documento = documento;
    }

    public List<Cargo> getLisetor() {
        return lisetor;
    }

    public void setLisetor(List<Cargo> lisetor) {
        this.lisetor = lisetor;
    }

}
