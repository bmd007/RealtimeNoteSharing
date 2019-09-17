package ir.tiroon.notes.model.event

import ir.tiroon.notes.model.Note
import ir.tiroon.notes.model.User

class CollaborationAddedEvent implements Serializable {
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
