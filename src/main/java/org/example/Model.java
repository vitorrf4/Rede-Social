package org.example;

import org.apache.commons.collections.MultiMap;
import org.apache.commons.collections.map.MultiValueMap;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.util.Iterator;
import java.util.List;

public class Model {
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("redesocial");
    EntityManager em = emf.createEntityManager();

    public boolean persistirUsuario(Usuario usuario) {
        if (buscarUsuario(usuario.getLogin()) != null) {
            return false;
        }

        em.getTransaction().begin();
        em.persist(usuario);
        em.getTransaction().commit();

        return true;
    }

    public Usuario buscarUsuario(Long idRecebido) {
        try {
            Query queryUsuario = em.createQuery("select id from Usuario where id = :id and ativo = 1");
            queryUsuario.setParameter("id", idRecebido);

            Long idUsuario = (Long)queryUsuario.getSingleResult();

            return em.find(Usuario.class, idUsuario);
        } catch (NoResultException e) {
            return null;
        }
    }

    public Usuario buscarUsuario(String nomeUsuario) {
        try {
            Query queryUsuario = em.createQuery("select id from Usuario where login = :login and ativo = 1");
            queryUsuario.setParameter("login", nomeUsuario);

            Long id = (Long)queryUsuario.getSingleResult();

            return em.find(Usuario.class, id);
        } catch (Exception e) {
            return null;
        }

    }

    public List<Usuario> buscarTodosUsuariosAtivos() {
        Query queryUsuarios = em.createQuery(
            "from Usuario where ativo = 1"
        );

        return (List<Usuario>) queryUsuarios.getResultList();
    }

    public Usuario compararLoginSenha(Usuario usuarioEntrado) {
        try {
            Query query = em.createQuery(
                "select id from Usuario where login = :login and senha = :senha and ativo = 1"
            );
            query.setParameter("login", usuarioEntrado.getLogin());
            query.setParameter("senha" , usuarioEntrado.getSenha());

            Long id = (Long)query.getSingleResult();

            return buscarUsuario(id);
        } catch (Exception e) {
            return null;
        }
    }

    public Iterator<String> buscarPostagens(Usuario usuario) {
        return usuario.getMensagens().iterator();
    }

    public MultiMap buscarTimeline(Usuario usuarioLogado) {
        int qntdSeguida = usuarioLogado.getSeguindo().size();

        if (qntdSeguida == 0)
            return null;

        MultiMap timeline = new MultiValueMap();

        for (Long idSeguindo : usuarioLogado.getSeguindo()) {
            Usuario seguindo = buscarUsuario(idSeguindo);

            if (!seguindo.getMensagens().isEmpty()) {
                for (String mensagem : seguindo.getMensagens()) {
                    timeline.put(seguindo, mensagem);
                }
            }

        }

        return timeline;
    }

    public boolean seguirUsuario(Usuario usuarioSeguindo, Usuario usuarioASeguir) {
        if (usuarioSeguindo.getId().equals(usuarioASeguir.getId()) ||
                usuarioSeguindo.getSeguindo().contains(usuarioASeguir.getId())) {
            return false;
        }

        try {
            em.getTransaction().begin();  
            usuarioSeguindo.getSeguindo().add(usuarioASeguir.getId());
            usuarioASeguir.setSeguidores(usuarioASeguir.getSeguidores() + 1);
            em.getTransaction().commit();
            return true;
        } catch (Exception e) {
            em.getTransaction().rollback();
            em.getTransaction().commit();
            return false;
        }
    }
    
    public void desativarUsuario(Usuario usuarioDesativado) {
        try {
            em.getTransaction().begin();
            usuarioDesativado.setAtivo(false);
            diminuirNumSeguindo(usuarioDesativado);
            diminuirNumSeguidores(usuarioDesativado);
        } catch (Exception e) {
            em.getTransaction().rollback();
        } finally {
            em.getTransaction().commit();
        }
    }

    public void diminuirNumSeguindo(Usuario usuarioDesativado) {
        for (Long id : usuarioDesativado.getSeguindo()) {
            Usuario seguindo = buscarUsuario(Long.toString(id));
            seguindo.setSeguidores(seguindo.getSeguidores() - 1);
        }
    }

    public void diminuirNumSeguidores(Usuario usuarioDesativado){
        List<Usuario> todosUsuarios = buscarTodosUsuariosAtivos();

        for (Usuario seguindo : todosUsuarios) {
            if (seguindo.getSeguindo().contains(usuarioDesativado.getId())) {
                seguindo.getSeguindo().remove(usuarioDesativado.getId());
            }
        }
    }

    public void salvarMensagem(String mensagem, Usuario usuario){
        em.getTransaction().begin();
        usuario.getMensagens().add(mensagem);
        em.getTransaction().commit();
    }
}
