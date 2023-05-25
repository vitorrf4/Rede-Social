package org.example;
import java.util.Scanner;
import java.util.Iterator;
import java.util.List;

public class View {
    Scanner sc = new Scanner(System.in);

    public String recebeOpcaoPrograma(){
        System.out.println(
            "1 - Programa Admin\n" +
            "2 - Programa Usuário"
        );
        String escolha = sc.nextLine();
        return escolha;
    }

    //Metodos admin
    public String recebeOpcaoAdmin(){
        System.out.println(
            "1 - Criar usuário\n" +
            "2 - Buscar usuário por ID\n" +
            "3 - Listar todos os usuários\n" +
            "4 - Deletar usuário\n" +
            "5 - Sair do programa administrador"
        );
        String escolha = sc.nextLine();
        return escolha;
    }

    public Usuario recebeCriacaoUsuario(){
        String nome;
        String login;
        String senha;

        do {
            System.out.print("Nome: ");
            nome = sc.nextLine().trim();

            if(nome.isBlank()){
                System.out.println("Nome inválido");
            }
        } while (nome.isBlank());

        do {
            System.out.print("Login: ");
            login = sc.nextLine();

            if(login.contains(" ")){
                System.out.println("Seu login não deve conter espaços");
            } else if(login.isEmpty()){
                System.out.println("O campo deve ser preenchido");
            }
        } while (login.contains(" ") || login.isEmpty());

        do {
            System.out.print("Senha: ");
            senha = sc.nextLine();

            if(senha.contains(" ")){
                System.out.println("Sua senha não deve conter espaços");
            } else if(senha.isEmpty()){
                System.out.println("O campo deve ser preenchido");
            }
        } while (senha.contains(" ") || senha.isEmpty());

        Usuario usuario = new Usuario(nome, login, senha);
        return usuario;
    }
    public String recebeIdUsuario(){
        System.out.print("Digite o ID do usuário: ");
        String id = sc.nextLine();
        return id;
    }
    public void mostraUsuario(Usuario usuario){
        System.out.println(
            "ID: " + usuario.getId() + " | " +
            "Nome: " + usuario.getNome() + " | " +
            "Login: " + usuario.getLogin() + " | " +
            "Senha: " + usuario.getSenha()
        );
    }
    public void mostraTodosUsuarios(List<Usuario> todosUsuarios){
        for(Usuario usuario : todosUsuarios){
            System.out.println(
                "ID: " + usuario.getId() + " | " +
                "Nome: " + usuario.getNome() + " | " +
                "Login: " + usuario.getLogin() + " | " +
                "Senha: " + usuario.getSenha()
            );
        }
    }

    //Metodos usuario
    public Usuario recebeLogin(){
        Usuario usuario = new Usuario();

        System.out.print("Login: ");
        String login = sc.nextLine();
        usuario.setLogin(login);
        System.out.print("Senha: ");
        String senha = sc.nextLine();
        usuario.setSenha(senha);
        return usuario;
    }
    public String recebeOpcaoUsuario(){
        System.out.println(
            "1 - Ver seu perfil\n" +
            "2 - Ver timeline\n" +
            "3 - Mostrar todos os usuários da rede\n" +
            "4 - Postar mensagem\n" +
            "5 - Sair do programa usuário" 
        );
        String escolha = sc.nextLine();
        return escolha;
    }
    public void mostraPerfil(Usuario usuario){
        System.out.println(
            usuario.getNome() + "\n" +
            " @" + usuario.getLogin() + "\n" +
            "Seguidores : " + usuario.getSeguidores() +
            "\tSeguindo : " + usuario.getSeguindo().size()  
        );
    }
    public void mostraPostagens(Iterator<String> mensagens){
        System.out.println("Postagens: ");

        while(mensagens.hasNext()){
            System.out.println("\t\"" + mensagens.next() + "\"");
        }
    }
    public void mostraTodosPerfis(List<Usuario> usuarios){
        for(int i = 0; i < usuarios.size(); i++){
            String seguidorString = (usuarios.get(i).getSeguidores() == 1) ? "Seguidor" : "Seguidores";

            System.out.println(
                "Usuario: " + usuarios.get(i).getLogin() +
                " | " + usuarios.get(i).getSeguidores() + " " + seguidorString + " | " +
                "Postagens: " + usuarios.get(i).getMensagens().size()
            );
        }
    }
    public void mostraTimeline(String[][] mensagens){
        System.out.println("\t-- TIMELINE --");
        
        if(mensagens[0][1] != null){
            for(int i = 0; i < mensagens.length; i++){
                for(int j = 1; j < mensagens[i].length; j++){
                    if(mensagens[i][j] != null && mensagens[i][1] != null){
                        System.out.println("@" + mensagens[i][0] + " diz");
                        System.out.println("\t\"" + mensagens[i][j] + "\"");
                    }
                }
            }

        } else {
            System.out.println("Nenhuma postagem");
        }
    }

    public String recebeSubOpcoesSeguir(){
        System.out.println(
            "1 - Seguir usuário\n" +
            "2 - Voltar"
        );
        String escolha = sc.nextLine();
        return escolha;
    }

    public String recebeNomePerfil(){
        System.out.println("Digite o nome do usuario a ser seguido: ");
        String nomeUsuario = sc.nextLine();

        return nomeUsuario;
    }

    public String recebePostagem(){
        String mensagem;

        do{
            System.out.println("Digite sua mensagem, ela não deve conter menos de 250 caracteres : ");
            mensagem = sc.nextLine();

            if(mensagem.length() > 250){
                System.out.println("Erro, mensagem excede o limite de caracteres");
            }
        } while(mensagem.length() > 250);

        System.out.println("Mensagem postada com sucesso!");
        return mensagem;
    }

    //Mensagens
    public void mostraErroLogin(){
        System.out.println("Um usuario com este login já existe, tente novamente");
    }
    public void mostraBemVindo(Usuario usuario){
        System.out.println("Bem-Vindo " + usuario.getLogin());
    }
    public void mostraUsuarioRemovido(){
        System.out.println("Usuário removido com sucesso!");
    }
    public void mostraOpcaoInvalida(){
        System.out.println("Opção inválida");
    }
    public void mostraUsuarioInvalido(){
        System.out.println("Usuário não encontrado");
    }
    public void mostraLoginInvalido(){
        System.out.println("Login ou senha inválidos");
    }
    public void mostraSeguindo(Usuario usuarioSeguido){
        System.out.println("Seguindo " + usuarioSeguido.getLogin() + "!");
    }
    public void mostraTimelineVazia(){
        System.out.println("Nenhuma postagem no momento");
    }
    public void mostraNenhumUsuario(){
        System.out.println("Nenhum usuário cadastrado");
    }
}
