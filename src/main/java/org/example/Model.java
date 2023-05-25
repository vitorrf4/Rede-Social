package org.example;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.util.Iterator;
import java.util.List;

public class Model {
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("mydb");
    EntityManager em = emf.createEntityManager();

    public boolean persisteUsuario(Usuario usuario){
        // verifica se ja existe um usuario ja registrado com esse login
        if (buscaUsuarioPorLogin(usuario.getLogin()) != null){
            return false;
        } 
        em.getTransaction().begin();
        em.persist(usuario);
        em.getTransaction().commit();

        return true;
    }

    public Usuario buscaUsuarioPorId(String idRecebido){
        try {
            Query queryUsuario = em.createQuery("select id from Usuario where id = :id and ativo = 1");
            queryUsuario.setParameter("id", Long.parseLong(idRecebido));

            Long idUsuario = (Long)queryUsuario.getSingleResult();
            Usuario usuarioBuscado = em.find(Usuario.class, idUsuario);

            return usuarioBuscado;
        } catch (NumberFormatException | NoResultException e) {
            return null;
        }
    }
    public Usuario buscaUsuarioPorLogin(String nomeUsuario){
        try {
            Query queryUsuario = em.createQuery("select id from Usuario where login = :login and ativo = 1");
            queryUsuario.setParameter("login", nomeUsuario);

            Long id = (Long)queryUsuario.getSingleResult();

            Usuario usuarioBuscado = em.find(Usuario.class, id);

            return usuarioBuscado;
        } catch (Exception e) {
            return null;
        }

    }

    public List<Usuario> buscaTodosUsuariosAtivos(){
        Query queryUsuarios = em.createQuery(
            "from Usuario where ativo = 1"
        );
        List<Usuario> usuariosAtivos = queryUsuarios.getResultList();

        return usuariosAtivos;
    }

    public Usuario comparaLoginSenha(Usuario usuarioEntrado){
        try {
            Query query = em.createQuery(
                "select id from Usuario where login = :login and senha = :senha and ativo = 1"
            );
            query.setParameter("login", usuarioEntrado.getLogin());
            query.setParameter("senha" , usuarioEntrado.getSenha());

            Long idLong = (Long)query.getSingleResult();
            String id = Long.toString(idLong);
            Usuario usuario = buscaUsuarioPorId(id);

            return usuario;
        } catch (Exception e) {
            return null;
        }
    }

    public Iterator<String> buscaPostagens(Usuario usuario){
        Iterator<String> itMensagens = usuario.getMensagens().iterator();
        return itMensagens;
    }

    public String[][] buscaTimeline(Usuario usuarioLogado){
        int qntdSendoSeguida = usuarioLogado.getSeguindo().size();
        String[][] mensagens = null;
        int lin = 0;
        
        if(qntdSendoSeguida != 0){
            try {
                mensagens = new String[qntdSendoSeguida][50];

                for(Long idUsuarioSeguindo : usuarioLogado.getSeguindo()){
                    Usuario usuarioSeguindo = buscaUsuarioPorId(Long.toString(idUsuarioSeguindo));
                    mensagens[lin][0] = usuarioSeguindo.getLogin(); // primeira campo de cada linha é o nome do usuario que realizou as postagens
        
                    for(int col = 1; col <= usuarioSeguindo.getMensagens().size(); col++){
                        String mensagemCol = usuarioSeguindo.getMensagens().get(col-1);
                        
                        mensagens[lin][col] = mensagemCol;
                    }
                    lin++;
                }
                
            } catch (Exception e) {
                return null;
            }
        }

        return mensagens;
    }
    public boolean seguirUsuario(Usuario usuarioSeguindo, Usuario usuarioASeguir){
        // verifica se o usuario esta tentando seguir a si mesmo, ou um usuario que ele já segue
        if(usuarioSeguindo.getId() == usuarioASeguir.getId() || usuarioSeguindo.getSeguindo().contains(usuarioASeguir.getId())){ 
            return false;
        } 
        try {
            em.getTransaction().begin();  
            usuarioSeguindo.getSeguindo().add(usuarioASeguir.getId());
            usuarioASeguir.setSeguidores(usuarioASeguir.getSeguidores() + 1);
            em.getTransaction().commit();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    public void desativaUsuario(Usuario usuarioDesativado){
        em.getTransaction().begin();
        usuarioDesativado.setAtivo(false);
        diminuiNumSeguindo(usuarioDesativado);
        diminuiNumSeguidores(usuarioDesativado);
        em.getTransaction().commit();
    }
    public void diminuiNumSeguindo(Usuario usuarioDesativado){
        for(Long id : usuarioDesativado.getSeguindo()){
            Usuario seguindo = buscaUsuarioPorId(Long.toString(id));
            seguindo.setSeguidores(seguindo.getSeguidores() - 1);
        }
    }
    public void diminuiNumSeguidores(Usuario usuarioDesativado){
        List<Usuario> todosUsarios = buscaTodosUsuariosAtivos();

        for(Usuario seguindo : todosUsarios){
            if(seguindo.getSeguindo().contains(usuarioDesativado.getId())){
                seguindo.getSeguindo().remove(usuarioDesativado.getId());
            }
        }
    }
    public void salvaMensagem(String mensagem, Usuario usuario){
        em.getTransaction().begin();
        usuario.getMensagens().add(mensagem);
        em.getTransaction().commit();
    }
}
