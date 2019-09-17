package ir.tiroon.notes.repository

import ir.tiroon.notes.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository extends JpaRepository<User, String> {
    User findUserByEmail(String email)
    User findUserByPhoneNumber(String phoneNumber)
}
