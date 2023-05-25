package org.example;

import java.util.Iterator;
import java.util.List;

public class Controller {
    Model model;
    View view;

    public Controller(Model model, View view){
        this.model = model;
        this.view = view;
        selecionaAdminOuUsuario();
    }

    public void selecionaAdminOuUsuario(){
        while(true){
            String escolhaPrograma = view.recebeOpcaoPrograma();
    
            switch(escolhaPrograma){
                case "1":
                    selecionaOpcoesAdmin();
                    break;
                case "2":
                    selecionaOpcoesUsuario();
                    break;
                default:
                    view.mostraOpcaoInvalida();
                break;
            }

        }
    }

    //Metódos admin
    public void selecionaOpcoesAdmin(){
        opcoesAdmin : while(true){
            String escolha = view.recebeOpcaoAdmin();
            switch(escolha){
                case "1":   
                    criaUsuario();
                    break;
                case "2":
                    buscaUsuarioPorId();
                    break;
                case "3":
                    buscaTodosUsuarios();
                    break;
                case "4":
                    desativaUsuario();
                    break;
                case "5":
                    break opcoesAdmin;
                default:
                    view.mostraOpcaoInvalida();
                    break;
            }

        }
    }

    public void criaUsuario(){
        boolean sucesso = false;
        do{
            Usuario usuarioCriado = view.recebeCriacaoUsuario(); 
            sucesso = model.persisteUsuario(usuarioCriado);

            if(sucesso == false){
                view.mostraErroLogin();
            }
        } while(sucesso != true);
    }

    public void buscaUsuarioPorId(){
        String id = view.recebeIdUsuario();
        Usuario usuarioBuscado = model.buscaUsuarioPorId(id);
        
        if (usuarioBuscado != null){
            view.mostraUsuario(usuarioBuscado);
        } else {
            view.mostraUsuarioInvalido();
        }
    }
    public void buscaTodosUsuarios(){
        List<Usuario> todosUsuarios = model.buscaTodosUsuariosAtivos();

        if (todosUsuarios.size() > 0){
            view.mostraTodosUsuarios(todosUsuarios);
        } else {
            view.mostraNenhumUsuario();
        }
    }

    public void desativaUsuario(){
        String id = view.recebeIdUsuario();
        Usuario usuarioInativado = model.buscaUsuarioPorId(id);

        if (usuarioInativado != null){
            model.desativaUsuario(usuarioInativado);
            view.mostraUsuarioRemovido();
        } else {
            view.mostraUsuarioInvalido();
        }
    }

    //Métodos usuário
    public void selecionaOpcoesUsuario(){
        Usuario usuarioLogado =  tentaLogin();
        
        opcoesUsuario : while(true){
            String escolha = view.recebeOpcaoUsuario();

            switch(escolha){
                case "1":
                    vePerfil(usuarioLogado);
                    break;
                case "2":
                    mostraTimeline(usuarioLogado);
                    break;
                case "3":
                    mostraUsuarios();
                    escolheOpcoesSeguir(usuarioLogado);
                    break;
                case "4":
                    postaMensagem(usuarioLogado);
                    break;
                case "5":
                    break opcoesUsuario;
                default:
                    view.mostraOpcaoInvalida();
                    break;
            }
        }
    }
    public Usuario tentaLogin(){
        Usuario usuarioEncontrado = null;

        do{
            Usuario usuarioEntrado = view.recebeLogin();
            usuarioEncontrado = model.comparaLoginSenha(usuarioEntrado);

            if(usuarioEncontrado == null){
                view.mostraLoginInvalido();
            }
        } while(usuarioEncontrado == null);
        
        view.mostraBemVindo(usuarioEncontrado);
        return usuarioEncontrado;
    }

    public void vePerfil(Usuario usuario){
        view.mostraPerfil(usuario);
        Iterator<String> mensagens = model.buscaPostagens(usuario);
        view.mostraPostagens(mensagens);
    }

    public void mostraUsuarios(){
        List<Usuario> usuarios = model.buscaTodosUsuariosAtivos();
        view.mostraTodosPerfis(usuarios);
    }
    public void mostraTimeline(Usuario usuarioLogado){
        String[][] mensagens =  model.buscaTimeline(usuarioLogado);

        if(mensagens != null){
            view.mostraTimeline(mensagens);
        } else {
            view.mostraTimelineVazia();
        }
    }
    public void postaMensagem(Usuario usuario){
        String mensagem = view.recebePostagem();
        model.salvaMensagem(mensagem, usuario);
    }

    public void escolheOpcoesSeguir(Usuario usuarioLogado){
        String escolha = "0";

        subOpcoesSeguir: while(escolha != "1" || escolha != "2"){
            escolha = view.recebeSubOpcoesSeguir();

            switch(escolha){
                case "1":
                    segueUsuario(usuarioLogado);
                    break;
                case "2":
                    break subOpcoesSeguir;
                default:
                    view.mostraOpcaoInvalida();
                    break;
            }
        }
    }
    public void segueUsuario(Usuario usuarioLogado){
        String nomeUsuario = view.recebeNomePerfil();
        Usuario usuarioASeguir = model.buscaUsuarioPorLogin(nomeUsuario);

        if(usuarioASeguir != null){
            boolean sucesso = model.seguirUsuario(usuarioLogado, usuarioASeguir);
            if(sucesso != true){
                view.mostraOpcaoInvalida();
            } else {
                view.mostraSeguindo(usuarioASeguir);
            }
        } else {
            view.mostraUsuarioInvalido();
        }
    }

}

