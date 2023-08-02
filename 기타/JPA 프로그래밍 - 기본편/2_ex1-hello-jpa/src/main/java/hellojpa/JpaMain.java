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

            System.out.println("1");
            Member findMember = em.find(Member.class, 1L);
            // System.out.println(findMember.getName());
            // System.out.println(findMember.getId());
            findMember.setName("HelloJPA");
            List<Member> result = em.createQuery("select m from Member m where m.name = :name and m.id = :id", Member.class)
                            .setParameter("name","HelloJPA").setParameter("id", 1L).getResultList();

            System.out.println("2");

            for(Member m : result) {
                System.out.println(m.getName());
            }

            tx.commit();
        }catch (Exception e) {
            System.out.println("3");
            tx.rollback();
        } finally {
            em.close();
            emf.close();
        }

    }
}
