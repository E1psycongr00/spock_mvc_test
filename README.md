# spock_mvc_test
spock를 활용해 Spring Application의 Controller, Service, Repository를 테스트하는 예제<p></p>
회원가입, 회원 조회 API에 대해서 간단한 테스트 예제를 작성 <p></p>
입력에 대해 몇가지 유효성이 적용되어 있고 간단한 예외 핸들링이 구현되어 있음

# Entity 구조

```java
@Entity
@Table(name = "member")
public class MemberEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "email")
    private String email;

    @Column(name = "name", length = 10)
    private String name;

    @Column(name = "password", length = 20)
    private String password;

    @Column(name = "phone_number")
    private String phoneNumber;

}
```

```java
@Entity
@Table(name = "orders")
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false, columnDefinition = "BINARY(16)")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private MemberEntity memberEntity;

    @Column(name = "start_at")
    private LocalDateTime startAt;

}
```


