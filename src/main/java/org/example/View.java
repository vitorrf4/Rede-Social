package org.example;
import org.apache.commons.collections.MultiMap;

import java.util.Scanner;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class View {
    Scanner sc = new Scanner(System.in);

    public String receberOpcaoPrograma() {
        System.out.println(
            "1 - Programa Admin\n" +
            "2 - Programa Usuário"
        );
        return sc.nextLine();
    }

    //Metodos admin
    public String receberOpcaoAdmin() {
        System.out.println(
            "1 - Criar usuário\n" +
            "2 - Buscar usuário por ID\n" +
            "3 - Listar todos os usuários\n" +
            "4 - Deletar usuário\n" +
            "5 - Sair do programa administrador"
        );
        return sc.nextLine();
    }

    public Usuario receberCriacaoUsuario() {
        String nome;
        String login;
        String senha;

        do {
            System.out.print("Nome: ");
            nome = sc.nextLine().trim();

            if (nome.isBlank())
                System.out.println("Nome inválido");
        } while (nome.isBlank());

        do {
            System.out.print("Login: ");
            login = sc.nextLine();

            if (login.contains(" "))
                System.out.println("Seu login não deve conter espaços");
            else if(login.isEmpty())
                System.out.println("O campo deve ser preenchido");

        } while (login.contains(" ") || login.isEmpty());

        do {
            System.out.print("Senha: ");
            senha = sc.nextLine();

            if (senha.contains(" "))
                System.out.println("Sua senha não deve conter espaços");
            else if(senha.isEmpty())
                System.out.println("O campo deve ser preenchido");
        } while (senha.contains(" ") || senha.isEmpty());

        return new Usuario(nome, login, senha);
    }

    public Long receberIdUsuario() {
        System.out.print("Digite o ID do usuário: ");

        return Long.parseLong(sc.nextLine());
    }

    public void mostrarUsuario(Usuario usuario) {
        System.out.println(
            "ID: " + usuario.getId() + " | " +
            "Nome: " + usuario.getNome() + " | " +
            "Login: " + usuario.getLogin() + " | " +
            "Senha: " + usuario.getSenha()
        );
    }

    public void mostrarTodosUsuarios(List<Usuario> todosUsuarios) {
        for (Usuario usuario : todosUsuarios) {
            System.out.println(
                "ID: " + usuario.getId() + " | " +
                "Nome: " + usuario.getNome() + " | " +
                "Login: " + usuario.getLogin() + " | " +
                "Senha: " + usuario.getSenha()
            );
        }
    }

    //Metodos usuario
    public Usuario receberLogin() {
        Usuario usuario = new Usuario();

        System.out.print("Login: ");
        String login = sc.nextLine();
        usuario.setLogin(login);
        System.out.print("Senha: ");
        String senha = sc.nextLine();
        usuario.setSenha(senha);
        return usuario;
    }

    public String receberOpcaoUsuario() {
        System.out.println(
            "1 - Ver seu perfil\n" +
            "2 - Ver timeline\n" +
            "3 - Mostrar todos os usuários da rede\n" +
            "4 - Postar mensagem\n" +
            "5 - Sair do programa usuário" 
        );
        return sc.nextLine();
    }

    public void mostrarPerfil(Usuario usuario) {
        System.out.println(
            usuario.getNome() + "\n" +
            " @" + usuario.getLogin() + "\n" +
            "Seguidores : " + usuario.getSeguidores() +
            "\tSeguindo : " + usuario.getSeguindo().size()  
        );
    }

    public void mostrarPostagens(Iterator<String> mensagens) {
        System.out.println("Postagens: ");

        while (mensagens.hasNext()) {
            System.out.println("\t\"" + mensagens.next() + "\"");
        }
    }

    public void mostrarTodosPerfis(List<Usuario> usuarios) {
        for (Usuario usuario : usuarios) {
            String seguidorString = (usuario.getSeguidores() == 1) ? "Seguidor" : "Seguidores";

            System.out.println(
                    "Usuario: " + usuario.getLogin() +
                            " | " + usuario.getSeguidores() + " " + seguidorString + " | " +
                            "Postagens: " + usuario.getMensagens().size()
            );
        }
    }

    public void mostrarTimeline(MultiMap timeline) {
        System.out.println("\t-- TIMELINE --");

        for (Object autor : timeline.keySet()) {
            System.out.println("@" + autor.toString() + " diz");

            for (String mensagem : (List<String>)timeline.get(autor)) {
                System.out.printf("  '%s'\n", mensagem);
            }

        }
    }

    public String receberSubOpcoesSeguir() {
        System.out.println(
            "1 - Seguir usuário\n" +
            "2 - Voltar"
        );
        return sc.nextLine();
    }

    public String receberNomePerfil() {
        System.out.println("Digite o nome do usuario a ser seguido: ");

        return sc.nextLine();
    }

    public String receberPostagem() {
        String mensagem;

        do {
            System.out.println("Digite sua mensagem, ela não deve conter menos de 250 caracteres : ");
            mensagem = sc.nextLine();

            if (mensagem.length() > 250)
                System.out.println("A mensagem excede o limite de caracteres");
        } while(mensagem.length() > 250);

        System.out.println("Mensagem postada com sucesso!");
        return mensagem;
    }

    //Mensagens
    public void mostrarErroLogin() {
        System.out.println("Um usuario com este login já existe, tente novamente");
    }
    public void mostrarBemVindo(Usuario usuario) {
        System.out.println("Bem-Vindo " + usuario.getLogin());
    }
    public void mostrarUsuarioRemovido() {
        System.out.println("Usuário removido com sucesso!");
    }
    public void mostrarOpcaoInvalida() {
        System.out.println("Opção inválida");
    }
    public void mostrarUsuarioInvalido() {
        System.out.println("Usuário não encontrado");
    }
    public void mostrarLoginInvalido() {
        System.out.println("Login ou senha inválidos");
    }
    public void mostrarSeguindo(Usuario usuarioSeguido) {
        System.out.println("Seguindo " + usuarioSeguido.getLogin() + "!");
    }
    public void mostrarTimelineVazia() {
        System.out.println("Nenhuma postagem no momento");
    }
    public void mostrarNenhumUsuario() {
        System.out.println("Nenhum usuário cadastrado");
    }
}
