package com.bridgelabz.noteservice.service;

import java.util.List;

import com.bridgelabz.noteservice.dto.NoteDto;
import com.bridgelabz.noteservice.dto.UpdateNoteDto;
import com.bridgelabz.noteservice.entity.NoteEntity;

public interface NoteServiceInf {
	NoteEntity addNote(NoteDto notedto,String token);

	List<NoteEntity> getAllNotes(String token);

	void deleteNoteById(String token, Long noteid);

	void deleteAllNotes(String token);
	
	NoteEntity updateNote(String token, Long noteid, UpdateNoteDto updatenotedto);

	NoteEntity isPinNote(String token, Long noteid);

	NoteEntity isArchieveNote(String token, Long noteid);

	NoteEntity isTrashed(String token, Long noteid);
	
	NoteEntity remindMe(String token, Long noteid, String reminderDate);
}