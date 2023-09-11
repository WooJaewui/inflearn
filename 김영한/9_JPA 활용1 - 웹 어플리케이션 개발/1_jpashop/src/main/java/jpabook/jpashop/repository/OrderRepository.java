package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderRepository {

    private final EntityManager em;

    public void save(Order order) {
        em.persist(order);
    }

    public Order findOne(Long id) {
        return em.find(Order.class, id);
    }


    /**
     *  String, 자바 코드로 동적 쿼리 짜기.
     */
    public List<Order> findAllByString(OrderSearch orderSearch) {

        String jpql = "select o from Order o join fetch o.member m";
        
        // 복잡한 로직
        // if( ... ) ...

        return em.createQuery(jpql, Order.class)
                /*.setParameter("status", orderSearch.getOrderStatus())
                .setParameter("name", orderSearch.getMemberName())*/
                .setMaxResults(1000)
                .getResultList();

    }

    /**
     *  JPA Criteria
     */
    public List<Order> findAllByCriteria(OrderSearch orderSearch) {

        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Order> query = criteriaBuilder.createQuery(Order.class);
        Root<Order> o = query.from(Order.class);
        Join<Object, Object> m = o.join("member", JoinType.INNER);

        List<Predicate> criteria = new ArrayList<>();

        // 주문 상태 검색
        if (orderSearch.getOrderStatus() != null) {
            Predicate status = criteriaBuilder.equal(o.get("status"), orderSearch.getOrderStatus());
            criteria.add(status);
        }

        // 회원 이름 검색
        if (StringUtils.hasText(orderSearch.getMemberName())) {
            Predicate name = criteriaBuilder.like(m.get("name"), "%" + orderSearch.getMemberName() + "%");
            criteria.add(name);
        }

        query.where(criteria.toArray(new Predicate[criteria.size()]));
        return em.createQuery(query).setMaxResults(1000).getResultList();

    }

    /**
     *  QueryDSL
     */
    //public List<Order> findAll(OrderSearch orderSearch) { }

}
