package ir.tiroon.notes.model

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import ir.tiroon.fanavard.q2.monolith.model.event.NoteChangedEvent
import org.hibernate.annotations.Proxy

import javax.persistence.*

@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Table(name = "User")
@Proxy(lazy = false)
class User implements Serializable {

    @Id
    @Column(nullable = false, unique = true)
    String phoneNumber

    @Column(nullable = false)
    String name

    @JsonIgnore
    @Column(nullable = false)
    String password

    @Column(unique = true, nullable = false)
    String email

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "authorities",
            joinColumns = @JoinColumn(name = "phoneNumber"),
            inverseJoinColumns = @JoinColumn(name = "roleId"))
    Set<Role> roles = new HashSet<>()

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    State state = State.Active

    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "collaborators", cascade = CascadeType.REMOVE)
    Set<Note> collaborations = new HashSet<>()

    @JsonIgnore
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "owner", cascade = CascadeType.REMOVE)
    Set<Note> notes = new HashSet<>()

    User() {
    }

    @JsonCreator
    User(@JsonProperty("name") String name, @JsonProperty("password") String password,
         @JsonProperty("email") String email, @JsonProperty("phoneNumber") String phoneNumber) {
        this.name = name
        this.password = password
        this.email = email
        this.phoneNumber = phoneNumber
    }

    User(String phoneNumber, String name, String password, String email, Set<Role> roles, State state, Set<Note> collaborations, Set<Note> notes, Set<NoteChangedEvent> changes) {
        this.phoneNumber = phoneNumber
        this.name = name
        this.password = password
        this.email = email
        this.roles = roles
        this.state = state
        this.collaborations = collaborations
        this.notes = notes
    }

    Set<Note> getNotes() {
        notes
    }

    void setNotes(Set<Note> notes) {
        this.notes = notes
    }

    void collaborateIn(Note note) {
        collaborations.add(note)
    }

    Set<Note> getCollaborations() {
        collaborations
    }

    void setCollaborations(Set<Note> collaborations) {
        this.collaborations = collaborations
    }

    String getPhoneNumber() {
        phoneNumber
    }

    void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber
    }

    String getName() {
        name
    }

    void setName(String name) {
        this.name = name
    }

    String getPassword() {
        password
    }

    void setPassword(String password) {
        this.password = password
    }

    String getEmail() {
        email
    }

    void setEmail(String email) {
        this.email = email
    }

    Set<Role> getRoles() {
        roles
    }

    void setRoles(Set<Role> roles) {
        this.roles = roles
    }

    State getState() {
        state
    }

    void setState(State state) {
        this.state = state
    }
}
