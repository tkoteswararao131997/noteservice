package com.bridgelabz.noteservice.serviceImpl;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.client.RestTemplate;

import com.bridgelabz.noteservice.dto.NoteDto;
import com.bridgelabz.noteservice.dto.UpdateNoteDto;
import com.bridgelabz.noteservice.entity.LabelEntity;
import com.bridgelabz.noteservice.entity.NoteEntity;
import com.bridgelabz.noteservice.exception.CustomException;
import com.bridgelabz.noteservice.repository.NoteRepository;
import com.bridgelabz.noteservice.response.Response;
import com.bridgelabz.noteservice.service.NoteServiceInf;
import com.bridgelabz.noteservice.utility.JwtOperations;

@Service
@CrossOrigin("*")
public class NoteServiceImpl implements NoteServiceInf {
	@Autowired
	private NoteRepository noterepo;
	@Autowired
	private RestTemplate restTemplate;
	private JwtOperations jwt=new JwtOperations();
	private NoteEntity noteentity=new NoteEntity();
	public Response isUserExists(Long id)
	{
		Response result=restTemplate.getForEntity("http://localhost:8090/user/getuserbyid/"+id,Response.class).getBody();
		return result;
	}
	@Override
	public NoteEntity addNote(NoteDto notedto, String token) {
		Long id=jwt.parseJWT(token);
		isUserExists(id);
		BeanUtils.copyProperties(notedto, noteentity);
		noteentity.setCreateDate(LocalDateTime.now());
		noteentity.setUserId(id);
		NoteEntity note=noterepo.save(noteentity);
		return note;
		
	}
	@Override
	public List<NoteEntity> getAllNotes(String token) {
		long id=jwt.parseJWT(token);
		isUserExists(id);
		List<NoteEntity> notes=noterepo.getAllNotes(id).orElseThrow(() -> new CustomException("no notes in the list",HttpStatus.OK,null));
		return notes;
	}
	@Override
	public void deleteNoteById(String token, Long noteid) {
		Long userid= jwt.parseJWT(token);
		isUserExists(userid);
		NoteEntity note=getNoteById(noteid, userid);
		noterepo.delete(note);
	}
	
