package jpabook.jpashop;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class JpaMain {

    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try{

            /*Member member = new Member();
            member.setName("memberA");
            em.persist(member);*/


            tx.commit();
        } catch(Exception e) {
            System.out.println("오류발생");
            e.printStackTrace();
            tx.rollback();
        } finally {
            em.close();
            emf.close();
        }



    }


}
