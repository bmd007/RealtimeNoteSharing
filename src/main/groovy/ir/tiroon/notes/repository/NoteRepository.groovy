package ir.tiroon.notes.repository

import ir.tiroon.fanavard.q2.monolith.model.Note
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface NoteRepository extends JpaRepository<Note, Long> {
}
