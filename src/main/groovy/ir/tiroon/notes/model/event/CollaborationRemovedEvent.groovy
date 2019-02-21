package ir.tiroon.notes.model.event

import ir.tiroon.fanavard.q2.monolith.model.User

class CollaborationRemovedEvent implements Serializable{
    Long noteId
    User removedUser

    CollaborationRemovedEvent(Long noteId, User removedUser) {
        this.noteId = noteId
        this.removedUser = removedUser
    }
}
