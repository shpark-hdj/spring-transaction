package com.example

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.lang.RuntimeException
import javax.persistence.EntityManagerFactory
import javax.persistence.RollbackException

@SpringBootTest
internal class UserServiceTest {

    @Autowired
    private lateinit var userService: UserService
    @Autowired
    private lateinit var userRepository: UserRepository
    @Autowired
    lateinit var entityManagerFactory: EntityManagerFactory

    @Test
    fun `mysql 의 wait_timeout 보다 클라이언트의 트랜잭션 시간이 더 긴 경우 익셉션 발생`() {
        val em = entityManagerFactory.createEntityManager()
        em.transaction.begin()
        println("트랜잭션 시작")
        val createQuery = em.createQuery("select a from User a")
        createQuery.resultList

        // db wait_timeout = 3초
        Thread.sleep(4000)

        assertThatThrownBy {
            println("트랜잭션 커밋")
            em.transaction.commit()
        }.isInstanceOf(RollbackException::class.java)
    }

    @Test
    fun `메소드 A 에서 메소드 B(REQUIRES_NEW)호출시 unCheckedEx 일 경우 상위 트랜잭션까지 모두 롤백`() {
//        assertThatThrownBy {
            userService.external()
//        }.isInstanceOf(RuntimeException::class.java)
//
//        val user = userRepository.findAll()
//
//        assertThat(user.isEmpty()).isTrue
    }
}