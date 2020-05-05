package com.bridgelabz.noteservice.Controller;

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

import com.bridgelabz.noteservice.Dto.NoteDto;
import com.bridgelabz.noteservice.Dto.UpdateNoteDto;
import com.bridgelabz.noteservice.Entity.NoteEntity;
import com.bridgelabz.noteservice.Response.Response;
import com.bridgelabz.noteservice.ServiceImpl.NoteServiceImpl;

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
	@PostMapping("/addnote")
	public ResponseEntity<Response> addNote(@Valid @RequestBody NoteDto notedto,@RequestHeader String token,BindingResult result)
	{
		if(result.hasErrors())
			return new ResponseEntity<Response>(new Response("invalid details",null,400,"true"),HttpStatus.OK);
		return new ResponseEntity<Response>(new Response("note added",noteimpl.addNote(notedto,token),201,"true"),HttpStatus.CREATED);
	}
	
	/**
	 * Get All Notes:used to get all notes by that user
	 * @param token
	 * @return display all the notes in response
	 */
	@GetMapping("/getallnotes")
	public List<NoteEntity> getAllNotes(@RequestHeader String token)
	{
		return noteimpl.getAllNotesByTitle(token);
	}
	
	/**
	 * Delete Note:used to delete note based upon id in that user
	 * @param token
	 * @param noteid
	 * @return deleted note response
	 */
	@DeleteMapping("/deletenotes/{noteid}")
	public ResponseEntity<Response> deleteNoteById(@RequestHeader String token,@PathVariable("noteid") long noteid)
	{
		noteimpl.deleteNoteById(token,noteid);
		return new ResponseEntity<Response>(new Response("note was deleted",null,200,"true"),HttpStatus.OK);
	}
	
	/**
	 * Delete All Notes : used to delete all notes of that user
	 * @param token
	 * @return deleted all notes
	 */
	@DeleteMapping("/deleteallnotes")
	public ResponseEntity<Response> deleteAllNotes(@RequestHeader String token)
	{
		noteimpl.deleteAllNotes(token);
		return new ResponseEntity<Response>(new Response("all notes were deleted",null,200,"true"),HttpStatus.OK);
	}
	/**
	 * Update Note : used to update the note content
	 * @param updatenotedto
	 * @param token
	 * @param noteid
	 * @return display updated note
	 */
	@PutMapping("/updatenote/{noteId}")
	public ResponseEntity<Response> updateNote(@RequestBody UpdateNoteDto updatenotedto,@PathVariable("noteId") long noteId,@RequestHeader String token)
	{
		NoteEntity note=noteimpl.updateNote(token,noteId,updatenotedto);
		return new ResponseEntity<Response>(new Response(note.getTitle()+"note was updated",note,200,"true"),HttpStatus.OK);
	}
	
	/**
	 * pin note : used to pin the note
	 * @param token
	 * @param noteid
	 * @return to display the note
	 */
	@PutMapping("/ispinnote/{noteid}")
	public ResponseEntity<Response> isPinNote(@RequestHeader String token,@PathVariable("noteid") long noteid)
	{
		NoteEntity note=noteimpl.isPinNote(token,noteid);
		return new ResponseEntity<Response>(new Response(note.getTitle()+" was pinned",note,200,"true"),HttpStatus.OK);
	}
	/**
	 * Archieve Note : used to archieve the note
	 * @param token
	 * @param noteid
	 * @return to display the note
	 */
	@PutMapping("/isarchieve/{noteid}")
	public ResponseEntity<Response> isArchieve(@RequestHeader String token,@PathVariable("noteid") long noteid)
	{
		NoteEntity note=noteimpl.isArchieveNote(token,noteid);
		return new ResponseEntity<Response>(new Response(note.getTitle()+" was archieved",note,200,"true"),HttpStatus.OK);
	}
	/**
	 * Trash Note : used to store note in trash
	 * @param token
	 * @param noteid
	 * @return to display the note
	 */
	@PutMapping("/istrashed/{noteid}")
	public ResponseEntity<Response> isTrashed(@RequestHeader String token,@PathVariable("noteid") long noteid)
	{
		System.out.println();
		NoteEntity note=noteimpl.isTrashed(token,noteid);
		return new ResponseEntity<Response>(new Response(note.getTitle()+" was trashed",note,200,"true"),HttpStatus.OK);
	}
	/**
	 * Remind Me : used to set reminder
	 * @param token
	 * @param noteid
	 * @return remind response
	 */
	@PutMapping("/remindme/{noteid}")
	public ResponseEntity<Response> remindMe(@RequestBody String reminderDate,@RequestHeader String token,@PathVariable("noteid") long noteid,BindingResult result)
	{

		if(result.hasErrors())
		return new ResponseEntity<Response>(new Response("invalid details",null,400,"true"),HttpStatus.BAD_REQUEST);
		NoteEntity note=noteimpl.remindMe(token,noteid,reminderDate);
		return new ResponseEntity<Response>(new Response(note.getReminde()+" was set",note,200,"true"),HttpStatus.OK);
	}
	/**
	 * Get All Pin-Notes : used to get all pinned notes
	 * @param token
	 * @return pinned notes
	 */
	@GetMapping("/getallpins")
	public ResponseEntity<Response> getAllPinNotes(@RequestHeader String token)
	{
		return new ResponseEntity<Response>(new Response("your pinned notes are",noteimpl.getAllPinNotes(token),200,"true"),HttpStatus.OK);
	}
	/**
	 * Get All Archieve-Notes : used to get all archieved notes
	 * @param token
	 * @return archieved notes
	 */
	@GetMapping("/getallarchieves")
	public ResponseEntity<Response> getAllArchieveNotes(@RequestHeader String token)
	{
		return new ResponseEntity<Response>(new Response("your archieve notes are",noteimpl.getAllArchieveNotes(token),200,"true"),HttpStatus.OK);
	}
	
	@GetMapping("/getremindernotes")
	public ResponseEntity<Response> getReminderNotes(@RequestHeader String token)
	{
		return new ResponseEntity<Response>(new Response("reminder notes",noteimpl.getReminderNotes(token),200,"true"),HttpStatus.OK);
	}
	/**
	 * 
	 * @param token
	 * @return
	 */
	@GetMapping("/getalltrashnotes")
	public ResponseEntity<Response> getAllTrashedNotes(@RequestHeader String token)
	{
		return new ResponseEntity<Response>(new Response("your trashed notes are",noteimpl.getAllTrashedNotes(token),200,"true"),HttpStatus.OK);
	}
	/**
	 * Get Note By Id : used to get a note based upon id value
	 * @param token
	 * @param noteid
	 * @return single note response
	 */
	@GetMapping("/getnotebyid/{noteid}")
	public ResponseEntity<Response> getNoteById(@RequestHeader String token,@PathVariable("noteid") long noteid)
	{
		return new ResponseEntity<Response>(new Response("your note is",noteimpl.getNoteById(token,noteid),200,"true"),HttpStatus.OK);
	}
	
	@PutMapping("/updateremindeme/{noteid}")
	public ResponseEntity<Response> updateRemindMe(@RequestBody String remindme,@RequestHeader String token,@PathVariable("noteid") long noteid,BindingResult result)
	{

		if(result.hasErrors())
		return new ResponseEntity<Response>(new Response("invalid details",null,400,"true"),HttpStatus.BAD_REQUEST);
		return new ResponseEntity<Response>(new Response("remindme is updated",noteimpl.UpdateRemindMe(remindme,token,noteid),200,"true"),HttpStatus.OK);
	}
	
	@DeleteMapping("/deleteremindeme/{noteid}")
	public ResponseEntity<Response> delteRemindMe(@RequestHeader String token,@PathVariable("noteid") long noteid)
	{
		noteimpl.deleteRemindMe(token,noteid);
		return new ResponseEntity<Response>(new Response("remindme is deleted",null,200,"true"),HttpStatus.OK);
	}
		
	@GetMapping("/allnotesbytitle")
	public ResponseEntity<Response> getAllNotesByTitle(@RequestHeader String token)
	{
		return new ResponseEntity<Response>(new Response("your notes are",noteimpl.getAllNotesByTitle(token),200,"true"),HttpStatus.OK);
	}
	@PutMapping("/changecolor/{noteid}")
	public ResponseEntity<Response> changeNoteColor(@RequestBody String color,@RequestHeader String token,@PathVariable("noteid") long noteid)
	{
		return new ResponseEntity<Response>(new Response("note color updared",noteimpl.changeNoteColor(color,token,noteid),200,"true"),HttpStatus.OK);
	}
	@GetMapping("/getlabelsfromnote/{noteid}")
	public ResponseEntity<Response> getLabelsFromNote(@RequestHeader String token,@PathVariable("noteid") long noteid)
	{
		return new ResponseEntity<Response>(new Response("labels from note",noteimpl.getLabelsFromNote(token,noteid),200,"true"),HttpStatus.OK);
	}
	@PutMapping("/deletereminder/{noteid}")
	public ResponseEntity<Response> deleteReminder(@RequestHeader String token,@PathVariable("noteid") Long noteid)
	{
		noteimpl.deleteReminder(token, noteid);
		return new ResponseEntity<Response>(new Response("reminder deleted",null,200,"true"),HttpStatus.OK);
	}
//	@GetMapping("/searchByTitle/{title}")
//	public ResponseEntity<Response> searchByTitle(@RequestHeader String token,@PathVariable("title") String title)
//	{
//		return new ResponseEntity<Response>(new Response("search notes are:",notesearchimpl.searchByTitle(title),200,"true"),HttpStatus.OK);
//	}
	
}
