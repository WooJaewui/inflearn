package hellojpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class JpaMain {

    public static void main(String[] args) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("hello");
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        // 저장
        /*try {
            Member member = new Member();
            member.setId(2L);
            member.setName("HelloB");
            entityManager.persist(member);

            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
        } finally {
            entityManager.close();
            entityManagerFactory.close();
        }*/

        // 수정
        /*try {
            //Member findMember = entityManager.find(Member.class, 1L);
            List<Member> resultList = entityManager.createQuery("select m from Member as m", Member.class)
                    .setFirstResult(0)
                    .setMaxResults(10)
                    .getResultList();

            for (Member member : resultList) {
                System.out.println("member.name = " + member.getName());
            }


            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
        } finally {
            entityManager.close();
            entityManagerFactory.close();
        }*/


        // 영속성 컨텍스트 1
        /*try {
            Member member = new Member();
            member.setId(100L);
            member.setName("HelloJPA");

            // 영속
            entityManager.persist(member);

            transaction.commit();   // 쿼리가 실행됨
        } catch (Exception e) {
            transaction.rollback();
        } finally {
            entityManager.close();
            entityManagerFactory.close();
        }*/

        // 캐싱, 영속 엔티티의 동일성 보장
        /*try {
            Member findMember1 = entityManager.find(Member.class, 1L);
            Member findMember2 = entityManager.find(Member.class, 1L);

            System.out.println(findMember1.getName());
            System.out.println(findMember1 == findMember2);

            transaction.commit();   // 쿼리가 실행됨
        } catch (Exception e) {
            transaction.rollback();
        } finally {
            entityManager.close();
            entityManagerFactory.close();
        }*/


        // 쓰기 지연
        /*try {
            Member member = new Member();
            member.setId(101L);
            member.setName("Jaewui");

            entityManager.persist(member);
            entityManager.persist(member);

            transaction.commit();   // 쿼리가 실행됨
        } catch (Exception e) {
            transaction.rollback();
        } finally {
            entityManager.close();
            entityManagerFactory.close();
        }*/


        // 쓰기 지연
        /*try {
            Member findMember = entityManager.find(Member.class, 101L);
            findMember.setName("zzzz");

            System.out.println("===========================");
            transaction.commit();   // 쿼리가 실행됨
        } catch (Exception e) {
            transaction.rollback();
        } finally {
            entityManager.close();
            entityManagerFactory.close();
        }*/

        // flush
        /*try {
            Member member = new Member(200L, "member200");
            entityManager.persist(member);
            entityManager.flush();

            System.out.println("===========================");
            transaction.commit();   // 쿼리가 실행됨
        } catch (Exception e) {
            transaction.rollback();
        } finally {
            entityManager.close();
            entityManagerFactory.close();
        }*/


        // 준영속
        /*try {
            Member findMember = entityManager.find(Member.class, 100L);
            entityManager.clear();
            Member findMember2 = entityManager.find(Member.class, 100L);
            //entityManager.detach(findMember);

            System.out.println("===========================");
            transaction.commit();   // 쿼리가 실행됨
        } catch (Exception e) {
            transaction.rollback();
        } finally {
            entityManager.close();
            entityManagerFactory.close();
        }*/


        // 필드와 컬럼 매핑
        /*try {
            Member member = new Member();
            member.setUsername("C");

            entityManager.persist(member);

            System.out.println(member.getId());

            System.out.println("===========================");
            transaction.commit();   // 쿼리가 실행됨
        } catch (Exception e) {
            transaction.rollback();
        } finally {
            entityManager.close();
            entityManagerFactory.close();
        }*/


        // allocationSize
        try {
            Member member = new Member();
            member.setUsername("A");

            Member member2 = new Member();
            member2.setUsername("B");

            Member member3 = new Member();
            member3.setUsername("C");

            entityManager.persist(member);
            entityManager.persist(member2);
            entityManager.persist(member3);

            System.out.println(member.getId());
            System.out.println(member2.getId());
            System.out.println(member3.getId());

            System.out.println("===========================");
            transaction.commit();   // 쿼리가 실행됨
        } catch (Exception e) {
            transaction.rollback();
        } finally {
            entityManager.close();
            entityManagerFactory.close();
        }


    }

}
