package com.example

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(rollbackFor = [Exception::class])
class UserService(
    private val userRepository: UserRepository
) {

    fun external() {
        for (i in 1..10) {

        }
    }

    fun internal() {

    }
}