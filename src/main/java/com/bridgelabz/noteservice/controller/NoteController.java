package com.bridgelabz.noteservice.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.bridgelabz.noteservice.dto.NoteDto;
import com.bridgelabz.noteservice.dto.UpdateNoteDto;
import com.bridgelabz.noteservice.entity.LabelEntity;
import com.bridgelabz.noteservice.entity.NoteEntity;
import com.bridgelabz.noteservice.response.Response;
import com.bridgelabz.noteservice.serviceImpl.NoteServiceImpl;

@RestController
@CrossOrigin("*")
public class NoteController {
	@Autowired
	private NoteServiceImpl noteimpl;
//	@Autowired
//	private NoteSearchImpl notesearchimpl;
	
	
	/**
	 * Add Note : used to add note to user
	 * @param notedto
	 * @param token
	 * @param result
	 * @return note added or not response
	 */
	@PostMapping("/notes/addnote")
	public ResponseEntity<Response> addNote(@Valid @RequestBody NoteDto notedto,@RequestHeader String token,BindingResult result)
	{
		if(result.hasErrors())
			return new ResponseEntity<Response>(new Response("invalid details",null,400),HttpStatus.OK);
		return new ResponseEntity<Response>(new Response("note added",noteimpl.addNote(notedto,token),201),HttpStatus.CREATED);
	}
	
	/**
	 * Get All Notes:used to get all notes by that user
	 * @param token
	 * @return display all the notes in response
	 */
	@GetMapping("/notes/getallnotes")
	public List<NoteEntity> getAllNotes(@RequestHeader String token)
	{
		System.out.println(token);
		return noteimpl.getAllNotesByTitle(token);
	}
	
	/**
	 * Delete Note:used to delete note based upon id in that user
	 * @param token
	 * @param noteid
	 * @return deleted note response
	 */
	@DeleteMapping("/notes/deletenotes/{noteid}")
	public ResponseEntity<Response> deleteNoteById(@RequestHeader String token,@PathVariable("noteid") Long noteid)
	{
		noteimpl.deleteNoteById(token,noteid);
		return new ResponseEntity<Response>(new Response("note was deleted",null,200),HttpStatus.OK);
	}
	
	/**
	 * Delete All Notes : used to delete all notes of that user
	 * @param token
	 * @return deleted all notes
	 */
	@DeleteMapping("/notes/deleteallnotes")
	public ResponseEntity<Response> deleteAllNotes(@RequestHeader String token)
	{
		noteimpl.deleteAllNotes(token);
		return new ResponseEntity<Response>(new Response("all notes were deleted",null,200),HttpStatus.OK);
	}
	/**
	 * Update Note : used to update the note content
	 * @param updatenotedto
	 * @param token
	 * @param noteid
	 * @return display updated note
	 */
	@PutMapping("/notes/updatenote/{noteId}")
	public ResponseEntity<Response> updateNote(@RequestBody UpdateNoteDto updatenotedto,@PathVariable("noteId") Long noteId,@RequestHeader String token)
	{
		NoteEntity note=noteimpl.updateNote(token,noteId,updatenotedto);
		return new ResponseEntity<Response>(new Response(note.getTitle()+"note was updated",note,200),HttpStatus.OK);
	}
	
	/**
	 * pin note : used to pin the note
	 * @param token
	 * @param noteid
	 * @return to display the note
	 */
	@PutMapping("/notes/ispinnote/{noteid}")
	public ResponseEntity<Response> isPinNote(@RequestHeader String token,@PathVariable("noteid") Long noteid)
	{
		NoteEntity note=noteimpl.isPinNote(token,noteid);
		return new ResponseEntity<Response>(new Response(note.getTitle()+" was pinned",note,200),HttpStatus.OK);
	}
	/**
	 * Archieve Note : used to archieve the note
	 * @param token
	 * @param noteid
	 * @return to display the note
	 */
	@PutMapping("/notes/isarchieve/{noteid}")
	public ResponseEntity<Response> isArchieve(@RequestHeader String token,@PathVariable("noteid") Long noteid)
	{
		NoteEntity note=noteimpl.isArchieveNote(token,noteid);
		return new ResponseEntity<Response>(new Response(note.getTitle()+" was archieved",note,200),HttpStatus.OK);
	}
	/**
	 * Trash Note : used to store note in trash
	 * @param token
	 * @param noteid
	 * @return to display the note
	 */
	@PutMapping("/notes/istrashed/{noteid}")
	public ResponseEntity<Response> isTrashed(@RequestHeader String token,@PathVariable("noteid") Long noteid)
	{
		NoteEntity note=noteimpl.isTrashed(token,noteid);
		return new ResponseEntity<Response>(new Response(note.getTitle()+" was trashed",note,200),HttpStatus.OK);
	}
	/**
	 * Remind Me : used to set reminder
	 * @param token
	 * @param noteid
	 * @return remind response
	 */
	@PutMapping("/notes/remindme/{noteid}")
	public ResponseEntity<Response> remindMe(@RequestBody String reminderDate,@RequestHeader String token,@PathVariable("noteid") Long noteid,BindingResult result)
	{

		if(result.hasErrors())
		return new ResponseEntity<Response>(new Response("invalid details",null,400),HttpStatus.BAD_REQUEST);
		NoteEntity note=noteimpl.remindMe(token,noteid,reminderDate);
		return new ResponseEntity<Response>(new Response(note.getReminde()+" was set",note,200),HttpStatus.OK);
	}
	/**
	 * Get All Pin-Notes : used to get all pinned notes
	 * @param token
	 * @return pinned notes
	 */
	@GetMapping("/notes/getallpins")
	public ResponseEntity<Response> getAllPinNotes(@RequestHeader String token)
	{
		return new ResponseEntity<Response>(new Response("your pinned notes are",noteimpl.getAllPinNotes(token),200),HttpStatus.OK);
	}
	/**
	 * Get All Archieve-Notes : used to get all archieved notes
	 * @param token
	 * @return archieved notes
	 */
	@GetMapping("/notes/getallarchieves")
	public ResponseEntity<Response> getAllArchieveNotes(@RequestHeader String token)
	{
		List<NoteEntity> notes=noteimpl.getAllArchieveNotes(token);
		return new ResponseEntity<Response>(new Response("your archieve notes are",notes,200),HttpStatus.OK);
	}
	
