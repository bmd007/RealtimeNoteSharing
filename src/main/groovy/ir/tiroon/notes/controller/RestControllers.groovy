package ir.tiroon.notes.controller

import ir.tiroon.notes.model.Note
import ir.tiroon.notes.model.User
import ir.tiroon.notes.model.event.NoteChangedEvent
import ir.tiroon.notes.service.NoteServices
import ir.tiroon.notes.service.UserServices
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

import java.security.Principal

@RestController
class RestControllers {

    @Autowired
    UserServices userServices

    @Autowired
    NoteServices noteServices

    // Note controllers
    @PostMapping(path = "/addNote", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    Note addNote(@RequestBody Note note, Principal principal) {
        noteServices.save(note, principal.name)
        note
    }

    @GetMapping("/notes")
    @ResponseBody
    Set<Note> notes(Principal principal) {
        noteServices.list(principal.name)
    }

    @GetMapping("/note/{noteId}")
    @ResponseBody
    Note note(@PathVariable("noteId") Long noteId, Principal principal) {
        noteServices.get(noteId, principal.name)
    }

    @GetMapping("/removeNote/{noteId}")
    ResponseEntity removeNote(@PathVariable("noteId") Long noteId, Principal principal) {
        noteServices.deleteById(noteId, principal.name) ?
                ResponseEntity.ok().build() : ResponseEntity.badRequest().build()
    }

    @PostMapping(path = "/noteChanged", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    NoteChangedEvent noteChanged(@RequestBody Note changingNote, Principal principal) {
        def changedNote = noteServices.update(changingNote, principal.name)
        changedNote == null ? null : noteServices.noteChangedEvent(changedNote, principal.getName())
    }

    //Collaboration Controllers
    @PostMapping(path = "/addCollaborationByList/{noteId}", consumes = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    Set<User> addCollaborationByList(Principal principal, @RequestBody String collaborators, @PathVariable("noteId") Long noteId) {
        noteServices.addCollaborationByList(collaborators.split(","), noteId, principal.name)
    }

    @GetMapping("/addCollaboration/{collaboratorPhone}/{noteId}")
    @ResponseBody
    User addCollaboration(Principal principal, @PathVariable("collaboratorPhone") String phone, @PathVariable("noteId") Long noteId) {
        noteServices.addCollaboration(phone, noteId, principal.name)
    }


    @GetMapping("/collaborators/{noteId}")
    @ResponseBody
    Set<User> collaborators(@PathVariable("noteId") Long noteId, Principal principal) {
        noteServices.collaborators(noteId, principal.name)
    }


    @GetMapping("/removeCollaboration/{collaboratorPhone}/{noteId}")
    ResponseEntity<String> removeCollaboration(Principal principal,
                                               @PathVariable("collaboratorPhone") String collaboratorPhone,
                                               @PathVariable("noteId") Long noteId) {
        noteServices.removeCollaboration(collaboratorPhone, noteId, principal.name) ?
                ResponseEntity.ok().body(collaboratorPhone) : ResponseEntity.badRequest().build()
    }

    @GetMapping("/whoIsNotCollaborating/{noteId}")
    @ResponseBody
    Set<User> whoIsNotCollaborating(@PathVariable("noteId") Long noteId, Principal principal) {
        userServices.whoIsNotCollaborating(noteId, principal.name)
    }


    //User controllers
    @GetMapping("/loggedInUser")
    @ResponseBody
    User loggedInUser(Principal principal) {
        userServices.get(principal.name)
    }

    @GetMapping("/users")
    @ResponseBody
    List<User> users() {
        userServices.list()
    }

    @GetMapping("/removeUser/{phoneNumber}")
    ResponseEntity removeUser(@PathVariable("phoneNumber") String phoneNumber, Principal principal) {
        userServices.delete(phoneNumber, principal.name) ?
                ResponseEntity.ok().build() : ResponseEntity.badRequest().build()
    }

//    @Autowired
//    HttpSessionCsrfTokenRepository csrfTokenRepository
//security
//    @GetMapping(path = "/csrf", produces = "text/plain")
//    @ResponseBody
//    String csrfEndpoint(HttpServletRequest request){
//       csrfTokenRepository.loadToken(request).token
//    }
}