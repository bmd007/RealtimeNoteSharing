package ir.tiroon.notes.model

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import org.hibernate.annotations.Proxy

import javax.persistence.*

@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Table(name = "Note")
@Proxy(lazy = false)
class Note implements Serializable {

    @Id
    @Column(nullable = false, unique = true)
    @GeneratedValue
    Long noteId

    @Column()
    String label

    @Column(length = 1000000)
    String text

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "phoneNumber", nullable = false)
    User owner

    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "collaborations",
            joinColumns = @JoinColumn(name = "noteId"),
            inverseJoinColumns = @JoinColumn(name = "phoneNumber")
    )
    Set<User> collaborators = new HashSet<>()


    Note() {
    }

    Note(String label, String text, User owner, Set<User> collaborators) {
        this.label = label
        this.text = text
        this.owner = owner
        this.collaborators = collaborators
    }

    Note(String label, String text, User owner) {
        this.label = label
        this.text = text
        this.owner = owner
    }

    @JsonCreator
    Note(@JsonProperty("label") String label, @JsonProperty("text") String text) {
        this.label = label
        this.text = text
    }


    void addCollaborator(User user) {
        collaborators.add(user)
    }

    void removeCollaborator(User user) {
        collaborators.remove(user)
    }

    Long getNoteId() {
        return noteId
    }

    void setNoteId(Long id) {
        this.noteId = id
    }

    String getLabel() {
        return label
    }

    void setLabel(String label) {
        this.label = label
    }

    String getText() {
        return text
    }

    void setText(String text) {
        this.text = text
    }

    User getOwner() {
        return owner
    }

    Note setOwner(User owner) {
        this.owner = owner
        this
    }

    Set<User> getCollaborators() {
        return collaborators
    }

    void setCollaborators(Set<User> collaborators) {
        this.collaborators = collaborators
    }
}
