package com.example

import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
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
//        throw RuntimeException("xxxxxxx")
    }
}