	@GetMapping("/notes/getremindernotes")
	public ResponseEntity<Response> getReminderNotes(@RequestHeader String token)
	{
		List<NoteEntity> notes=noteimpl.getReminderNotes(token);
		return new ResponseEntity<Response>(new Response("reminder notes",notes,200),HttpStatus.OK);
	}
	/**
	 * 
	 * @param token
	 * @return
	 */
	@GetMapping("/notes/getalltrashnotes")
	public ResponseEntity<Response> getAllTrashedNotes(@RequestHeader String token)
	{
		List<NoteEntity> notes=noteimpl.getAllTrashedNotes(token);
		return new ResponseEntity<Response>(new Response("your trashed notes are",notes,200),HttpStatus.OK);
	}
	/**
	 * Get Note By Id : used to get a note based upon id value
	 * @param token
	 * @param noteid
	 * @return single note response
	 */
	@GetMapping("/notes/getnotebyid/{noteid}")
	public ResponseEntity<Response> getNoteById(@RequestHeader String token,@PathVariable("noteid") Long noteid)
	{
		NoteEntity note=noteimpl.getNoteById(token,noteid);
		return new ResponseEntity<Response>(new Response("your note is",note,200),HttpStatus.OK);
	}
	
	@PutMapping("/notes/updateremindeme/{noteid}")
	public ResponseEntity<Response> updateRemindMe(@RequestBody String remindme,@RequestHeader String token,@PathVariable("noteid") Long noteid,BindingResult result)
	{

		if(result.hasErrors())
		return new ResponseEntity<Response>(new Response("invalid details",null,40),HttpStatus.BAD_REQUEST);
		NoteEntity note=noteimpl.UpdateRemindMe(remindme,token,noteid);
		return new ResponseEntity<Response>(new Response("remindme is updated",note,200),HttpStatus.OK);
	}
	
	@DeleteMapping("/notes/deleteremindeme/{noteid}")
	public ResponseEntity<Response> delteRemindMe(@RequestHeader String token,@PathVariable("noteid") Long noteid)
	{
		noteimpl.deleteRemindMe(token,noteid);
		return new ResponseEntity<Response>(new Response("remindme is deleted",null,200),HttpStatus.OK);
	}
		
	@GetMapping("/notes/allnotesbytitle")
	public ResponseEntity<Response> getAllNotesByTitle(@RequestHeader String token)
	{
		List<NoteEntity> notes=noteimpl.getAllNotesByTitle(token);
		return new ResponseEntity<Response>(new Response("your notes are",notes,200),HttpStatus.OK);
	}
	@PutMapping("/notes/changecolor/{noteid}")
	public ResponseEntity<Response> changeNoteColor(@RequestBody String color,@RequestHeader String token,@PathVariable("noteid") Long noteid)
	{
		NoteEntity note=noteimpl.changeNoteColor(color,token,noteid);
		return new ResponseEntity<Response>(new Response("note color updared",note,200),HttpStatus.OK);
	}
	@GetMapping("/notes/getlabelsfromnote/{noteid}")
	public ResponseEntity<Response> getLabelsFromNote(@RequestHeader String token,@PathVariable("noteid") Long noteid)
	{
		List<LabelEntity> labels=noteimpl.getLabelsFromNote(token,noteid);
		return new ResponseEntity<Response>(new Response("labels from note",labels,200),HttpStatus.OK);
	}
	@PutMapping("/notes/deletereminder/{noteid}")
	public ResponseEntity<Response> deleteReminder(@RequestHeader String token,@PathVariable("noteid") Long noteid)
	{
		noteimpl.deleteReminder(token, noteid);
		return new ResponseEntity<Response>(new Response("reminder deleted",null,200),HttpStatus.OK);
	}
	
}
