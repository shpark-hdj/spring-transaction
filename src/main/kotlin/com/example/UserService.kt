package com.example

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(rollbackFor = [Exception::class], timeout = 60)
class UserService(
    private val userRepository: UserRepository
) {

    fun external() {
        Thread.sleep(55000)
        userRepository.save(User("test-user"))
    }
}