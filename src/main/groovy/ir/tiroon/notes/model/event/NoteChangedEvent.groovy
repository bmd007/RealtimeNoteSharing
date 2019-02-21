package ir.tiroon.notes.model.event

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import ir.tiroon.fanavard.q2.monolith.model.Note
import ir.tiroon.fanavard.q2.monolith.model.User
import org.hibernate.annotations.Proxy

import javax.persistence.*
import java.time.LocalDateTime

class NoteChangedEvent implements Serializable {

    User by
    Note note


    NoteChangedEvent(User by, Note note) {
        this.by = by
        this.note = note
    }
}