	public NoteEntity getNoteById(Long noteid,Long userid)
	{
		return noterepo.getNoteById(noteid, userid).orElseThrow(() -> new CustomException("no notes in the list",HttpStatus.OK,null));
	}
	@Override
	public void deleteAllNotes(String token) 
	{
		long userid= jwt.parseJWT(token);
		isUserExists(userid);
		noterepo.getAllNotes(userid);
		noterepo.deleteAllNotes(userid);
	}
	@Override
	public NoteEntity updateNote(String token, Long noteid, UpdateNoteDto updatenotedto) {
		long userid= jwt.parseJWT(token);
		isUserExists(userid);
		NoteEntity note=getNoteById(noteid, userid);
		BeanUtils.copyProperties(updatenotedto, note);
		noterepo.save(note);
		return note;
	}
	@Override
	public NoteEntity isPinNote(String token, Long noteid) {
		long userid= jwt.parseJWT(token);
		isUserExists(userid);
		NoteEntity note=getNoteById(noteid, userid);
		if(note.isPinned()==false)
			note.setPinned(true);
		else
			note.setPinned(false);
		note.setArchieve(false);
		note.setTrashed(false);
		noterepo.save(note);
		return note;
	}
	@Override
	public NoteEntity isArchieveNote(String token, Long noteid) {
		long userid= jwt.parseJWT(token);
		isUserExists(userid);
		NoteEntity note=getNoteById(noteid, userid);
		note.setPinned(false);
		if(note.isArchieve()==false)
			note.setArchieve(true);
		else
			note.setArchieve(false);
		note.setTrashed(false);
		noterepo.save(note);
		return note;
	}
	@Override
	public NoteEntity isTrashed(String token, Long noteid) {
		System.out.println(token);
		long userid= jwt.parseJWT(token);
		isUserExists(userid);
		NoteEntity note=getNoteById(noteid, userid);
		note.setPinned(false);
		note.setArchieve(false);
		if(note.isTrashed()==false)
			note.setTrashed(true);
		else
			note.setTrashed(false);
		noterepo.save(note);
		return note;
	}
	@Override
	public NoteEntity remindMe(String token, Long noteid,String reminderDate) {
		long userid= jwt.parseJWT(token);
		isUserExists(userid);
		NoteEntity note=getNoteById(noteid, userid);
		note.setReminde(reminderDate);
		noterepo.save(note);
		return note;
	}
	public List<NoteEntity> getAllPinNotes(String token) {
		long id=jwt.parseJWT(token);
		isUserExists(id);
		List<NoteEntity> notes=noterepo.getAllNotes(id).orElseThrow(null);
		List<NoteEntity> pinNotes=notes.stream().filter(archieve -> archieve.isPinned()==true).collect(Collectors.toList());		
		return pinNotes;
	}
	public List<NoteEntity> getAllArchieveNotes(String token) {
		long id=jwt.parseJWT(token);
		isUserExists(id);
		List<NoteEntity> notes=noterepo.getAllNotes(id).orElseThrow(null);
		List<NoteEntity> archieveNotes=notes.stream().filter(archieve -> archieve.isArchieve()==true).collect(Collectors.toList());		
		return archieveNotes;
	}
	public List<NoteEntity> getReminderNotes(String token) {
		long userid=jwt.parseJWT(token);
		isUserExists(userid);
		List<NoteEntity> notes=noterepo.getAllNotes(userid).orElseThrow(null);
		List<NoteEntity> ReminderNotes=notes.stream().filter(reminder -> reminder.getReminde()!=null).collect(Collectors.toList());		
		return ReminderNotes;
	}
	public NoteEntity getNoteById(String token, long noteid) {
		long userid=jwt.parseJWT(token);
		return noterepo.getNoteById(noteid, userid).orElseThrow(() -> new CustomException("no notes in the list",HttpStatus.OK,null));
	}
	public NoteEntity UpdateRemindMe(String remindme, String token, long noteid) {
		long userid=jwt.parseJWT(token);
		isUserExists(userid);
		NoteEntity note=getNoteById(noteid, userid);
		note.setReminde(remindme);
		noterepo.save(note);
		return note;
	}
	public void deleteRemindMe(String token, long noteid) {
		long userid=jwt.parseJWT(token);
		isUserExists(userid);
		NoteEntity note=getNoteById(noteid, userid);
		note.setReminde(null);
		noterepo.save(note);
	}
	public List<NoteEntity> getAllNotesByTitle(String token) {
		long id=jwt.parseJWT(token);
		isUserExists(id);
		List<NoteEntity> notes=noterepo.getAllNotes(id).orElseThrow(() -> new CustomException("no notes in the list",HttpStatus.OK,null));
		List<NoteEntity>sortNotes=notes.parallelStream().sorted(Comparator.comparing(NoteEntity::getTitle)).collect(Collectors.toList());
		return sortNotes;
	}
	public List<NoteEntity> getAllTrashedNotes(String token) {
		long id=jwt.parseJWT(token);
		isUserExists(id);
		List<NoteEntity> notes=noterepo.getAllNotes(id).orElseThrow(null);
		List<NoteEntity> pinNotes=notes.stream().filter(archieve -> archieve.isTrashed()==true).collect(Collectors.toList());		
		return pinNotes;
	}
	public NoteEntity changeNoteColor(String color, String token, long noteid) {
		long userid=jwt.parseJWT(token);
		isUserExists(userid);
		NoteEntity note=getNoteById(noteid, userid);
		note.setColor(color);
		noterepo.save(note);
		return note;
	}
	public List<LabelEntity> getLabelsFromNote(String token, long noteid) {
		long userid=jwt.parseJWT(token);
		isUserExists(userid);
		NoteEntity note=getNoteById(noteid, userid);
		return note.getLabels();
	}
	public void deleteReminder(String token,Long noteid) {
		long userid=jwt.parseJWT(token);
		isUserExists(userid);
		NoteEntity note=getNoteById(noteid, userid);
		note.setReminde(null);
		noterepo.save(note);
	}
}

