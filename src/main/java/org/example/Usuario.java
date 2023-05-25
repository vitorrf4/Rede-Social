package org.example;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.HashSet;

@Entity
@Table(name = "usuario")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String login;
    private String senha;
    private boolean ativo;
    private int seguidores;
    private HashSet<Long> seguindo = new HashSet<Long>();
    @Lob
    @Column(length = 251) // Sem essas duas propriedades o programa não consegue receber mensagens de 100 caracteres sem crashar
    private ArrayList<String> mensagens = new ArrayList<String>();
    
    //Hibernate não funciona sem o construtor padrão
    public Usuario(){}
      
    public Usuario(String nome, String login, String senha){
        this.nome = nome;
        this.login = login;
        this.senha = senha;
        this.ativo = true;
    }
    public Long getId(){
        return id;
    }
    public void setId(Long id){
        this.id = id;
    }
    public String getNome(){
        return nome;
    }
    public void setNome(String name){
        this.nome = name;
    }
    public String getLogin(){
        return login;
    }
    public void setLogin(String login){
        this.login = login;
    }
    public String getSenha(){
        return senha;
    }
    public void setSenha(String senha){
        this.senha = senha;
    }
    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }
    public boolean getAtivo(){
        return this.ativo;
    }
    public int getSeguidores() {
        return seguidores;
    }
    public void setSeguidores(int seguidores) {
        this.seguidores = seguidores;
    }
    public HashSet<Long> getSeguindo() {
        return seguindo;
    }
    public ArrayList<String> getMensagens() {
        return mensagens;
    }

}
