package com.femass.ds1.requerimentosfemass.util;

import java.io.IOException;
import java.util.Properties;

import com.outjected.email.api.SessionConfig;
import com.outjected.email.impl.SimpleMailConfig;
import java.io.StringWriter;
import javax.faces.context.FacesContext;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;

public class SimpleMailTemplete {

    /**
     * Método reponsável por pegar as configurações do arquivo de configurações
     * do email e retorna um configsession
     *
     * @return
     * @throws IOException
     */
    public SessionConfig enviarEmail() throws IOException {
        Properties props = new Properties();
        props.load(getClass().getResourceAsStream("/emails/configSimpleMail.properties"));

        SimpleMailConfig config = new SimpleMailConfig();
        config.setServerHost(props.getProperty("mail.smtp.host"));
        config.setServerPort(Integer.parseInt(props.getProperty("mail.smtp.port")));
        config.setEnableSsl(Boolean.parseBoolean(props.getProperty("mail.smtp.ssl")));
        config.setEnableTls(Boolean.parseBoolean(props.getProperty("mail.smtp.starttls.enable")));
        config.setAuth(Boolean.parseBoolean(props.getProperty("mail.smtp.auth")));
        config.setUsername(props.getProperty("mail.username"));
        config.setPassword(props.getProperty("mail.password"));

        // implementando as configurações
        SessionConfig configsession = config;
        return configsession;

        /*fazer esta parte no bean*/
 /*MailMessage message = new MailMessageImpl(configsession);

	message.to("alexmansan@gmail.com").subject("Pedido de Envio de senha.")
            .bodyHtml(new VelocityTemplate(getClass().getResourceAsStream("/emails/" + template)))
            .put("usuario", funcionarioCadastro)
            .put("locale", new Locale("pt", "BR"))
            .send();*/
    }

    /**
     * Método escreve o template codificando em utf-8. 
     * Precisa:
     * 1) nome do arquivo referente ao template 
     * 2) um VelocityContext onde contém as variáveis com as informações que serão mostradas no template.
     *
     * @param nomeTemplate
     * @param context
     * @return
     */
    public StringWriter escreveTempate(String nomeTemplate, VelocityContext context) {
//      setando propriedades
//      Properties props = new Properties();
//      props.put("input.encoding", "utf-8");

        // inicializando o velocity  
        VelocityEngine ve = new VelocityEngine();
        ve.setProperty(RuntimeConstants.FILE_RESOURCE_LOADER_PATH, FacesContext.getCurrentInstance().getExternalContext().getRealPath("/resources/template_velocity/"));
        ve.init();
//      ve.init(props);
        // escolhendo o template  
        Template t = ve.getTemplate(nomeTemplate, "UTF-8");
        // criando o contexto que liga o java ao template  
//        VelocityContext context = new VelocityContext();
        // envia variaveis para o template
//        context.put("responsavel", resp);
        StringWriter writer = new StringWriter();
        // mistura o contexto com o template  
        t.merge(context, writer);
//      saida writer
//        System.out.println("resultado do writer = " + writer.toString());
        return writer;
    }
}
