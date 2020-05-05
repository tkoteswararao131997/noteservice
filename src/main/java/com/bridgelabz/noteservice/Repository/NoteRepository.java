package com.bridgelabz.noteservice.Repository;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.web.bind.annotation.RequestParam;

import com.bridgelabz.noteservice.Entity.NoteEntity;

public interface NoteRepository extends JpaRepository<NoteEntity, String> {
	@Query(value = "select title from notes where title=?1 and userid=?2",nativeQuery = true)
	Optional<String> isNoteExists(@RequestParam String title,long userid);
	
	@Query(value = "select * from notes where userid=?1",nativeQuery = true)
	Optional<List<NoteEntity>> getAllNotes(long userid);
	
	@Query(value="select * from notes where note_id=?1 and userid=?2",nativeQuery = true)
	Optional<NoteEntity> getNoteById(long noteid,long userid);
	
	
	@Modifying
	@Transactional
	@Query(value = "delete from notes where note_id=?1 and userid=?2",nativeQuery =true)
	 void deleteNoteById(long noteid,long userid);
	
	
	@Modifying
	@Transactional
	@Query(value = "delete from notes where userid=?1",nativeQuery =true)
	 void deleteAllNotes(long userid);
	
	@Query(value = "select * from notes where userid=?1 and is_pinned=true",nativeQuery = true)
	Optional<List<NoteEntity>> getAllPinNotes(long userid);

//	@Query(value = "select * from notes where userid=?1 and is_archieve=true",nativeQuery = true)
//	Optional<List<NoteEntity>> getAllArchieveNotes(long userid);
	
}
