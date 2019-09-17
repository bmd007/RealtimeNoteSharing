package ir.tiroon.notes.model.event


import ir.tiroon.notes.model.Note
import ir.tiroon.notes.model.User

class NoteChangedEvent implements Serializable {

    User by
    Note note


    NoteChangedEvent(User by, Note note) {
        this.by = by
        this.note = note
    }
}
