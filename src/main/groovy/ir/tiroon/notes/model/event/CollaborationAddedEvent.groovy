package ir.tiroon.notes.model.event

import ir.tiroon.fanavard.q2.monolith.model.Note
import ir.tiroon.fanavard.q2.monolith.model.User

class CollaborationAddedEvent implements Serializable{
    Note note
    User addedUser

    CollaborationAddedEvent(Note note, User addedUser) {
        this.note = note
        this.addedUser = addedUser
    }

    Note getNote() {
        return note
    }

    void setNote(Note note) {
        this.note = note
    }

    User getAddedUser() {
        return addedUser
    }

    void setAddedUser(User addedUser) {
        this.addedUser = addedUser
    }
}
