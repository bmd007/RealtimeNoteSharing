package ir.tiroon.notes.model.event

import ir.tiroon.notes.model.User

class NoteRemovedEvent implements Serializable {
    Long removedNoteId
    Set<User> removedNoteCollaborators = new HashSet<>()

    NoteRemovedEvent(Long removedNoteId, Set<User> removedNoteCollaborators) {
        this.removedNoteId = removedNoteId
        this.removedNoteCollaborators.addAll(removedNoteCollaborators)
    }
}
