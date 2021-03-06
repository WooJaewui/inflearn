package hellojpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class JpaMain {
    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        tx.begin();

        try {

            // 비영속
            Member member1 = new Member(150L,"A");
            Member member2 = new Member(160L,"B");

            // 영속.
            Member findMember = em.find(Member.class, 150L);
            findMember.setName("AAAAAA");

            em.clear();

            tx.commit();
        }catch (Exception e) {
            System.out.println("오류발생");
            tx.rollback();
        } finally {
            em.close();
            emf.close();
        }

    }
}
