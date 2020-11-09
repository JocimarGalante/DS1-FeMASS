package com.femass.ds1.requerimentosfemass.model;

/**
 * Objeto para manipular as informações recebidas do web Service
 * @author Alex
 */
public class AlunoSerFemass {
    
    String nome; //"nome": "Alex Manhães dos Santos"
    String cpf; //"cpf": "107.877.577-05",
    String cr;  //"cr": 5.1,
    String curso; //"curso": "Sistemas de Informação",
    String email; //"email": "alexmansan@gmail.com",
    String endereco; //"endereco": "Rua Tim Maia ",
    String enderecoBairro; //"enderecoBairro": "Barramares",
    String enderecoCep; //"enderecoCep": "27900-000",
    String enderecoCidade; //"enderecoCidade": "Macaé",
    String enderecoComplemento; //"enderecoComplemento": "casa",
    String enderecoNumero; //"enderecoNumero": "160"

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getCr() {
        return cr;
    }

    public void setCr(String cr) {
        this.cr = cr;
    }

    public String getCurso() {
        return curso;
    }

    public void setCurso(String curso) {
        this.curso = curso;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getEnderecoBairro() {
        return enderecoBairro;
    }

    public void setEnderecoBairro(String enderecoBairro) {
        this.enderecoBairro = enderecoBairro;
    }

    public String getEnderecoCep() {
        return enderecoCep;
    }

    public void setEnderecoCep(String enderecoCep) {
        this.enderecoCep = enderecoCep;
    }

    public String getEnderecoCidade() {
        return enderecoCidade;
    }

    public void setEnderecoCidade(String enderecoCidade) {
        this.enderecoCidade = enderecoCidade;
    }

    public String getEnderecoComplemento() {
        return enderecoComplemento;
    }

    public void setEnderecoComplemento(String enderecoComplemento) {
        this.enderecoComplemento = enderecoComplemento;
    }

    public String getEnderecoNumero() {
        return enderecoNumero;
    }

    public void setEnderecoNumero(String enderecoNumero) {
        this.enderecoNumero = enderecoNumero;
    }
    
}
