package com.bridgelabz.noteservice.ServiceImpl;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.omg.CORBA.UserException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.client.RestTemplate;

import com.bridgelabz.noteservice.Dto.NoteDto;
import com.bridgelabz.noteservice.Dto.UpdateNoteDto;
import com.bridgelabz.noteservice.Entity.NoteEntity;
import com.bridgelabz.noteservice.Entity.UserEntity;
import com.bridgelabz.noteservice.Exception.CustomException;
import com.bridgelabz.noteservice.Repository.NoteRepository;
import com.bridgelabz.noteservice.Utility.JwtOperations;
import com.bridgelabz.noteservice.service.NoteServiceInf;

@Service
@CrossOrigin("*")
public class NoteServiceImpl implements NoteServiceInf {
	@Autowired
	private NoteRepository noterepo;
	@Autowired
	private RestTemplate restTemplate;
//	@Autowired
//	private UserServiceImpl userimpl;
	@Autowired
	private JwtOperations jwt;
//	@Autowired
//	private UserEntity userentity;
	@Autowired
	private NoteEntity noteentity;
//	@Autowired
//	private UserRepository userrepo;
//	@Autowired
//	NoteSearchImpl elasticRepo;
	public UserEntity getUser(String token)
	{
		//System.out.println(id);
		UserEntity userentity=restTemplate.getForEntity("http://localhost:8090/getuser/"+token,UserEntity.class).getBody();
		System.out.println(userentity+"--------------");
		return userentity;
	}
	@Override
	public NoteEntity addNote(NoteDto notedto, String token) {
		System.out.println(token);
		long id=jwt.parseJWT(token);
		if(noterepo.isNoteExists(notedto.getTitle(),id).isPresent())
			throw new CustomException("note already exists", HttpStatus.NOT_ACCEPTABLE,null,"false");
		UserEntity userentity=getUser(token);
		System.out.println(userentity);
		BeanUtils.copyProperties(notedto, noteentity);
		noteentity.setColor("white");
		noteentity.setArchieve(false);
		noteentity.setCreateDate(LocalDateTime.now());
		noteentity.setUpdateDate(LocalDateTime.now());
		noteentity.setReminde(null);
		noteentity.setPinned(false);
		noteentity.setTrashed(false);
		userentity.getNotes().add(noteentity);
		restTemplate.exchange("http://localhost:8090/saveuser/"+userentity, HttpMethod.PUT,null, String.class);
		List<NoteEntity> notes=userentity.getNotes();
		for(NoteEntity note : notes)
		{
			if(note.getTitle().equals(notedto.getTitle()))
			{
//			try {
//				//elasticRepo.createNote(note);
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			return note;
			}
		}
		return null;
		
	}
	@Override
	public List<NoteEntity> getAllNotes(String token) {
		long id=jwt.parseJWT(token);
		UserEntity userentity=getUser(token);
		List<NoteEntity> notes=noterepo.getAllNotes(id).orElseThrow(() -> new CustomException("no notes in the list",HttpStatus.OK,null,"false"));
		return notes;
	}
	@Override
	public void deleteNoteById(String token, long noteid) {
		long userid= jwt.parseJWT(token);
		UserEntity userentity=getUser(token);
		NoteEntity note=getNoteById(noteid, userid);
		noterepo.delete(note);
		String id=String.valueOf(noteid);
//		try {
//			elasticRepo.deleteNote(id);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
	
	public NoteEntity getNoteById(long noteid,long userid)
	{
		return noterepo.getNoteById(noteid, userid).orElseThrow(() -> new CustomException("no notes in the list",HttpStatus.OK,null,"false"));
	}
	@Override
	public void deleteAllNotes(String token) 
	{
		long userid= jwt.parseJWT(token);
		UserEntity userentity=getUser(token);
		noterepo.getAllNotes(userid);
		noterepo.deleteAllNotes(userid);
	}
	@Override
	public NoteEntity updateNote(String token, long noteid, UpdateNoteDto updatenotedto) {
		long userid= jwt.parseJWT(token);
		UserEntity userentity=getUser(token);
		NoteEntity note=getNoteById(noteid, userid);
		BeanUtils.copyProperties(updatenotedto, note);
		noterepo.save(note);
//		try {
//			elasticRepo.updateNote(note);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		return note;
	}
	@Override
	public NoteEntity isPinNote(String token, long noteid) {
		long userid= jwt.parseJWT(token);
		UserEntity userentity=getUser(token);
		NoteEntity note=getNoteById(noteid, userid);
		if(note.isPinned()==false)
			note.setPinned(true);
		else
			note.setPinned(false);
		note.setArchieve(false);
		note.setTrashed(false);
		noterepo.save(note);
//		try {
//			elasticRepo.updateNote(note);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		return note;
	}
	@Override
	public NoteEntity isArchieveNote(String token, long noteid) {
		long userid= jwt.parseJWT(token);
		UserEntity userentity=getUser(token);
		NoteEntity note=getNoteById(noteid, userid);
		note.setPinned(false);
		if(note.isArchieve()==false)
		{
			note.setArchieve(true);
		}
		else
		{
			note.setArchieve(false);
		}
		note.setTrashed(false);
		noterepo.save(note);
//		try {
//			elasticRepo.updateNote(note);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		return note;
	}
	@Override
	public NoteEntity isTrashed(String token, long noteid) {
		System.out.println(token);
		long userid= jwt.parseJWT(token);
		UserEntity userentity=getUser(token);
		NoteEntity note=getNoteById(noteid, userid);
		note.setPinned(false);
		note.setArchieve(false);
		if(note.isTrashed()==false)
			note.setTrashed(true);
		else
			note.setTrashed(false);
		noterepo.save(note);
//		try {
//			elasticRepo.updateNote(note);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		return note;
	}
	@Override
	public NoteEntity remindMe(String token, long noteid,String reminderDate) {
		long userid= jwt.parseJWT(token);
		UserEntity userentity=getUser(token);
		NoteEntity note=getNoteById(noteid, userid);
		note.setReminde(reminderDate);
		noterepo.save(note);
		return note;
	}
	public List<NoteEntity> getAllPinNotes(String token) {
		long id=jwt.parseJWT(token);
		List<NoteEntity> notes=noterepo.getAllNotes(id).orElseThrow(null);
		List<NoteEntity> pinNotes=notes.stream().filter(archieve -> archieve.isPinned()==true).collect(Collectors.toList());		
		return pinNotes;
	}
	public List<NoteEntity> getAllArchieveNotes(String token) {
		long id=jwt.parseJWT(token);
		UserEntity userentity=getUser(token);
		List<NoteEntity> notes=noterepo.getAllNotes(id).orElseThrow(null);
		List<NoteEntity> archieveNotes=notes.stream().filter(archieve -> archieve.isArchieve()==true).collect(Collectors.toList());		
		return archieveNotes;
	}
	public List<NoteEntity> getReminderNotes(String token) {
		long userid=jwt.parseJWT(token);
		UserEntity userentity=getUser(token);
		List<NoteEntity> notes=noterepo.getAllNotes(userid).orElseThrow(null);
		List<NoteEntity> ReminderNotes=notes.stream().filter(reminder -> reminder.getReminde()!=null).collect(Collectors.toList());		
		return ReminderNotes;
	}
	public NoteEntity getNoteById(String token, long noteid) {
		long userid=jwt.parseJWT(token);
		return noterepo.getNoteById(noteid, userid).orElseThrow(() -> new CustomException("no notes in the list",HttpStatus.OK,null,"false"));
	}
	public NoteEntity UpdateRemindMe(String remindme, String token, long noteid) {
		long userid=jwt.parseJWT(token);
		UserEntity userentity=getUser(token);
		NoteEntity note=getNoteById(noteid, userid);
		note.setReminde(remindme);
		noterepo.save(note);
//		try {
//			elasticRepo.updateNote(note);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		return note;
	}
	public void deleteRemindMe(String token, long noteid) {
		long userid=jwt.parseJWT(token);
		UserEntity userentity=getUser(token);
		NoteEntity note=getNoteById(noteid, userid);
		note.setReminde(null);
//		try {
//			elasticRepo.updateNote(note);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		noterepo.save(note);
	}
	public List<NoteEntity> getAllNotesByTitle(String token) {
		long id=jwt.parseJWT(token);
		UserEntity userentity=getUser(token);
		List<NoteEntity> notes=noterepo.getAllNotes(id).orElseThrow(() -> new CustomException("no notes in the list",HttpStatus.OK,null,"false"));
		List<NoteEntity>sortNotes=notes.parallelStream().sorted(Comparator.comparing(NoteEntity::getTitle)).collect(Collectors.toList());
		return sortNotes;
	}
	public List<NoteEntity> getAllTrashedNotes(String token) {
		long id=jwt.parseJWT(token);
		UserEntity userentity=getUser(token);
		List<NoteEntity> notes=noterepo.getAllNotes(id).orElseThrow(null);
		List<NoteEntity> pinNotes=notes.stream().filter(archieve -> archieve.isTrashed()==true).collect(Collectors.toList());		
		return pinNotes;
	}
	public NoteEntity changeNoteColor(String color, String token, long noteid) {
		long userid=jwt.parseJWT(token);
		UserEntity userentity=getUser(token);
		NoteEntity note=getNoteById(noteid, userid);
		note.setColor(color);
		noterepo.save(note);
//		try {
//			elasticRepo.updateNote(note);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		return note;
	}
	public Object getLabelsFromNote(String token, long noteid) {
		long userid=jwt.parseJWT(token);
		UserEntity userentity=getUser(token);
		NoteEntity note=getNoteById(noteid, userid);
		return note.getLabels();
	}
	public void deleteReminder(String token,Long noteid) {
		long userid=jwt.parseJWT(token);
		UserEntity userentity=getUser(token);
		NoteEntity note=getNoteById(noteid, userid);
		note.setReminde(null);
		noterepo.save(note);
//		try {
//			elasticRepo.updateNote(note);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
	public List<NoteEntity> searchByTitle(String token, String title) {
		long id=jwt.parseJWT(token);
		UserEntity userentity=getUser(token);
		List<NoteEntity> notes=userentity.getNotes();
		List<NoteEntity> searchednotes=new ArrayList<>();
		for(NoteEntity note : notes)
		{
			if(note.getTitle().contains(title))
			{
				searchednotes.add(note);
			}
		}
		return searchednotes;
	}
	
	

}

