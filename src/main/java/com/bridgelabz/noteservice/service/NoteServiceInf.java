package com.bridgelabz.noteservice.service;

import java.util.List;

import com.bridgelabz.noteservice.Dto.NoteDto;
import com.bridgelabz.noteservice.Dto.UpdateNoteDto;
import com.bridgelabz.noteservice.Entity.NoteEntity;

public interface NoteServiceInf {
	NoteEntity addNote(NoteDto notedto,String token);

	List<NoteEntity> getAllNotes(String token);

	void deleteNoteById(String token, long noteid);

	void deleteAllNotes(String token);
	
	NoteEntity updateNote(String token, long noteid, UpdateNoteDto updatenotedto);

	NoteEntity isPinNote(String token, long noteid);

	NoteEntity isArchieveNote(String token, long noteid);

	NoteEntity isTrashed(String token, long noteid);
	
	NoteEntity remindMe(String token, long noteid, String reminderDate);
}