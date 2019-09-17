package ir.tiroon.notes.service

import ir.tiroon.notes.model.State
import ir.tiroon.notes.model.User
import ir.tiroon.notes.repository.NoteRepository
import ir.tiroon.notes.repository.RoleRepository
import ir.tiroon.notes.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import java.util.stream.Collectors

@Transactional
@Service("userService")
class UserServices {

    @Autowired
    UserRepository userRepository

    @Autowired
    RoleRepository roleRepository

    @Autowired
    NoteRepository noteRepository

    @Autowired
    PasswordEncoder passwordEncoder

    User saveWithPassEncoding(User user) {
        user.state = State.Active

        def userRole = roleRepository.findRoleByRoleName("USER")
        user.getRoles().add(userRole)
        user.setPassword(passwordEncoder.encode(user.getPassword()))
        userRepository.save(user)
        user
    }

    User saveWithParameter(String email, String name, String password, String phoneNumber) {
        saveWithPassEncoding(new User(name, password, email, phoneNumber))
    }

    User get(String phn) {
        userRepository.getOne(phn)
    }

    User getAndNullIfNotExists(String phn) {
        userRepository.findUserByPhoneNumber(phn)
    }

    User getByEmail(String email) {
        userRepository.findUserByEmail(email)
    }

    ArrayList<User> list() {
        (ArrayList<User>) userRepository.findAll()
    }

    boolean delete(String phn, String requesterPhoneNumber) {
        def user = userRepository.getOne(phn)
        if (user.phoneNumber == requesterPhoneNumber) {
            userRepository.delete()
            userRepository.flush()
            true
        } else false
    }

    Set<User> whoIsNotCollaborating(Long noteId, String requesterPhoneNumber) {

        def note = noteRepository.getOne(noteId)

        def collaborators = note.collaborators

        if (note.owner.phoneNumber == requesterPhoneNumber) {
            def result = userRepository.findAll()
                    .stream()
                    .filter { !collaborators.contains(it) }
                    .collect(Collectors.toSet())
            result.remove(note.owner)
            result
        } else null
    }

}