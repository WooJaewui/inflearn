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


            Team team = new Team();
            team.setName("TeamA");
            em.persist(team);

            Member member = new Member();
            member.changeTeam(team);
            member.setUsername("member1");
            em.persist(member);

            em.flush();
            em.clear();

            Member findMember = em.find(Member.class, member.getId());
            List<Member> members = findMember.getTeam().getMembers();

            for(Member m : members) {
                System.out.println(m.getUsername());
            }


            System.out.println(findMember.getId());


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
