package ir.tiroon.notes.model.event

import ir.tiroon.notes.model.User

class CollaborationRemovedEvent implements Serializable {
    Long noteId
    User removedUser

    CollaborationRemovedEvent(Long noteId, User removedUser) {
        this.noteId = noteId
        this.removedUser = removedUser
    }
}
