package com.example

import java.time.LocalDateTime
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "users")
class User(
    val name: String
) {

    @Id
    @GeneratedValue
    private val id: Long? = null

    private val createdDate = LocalDateTime.now()
}
