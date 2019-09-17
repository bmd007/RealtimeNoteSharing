package ir.tiroon.notes.repository

import ir.tiroon.notes.model.Role
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface RoleRepository extends JpaRepository<Role, Long> {
    Role findRoleByRoleName(String roleName)
}
