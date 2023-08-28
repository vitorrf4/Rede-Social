package org.example;

import org.apache.commons.collections.MultiMap;

import java.util.Iterator;
import java.util.List;

public class Controller {
    Model model;
    View view;

    public Controller(Model model, View view){
        this.model = model;
        this.view = view;
        selecionarAdminOuUsuario();
    }

    public void selecionarAdminOuUsuario() {
        while (true) {
            String escolhaPrograma = view.receberOpcaoPrograma();
    
            switch (escolhaPrograma) {
                case "1" -> selecionarOpcoesAdmin();
                case "2" -> selecionarOpcoesUsuario();
                default -> view.mostrarOpcaoInvalida();
            }
        }

    }

    //Metódos admin
    public void selecionarOpcoesAdmin() {
        opcoesAdmin : while (true) {
            String escolha = view.receberOpcaoAdmin();
            switch (escolha) {
                case "1" -> criarUsuario();
                case "2" -> buscarUsuarioPorId();
                case "3" -> buscarTodosUsuarios();
                case "4" -> desativarUsuario();
                case "5" -> { break opcoesAdmin;}
                default -> view.mostrarOpcaoInvalida();
            }

        }
    }

    public void criarUsuario() {
        boolean sucesso;

        do {
            Usuario usuarioCriado = view.receberCriacaoUsuario();
            sucesso = model.persistirUsuario(usuarioCriado);

            if (!sucesso)
                view.mostrarErroLogin();
        } while (!sucesso) ;
    }

    public void buscarUsuarioPorId() {
        try {
            Long id = view.receberIdUsuario();
            Usuario usuarioBuscado = model.buscarUsuario(id);
            view.mostrarUsuario(usuarioBuscado);

        } catch (NullPointerException e) {
            view.mostrarUsuarioInvalido();
        } catch (NumberFormatException e) {
            view.mostrarOpcaoInvalida();
        }
    }

    public void buscarTodosUsuarios() {
        List<Usuario> todosUsuarios = model.buscarTodosUsuariosAtivos();

        if (todosUsuarios.isEmpty()) {
            view.mostrarNenhumUsuario();
            return;
        }

        view.mostrarTodosUsuarios(todosUsuarios);
    }

    public void desativarUsuario() {
        try {
            Long id = view.receberIdUsuario();
            Usuario usuarioInativado = model.buscarUsuario(id);
            model.desativarUsuario(usuarioInativado);
            view.mostrarUsuarioRemovido();

        } catch (NumberFormatException e) {
            view.mostrarOpcaoInvalida();
        } catch (NullPointerException e) {
            view.mostrarUsuarioInvalido();
        }
    }

    //Métodos usuário
    public void selecionarOpcoesUsuario() {
        Usuario usuarioLogado =  tentarLogin();
        
        opcoesUsuario : while (true) {
            String escolha = view.receberOpcaoUsuario();

            switch (escolha) {
                case "1" -> verPerfil(usuarioLogado);
                case "2" -> mostrarTimeline(usuarioLogado);
                case "3" -> {
                    mostrarUsuarios();
                    escolherOpcoesSeguir(usuarioLogado);
                }
                case "4" -> postarMensagem(usuarioLogado);
                case "5" -> { break opcoesUsuario; }
                default -> view.mostrarOpcaoInvalida();
            }
        }
    }

    public Usuario tentarLogin() {
        Usuario usuarioEncontrado;

        do {
            Usuario usuarioEntrado = view.receberLogin();
            usuarioEncontrado = model.compararLoginSenha(usuarioEntrado);

            if (usuarioEncontrado == null)
                view.mostrarLoginInvalido();
        } while (usuarioEncontrado == null);
        
        view.mostrarBemVindo(usuarioEncontrado);
        return usuarioEncontrado;
    }

    public void verPerfil(Usuario usuario) {
        view.mostrarPerfil(usuario);
        Iterator<String> mensagens = model.buscarPostagens(usuario);
        view.mostrarPostagens(mensagens);
    }

    public void mostrarUsuarios() {
        List<Usuario> usuarios = model.buscarTodosUsuariosAtivos();
        view.mostrarTodosPerfis(usuarios);
    }

    public void mostrarTimeline(Usuario usuarioLogado) {
        MultiMap mensagens =  model.buscarTimeline(usuarioLogado);

        if (mensagens != null && !mensagens.isEmpty())
            view.mostrarTimeline(mensagens);
        else
            view.mostrarTimelineVazia();
    }

    public void postarMensagem(Usuario usuario) {
        String mensagem = view.receberPostagem();
        model.salvarMensagem(mensagem, usuario);
    }

    public void escolherOpcoesSeguir(Usuario usuarioLogado) {
        String escolha = "0";

        opcoesSeguir: while (escolha != "1" || escolha != "2") {
            escolha = view.receberSubOpcoesSeguir();

            switch (escolha) {
                case "1":
                    seguirUsuario(usuarioLogado);
                    break;
                case "2":
                    break opcoesSeguir;
                default:
                    view.mostrarOpcaoInvalida();
                    break;
            }
        }
    }

    public void seguirUsuario(Usuario usuarioLogado) {
        String nomeUsuario = view.receberNomePerfil();
        Usuario usuarioASeguir = model.buscarUsuario(nomeUsuario);

        if (usuarioASeguir == null) {
            view.mostrarUsuarioInvalido();
            return;
        }

        boolean sucesso = model.seguirUsuario(usuarioLogado, usuarioASeguir);

        if (!sucesso)
            view.mostrarOpcaoInvalida();
        else
            view.mostrarSeguindo(usuarioASeguir);
    }

}

