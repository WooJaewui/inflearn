package jpabook.jpashop.service;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.exception.NotEnoughStockException;
import jpabook.jpashop.repository.OrderRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class OrderServiceTest {

    @Autowired
    EntityManager em;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;


    @Rollback(false)
    @Test
    void 주문() {
        //given
        Member member = createMember();

        Book book = createBook("시골 JPA", 10000, 10);

        int orderCount = 2;

        //when
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);


        //then
        Order findOrder = orderRepository.findOne(orderId);
        org.junit.jupiter.api.Assertions.assertEquals(OrderStatus.ORDER, findOrder.getStatus());
        org.junit.jupiter.api.Assertions.assertEquals(1, findOrder.getOrderItems().size());
        org.junit.jupiter.api.Assertions.assertEquals(10000 * orderCount, findOrder.getTotalPrice());
        org.junit.jupiter.api.Assertions.assertEquals(8, book.getStockQuantity());
    }

    private Book createBook(String name, int price, int stockQuantity) {
        Book book = new Book();
        book.setName(name);
        book.setPrice(price);
        book.setStockQuantity(stockQuantity);
        em.persist(book);
        return book;
    }

    private Member createMember() {
        Member member = new Member();
        member.setName("memberA");
        member.setAddress(new Address("서울", "한강", "123-123"));
        em.persist(member);
        return member;
    }


    @Test
    void 상품주문_재고수량초과() {
        //given
        Member member = createMember();
        Book book = createBook("시골 JPA", 10000, 10);
        int orderCount = 11;

        //when

        //then
        assertThatThrownBy(() -> {
            orderService.order(member.getId(), book.getId(), orderCount);
        }).isInstanceOf(NotEnoughStockException.class);
    }


    @Rollback(false)
    @Test
    void 주문_취소() {
        //given
        Member member = createMember();
        Book book = createBook("시골 JPA", 10000, 10);
        int orderCount = 2;

        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);
        //when
        orderService.cancelOrder(orderId);

        //then
        Order findOrder = orderRepository.findOne(orderId);

        Assertions.assertEquals(OrderStatus.CANCEL, findOrder.getStatus());
        Assertions.assertEquals(10, book.getStockQuantity());
    }


    @Test
    void 조회() {
        //given

        //when

        //then
    }

}