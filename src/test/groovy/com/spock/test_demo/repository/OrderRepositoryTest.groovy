package com.spock.test_demo.repository

import com.spock.test_demo.entity.MemberEntity
import com.spock.test_demo.entity.OrderEntity
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import spock.lang.Specification

import javax.persistence.EntityManager
import java.time.LocalDateTime

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class OrderRepositoryTest extends Specification {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    EntityManager em;


    def "FindByMemberEntity_Email - 외래키의 email 을 기준으로 orders 가져오기"() {
        given: "member 데이터 저장"
        def member = MemberEntity.builder()
                .email(email)
                .phoneNumber(phone)
                .password(password)
                .name(name).build()
        memberRepository.save(member)

        and: "order 데이터 저장"
        def orders = OrderEntity.builder()
                .memberEntity(member)
                .startAt(LocalDateTime.now()).build()
        orderRepository.save(orders)

        expect:
        def orderEntity = orderRepository.findByMemberEntity_Email(email)
        orderEntity.getMemberEntity().getEmail() == email

        where:
        email             | phone            | password | name
        "sh123@naver.com" | "010-1234-4567"  | "1234"   | "홍길동"
        "aa@naver.com"    | "010-1212-34566" | "1234"   | "abc"

    }
}
