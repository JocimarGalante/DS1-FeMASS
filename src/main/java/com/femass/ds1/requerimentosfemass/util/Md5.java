package com.femass.ds1.requerimentosfemass.util;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * @author Alex
 */
public class Md5 {

    public void mostraSenha(String senha){
        System.out.println("Senha: "+ senha +" Criptografada: " + DigestUtils.md5Hex(senha));
    }
    
    public static void main(String[] args) {
       Md5 criptografia = new Md5();
       criptografia.mostraSenha("teste");
    }

}
