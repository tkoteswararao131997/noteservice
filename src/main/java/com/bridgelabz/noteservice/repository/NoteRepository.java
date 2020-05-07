package com.bridgelabz.noteservice.repository;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.web.bind.annotation.RequestParam;

import com.bridgelabz.noteservice.entity.NoteEntity;

public interface NoteRepository extends JpaRepository<NoteEntity, String> {
	@Query(value = "select title from notes where title=?1 and user_id=?2",nativeQuery = true)
	Optional<String> isNoteExists(@RequestParam String title,Long userid);
	
	@Query(value = "select * from notes where user_id=?1",nativeQuery = true)
	Optional<List<NoteEntity>> getAllNotes(Long userid);
	
	@Query(value="select * from notes where note_id=?1 and user_id=?2",nativeQuery = true)
	Optional<NoteEntity> getNoteById(Long noteid,Long userid);
	
	
	@Modifying
	@Transactional
	@Query(value = "delete from notes where note_id=?1 and user_id=?2",nativeQuery =true)
	 void deleteNoteById(Long noteid,Long userid);
	
	
	@Modifying
	@Transactional
	@Query(value = "delete from notes where user_id=?1",nativeQuery =true)
	 void deleteAllNotes(Long userid);
	
	@Query(value = "select * from notes where user_id=?1 and is_pinned=true",nativeQuery = true)
	Optional<List<NoteEntity>> getAllPinNotes(Long userid);

//	@Query(value = "select * from notes where userid=?1 and is_archieve=true",nativeQuery = true)
//	Optional<List<NoteEntity>> getAllArchieveNotes(long userid);
	
}
