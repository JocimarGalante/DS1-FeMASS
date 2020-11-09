package com.femass.ds1.requerimentosfemass.util;

import java.util.Map;

import javax.faces.application.Application;
import javax.faces.application.NavigationHandler;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

import org.omnifaces.util.Messages;

import com.femass.ds1.requerimentosfemass.bean.AutenticacaoBean;
import com.femass.ds1.requerimentosfemass.model.Aluno;
import com.femass.ds1.requerimentosfemass.model.Responsavel;

//Atenção esse PhaseLister tem que ser registrado no faces-config.xml
@SuppressWarnings("serial")
public class AutenticacaoPhaseListener implements PhaseListener {

    @Override
    public void afterPhase(PhaseEvent event) {
        // depois do restore_view saber onde estou 
        FacesContext facesContext = event.getFacesContext();
        //serve pra navegar na arvore de componentes pegando a p�gina selecionada
        UIViewRoot uiViewRoot = facesContext.getViewRoot();

        String paginaAtual = uiViewRoot.getViewId();
        System.out.println(paginaAtual);

        //caso de exclusão das paginas  é preciso criar para cada pagina publica que eu tiver
        boolean ehPaginaAutenticacao = paginaAtual.contains("autenticacao.xhtml");
        System.out.println("ehPaginaAutenticacao: " + ehPaginaAutenticacao);

        //Agora vou fazer uma condição para as paginas que não são publicas se eu tiver mais de uma pagina é só concatenar no if.
        //exemplo de concatenação:if(!ehPaginaAutenticacao+!ehPaginaLiberada1+!ehPaginaLiberada2){...
        //se não for pagina de autenticação ou pagina de result pesquisa
        if (!ehPaginaAutenticacao) {
            // pra verificar o tempo da sessão
            // pegando da aplicação e forçando ir pra pagina de autenticação se nao estiver autenticado.	
            Application application = facesContext.getApplication();
            NavigationHandler navigationHandler = application.getNavigationHandler();

            ExternalContext externalContext = facesContext.getExternalContext();
            Map<String, Object> mapa = externalContext.getSessionMap();
            AutenticacaoBean autenticacaoBean = (AutenticacaoBean) mapa.get("autenticacaoBean");

            if (autenticacaoBean == null) {
                Messages.addGlobalError("Sessão expirada");
                navigationHandler.handleNavigation(facesContext, null, "/comum/autenticacao/autenticacao.xhtml?faces-redirect=true");
            } else {
                Responsavel responsavel = autenticacaoBean.getRespLogado();
                Aluno aluno = autenticacaoBean.getAluLogado();

                if (responsavel.getCargo() == null) {
                    //mensagem de erro
                    if (aluno.getMatricula() == null) {
                        Messages.addGlobalError("Usuário não autenticado.");
                        navigationHandler.handleNavigation(facesContext, null, "/comum/autenticacao/autenticacao.xhtml?faces-redirect=true");

                    }
                }
            }
        }

    }

    @Override
    public void beforePhase(PhaseEvent event) {
        // antes do restore_view

    }

    @Override
    public PhaseId getPhaseId() {
        // define em que fase o phaselistener vai agir 
        //iremos agir no restore_view
        return PhaseId.RESTORE_VIEW;
    }

}
