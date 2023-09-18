package study.querydsl.repository;

import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import study.querydsl.dto.MemberSearchCondition;
import study.querydsl.entity.Member;
import study.querydsl.entity.QMember;
import study.querydsl.repository.support.Querydsl4RepositorySupport;

import java.util.List;

import static study.querydsl.entity.QMember.*;

@Repository
public class MemberTestRepository extends Querydsl4RepositorySupport {


    public MemberTestRepository(Class<?> domainClass) {
        super(domainClass);
    }

    public List<Member> basicSelect() {
        return select(member)
                .from(member)
                .fetch();
    }

    public List<Member> basicSelectFrom() {
        return selectFrom(member)
                .fetch();
    }

    public Page<Member> searchpageByAppliyPage(MemberSearchCondition condition, Pageable pageable) {
        JPAQuery<Member> query = selectFrom(member)
                .where();

        return null;
    }

    public Page<Member> applyPagination(MemberSearchCondition condition, Pageable pageable) {
        applyPagination(pageable, queryFactory -> {
            queryFactory.selectFrom(member)
                    .fetch();
            return null;
        });

        return null;
    }



}
