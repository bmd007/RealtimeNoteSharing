package ir.tiroon.notes.service

import ir.tiroon.fanavard.q2.monolith.model.*
import ir.tiroon.fanavard.q2.monolith.model.event.CollaborationAddedEvent
import ir.tiroon.fanavard.q2.monolith.model.event.CollaborationRemovedEvent
import ir.tiroon.fanavard.q2.monolith.model.event.NoteChangedEvent
import ir.tiroon.fanavard.q2.monolith.model.event.NoteRemovedEvent
import ir.tiroon.fanavard.q2.monolith.repository.NoteRepository
import ir.tiroon.fanavard.q2.monolith.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service

import java.util.stream.Collectors

//@Transactional
@Service("noteService")
class NoteServices {

    @Autowired
    NoteRepository noteRepository

    @Autowired
    UserRepository userRepository


    @Autowired
    ApplicationEventPublisher eventPublisher


    NoteChangedEvent noteChangedEvent(Note note, String requesterPhoneNumber) {
        if (
        note.collaborators.phoneNumber.contains(requesterPhoneNumber)
                ||
                note.owner.phoneNumber == requesterPhoneNumber
        ) {

            def noteChanger = userRepository.getOne(requesterPhoneNumber)
            def event = new NoteChangedEvent(noteChanger, note)
            eventPublisher.publishEvent(event)
            event

        }else null

    }



    User addCollaboration(String newCollaboratorPhoneNumber, Long noteId, String requesterPhoneNumber) {
        def note = noteRepository.getOne(noteId)

        if (newCollaboratorPhoneNumber != requesterPhoneNumber
                && note.owner.phoneNumber == requesterPhoneNumber) {

            def newCollaborator = userRepository.getOne(newCollaboratorPhoneNumber)
            note.addCollaborator(newCollaborator)
            noteRepository.save(note)
            eventPublisher.publishEvent(new CollaborationAddedEvent(note, newCollaborator))
            newCollaborator

        } else null
    }


    boolean removeCollaboration(String removingCollaboratorPhN, Long noteId, String requesterPhoneNumber) {
        def note = noteRepository.getOne(noteId)
        def removingCollaborator = userRepository.getOne(removingCollaboratorPhN)
        if (note.owner.phoneNumber == requesterPhoneNumber
                ||
                (
                    note.collaborators.phoneNumber.contains(requesterPhoneNumber)
                    &&
                    removingCollaboratorPhN == requesterPhoneNumber
                )
        ) {
            note.removeCollaborator(removingCollaborator)
            noteRepository.save(note)
            eventPublisher.publishEvent(new CollaborationRemovedEvent(noteId, removingCollaborator))
            true
        } else false
    }

    void save(Note note, String uname) {
        def user = userRepository.getOne(uname)
        note.setOwner(user)
        noteRepository.save(note)
    }


    Note update(Note updatedNote, String requesterPhoneNumber) {
        Note originalNote = noteRepository.getOne(updatedNote.noteId)

        if (
            originalNote.collaborators.phoneNumber.contains(requesterPhoneNumber)
            ||
            originalNote.owner.phoneNumber == requesterPhoneNumber
        ) {
            originalNote.setText(updatedNote.getText())
            originalNote.setLabel(updatedNote.getLabel())
            noteRepository.save(originalNote)
            originalNote
        } else null
    }

//    @PreAuthorize("#username == principal.name")
//    @PostFilter("filterObject.owner.phoneNumber == authentication.principal.username")
    Note get(Long id, String requesterPhoneNumber) {
        def note = noteRepository.getOne(id)
        (
                note.collaborators.phoneNumber.contains(requesterPhoneNumber)
                ||
                note.owner.phoneNumber.equals(requesterPhoneNumber)
        ) ? note : null
    }

//    @PostFilter("filterObject.owner.phoneNumber == authentication.principal.username")
    Set<Note> list(String requesterPhoneNumber) {
        (ArrayList<Note>) noteRepository.findAll().stream()
                .filter { note ->
            (
                note.collaborators.phoneNumber.contains(requesterPhoneNumber)
                ||
                note.owner.phoneNumber.equals(requesterPhoneNumber)
            )
        }
        .collect(Collectors.toSet())
    }


    boolean deleteById(Long id, String requesterPhoneNumber) {
        def note = noteRepository.getOne(id)
        if (note.owner.phoneNumber == requesterPhoneNumber) {
            def noteRemovedEvent = new NoteRemovedEvent(id,note.collaborators)
            noteRepository.deleteById(id)
            noteRepository.flush()
            eventPublisher.publishEvent(noteRemovedEvent)
            true
        } else false
    }


    Set<User> collaborators(long noteId, String requesterPhoneNumber) {
        def note = noteRepository.getOne(noteId)

        if (note.owner.phoneNumber == requesterPhoneNumber) {
            note.collaborators
        } else if (note.collaborators.phoneNumber.contains(requesterPhoneNumber)) {
            def Set<User> noteCollaborators = new HashSet<>()
            noteCollaborators.add(userRepository.getOne(requesterPhoneNumber))
            noteCollaborators
        } else Collections.emptySet()
    }


    Set<User> addCollaborationByList(String[] collaboratorsPhoneNumberArray,
                                     Long noteId, String requesterPhoneNumber) {

        def note = noteRepository.getOne(noteId)
        if (note.owner.phoneNumber == requesterPhoneNumber) {

            Set<User> resultSet = collaboratorsPhoneNumberArray.collect().stream()
                    .map { userRepository.findUserByPhoneNumber(it) }
                    .map {
                        note.addCollaborator(it)
                        it
                    }.collect(Collectors.toSet())

            noteRepository.save(note)

            resultSet.stream().forEach{
               eventPublisher.publishEvent(new CollaborationAddedEvent(note, it)) }

            resultSet

        } else null

    }

}
