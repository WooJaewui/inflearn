package jpabook.jpashop;

import jpabook.jpashop.domain.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class JpaMain {

    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        EntityTransaction transaction = em.getTransaction();
        transaction.begin();

        try {

            Book book = new Book();
            book.setName("오브젝트");
            book.setAuthor("조용호");

            em.persist(book);


            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
        } finally {
            em.close();
            emf.close();
        }

    }

}
