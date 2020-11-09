package com.femass.ds1.requerimentosfemass.util;

public class Constantes {
//	public static final String PATH_MERGE = "C:/Merge"; 
	
	/*Pasta do sistema*/
	public static final String PATH_SIS = "C:/SisRequerimento";
	
	/*Comum*/
	public static final String PATH_LOGO_EMPRESA = "logo_empresa.png";
	public static final String PATH_IMAGEM = "Branco.png";

	/*Documentos*/
	public static final String PATH_DOC = PATH_SIS + "/Documentos/";
	
	/*Informações do sistema*/
	private String DEVELOPED = "Developed by Group DS1";
	private String VERSAO = "Ver.: 0.0.1";
	private String COPI = "© 2018";
	
	/*gets e sets*/
	public String getDEVELOPED() {
		return DEVELOPED;
	}
	public String getVERSAO() {
		return VERSAO;
	}
	public String getCOPI() {
		return COPI;
	}
}	
