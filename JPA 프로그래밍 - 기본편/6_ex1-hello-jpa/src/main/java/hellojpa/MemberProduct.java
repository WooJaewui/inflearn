package hellojpa;

import javax.persistence.*;

@Entity
public class MemberProduct {

    @Id
    @GeneratedValue
    @Column(name = "MEMBERPRODUCT_ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "PRODUCT_ID")
    private Product product;



}
