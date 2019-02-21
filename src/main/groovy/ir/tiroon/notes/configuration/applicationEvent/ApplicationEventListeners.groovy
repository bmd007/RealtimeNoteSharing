package ir.tiroon.notes.configuration.applicationEvent

import com.fasterxml.jackson.databind.ObjectMapper
import ir.tiroon.fanavard.q2.monolith.model.event.CollaborationAddedEvent
import ir.tiroon.fanavard.q2.monolith.model.event.CollaborationRemovedEvent
import ir.tiroon.fanavard.q2.monolith.model.event.NoteChangedEvent
import ir.tiroon.fanavard.q2.monolith.model.event.NoteRemovedEvent
import ir.tiroon.fanavard.q2.monolith.model.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.context.event.EventListener
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Component

@Component
@Configuration
class ApplicationEventListeners {

//    @Bean(name = "applicationEventMulticaster")
//    ApplicationEventMulticaster simpleApplicationEventMulticaster() {
//        def eventMulticaster = new SimpleApplicationEventMulticaster()
//        eventMulticaster.setTaskExecutor(?)
//        eventMulticaster
//    }

    @Autowired
    SimpMessagingTemplate messagingTemplate

    @Autowired
    ObjectMapper om


    @EventListener
    void noteChangedEventListener(Object receivedEvent) {

        if (receivedEvent instanceof NoteChangedEvent) {
            NoteChangedEvent ncEvent = receivedEvent

            Set<User> advertisingCollaborators = new HashSet<>()
            advertisingCollaborators.addAll(ncEvent.note.collaborators)

            advertisingCollaborators.add(ncEvent.note.owner)

            advertisingCollaborators.remove(ncEvent.by)

            for (User collaborator : advertisingCollaborators)
                messagingTemplate.convertAndSendToUser(
                        collaborator.phoneNumber,
                        "/queue/messages",
                        om.writeValueAsString(ncEvent)
                )

        }



        else if (receivedEvent instanceof CollaborationAddedEvent) {
            CollaborationAddedEvent cae = receivedEvent

            messagingTemplate.convertAndSendToUser(
                    cae.addedUser.phoneNumber,
                    "/queue/addedCollaborations",
                    om.writeValueAsString(cae.note)
            )
        }



        else if (receivedEvent instanceof CollaborationRemovedEvent) {
            CollaborationRemovedEvent cre = receivedEvent
            messagingTemplate.convertAndSendToUser(
                    cre.removedUser.phoneNumber,
                    "/queue/removedCollaborations",
                    om.writeValueAsString(cre.noteId)
            )
        }


        else if (receivedEvent instanceof NoteRemovedEvent) {
            NoteRemovedEvent nre = receivedEvent
            nre.removedNoteCollaborators.stream().forEach{
                messagingTemplate.convertAndSendToUser(
                        it.phoneNumber,
                        "/queue/removedNotes",
                        om.writeValueAsString(nre.removedNoteId)
                )
            }
        }

    }

}


