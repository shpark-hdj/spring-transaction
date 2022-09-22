package com.example

import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import javax.annotation.Resource

@Service
class UserService(
    private val userRepository: UserRepository
) {

    @Lazy
    @Resource(name = "userService")
    private lateinit var selfUserService: UserService

    @Transactional
    fun external() {
        println("call external")
        Thread.sleep(2000)
        userRepository.save(User("test-user1"))
        selfUserService.internal()
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    fun internal() {
        println("call internal")
        Thread.sleep(2000)
        userRepository.save(User("test-user2"))
        throw RuntimeException("xxxxxxx")
    }

    @Transactional
    fun external2() {
        println("call external")
        Thread.sleep(1000)
        userRepository.save(User("main-user1"))
        val executorService = Executors.newFixedThreadPool(5)
        val latch = CountDownLatch(5)

        for (i in 1..5) {
            executorService.submit {
                try {
                    selfUserService.internal2("sub-user-${i}")
                } finally {
                    latch.countDown()
                }
            }
        }

        latch.await()
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    fun internal2(name: String) {
        println("call internal")
        Thread.sleep(1000)
        userRepository.save(User(name))
        if (name.contains("3")) {
            throw RuntimeException("xxxxxxx")
        }
    }
}