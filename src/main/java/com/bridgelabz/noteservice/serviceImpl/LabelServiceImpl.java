package com.bridgelabz.noteservice.serviceImpl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.client.RestTemplate;

import com.bridgelabz.noteservice.dto.LabelDto;
import com.bridgelabz.noteservice.entity.LabelEntity;
import com.bridgelabz.noteservice.entity.NoteEntity;
import com.bridgelabz.noteservice.exception.CustomException;
import com.bridgelabz.noteservice.repository.LabelRepository;
import com.bridgelabz.noteservice.repository.NoteRepository;
import com.bridgelabz.noteservice.service.LabelServiceInf;
import com.bridgelabz.noteservice.utility.JwtOperations;

@Service
@CrossOrigin("*")
public class LabelServiceImpl implements LabelServiceInf 
{
	@Autowired
	private LabelRepository labelrepo;
	private LabelEntity labelentity=new LabelEntity();
	@Autowired
	private JwtOperations jwt;
	@Autowired
	private NoteServiceImpl noteimpl;
	@Autowired
	private NoteRepository noterepo;
	@Autowired
	private RestTemplate restTemplate;
	public void isUserExists(Long id)
	{
		restTemplate.getForEntity("http://localhost:8090/user/getuserbyid/"+id,Void.class).getBody();
	}
	@Override
	public LabelEntity createLabel(LabelDto labeldto,String token) 
	{
		Long id=jwt.parseJWT(token);
		isUserExists(id);
		isLabelExists(labeldto.getLabelName());
		labelentity.setLabelName(labeldto.getLabelName());
		labelentity.setCreateDate(LocalDateTime.now());
		labelentity.setUserId(id);
		LabelEntity label2=labelrepo.save(labelentity);
		return labelentity;
	}
	private void isLabelExists(String labelName) {
		if(labelrepo.isLabelExists(labelName).isPresent())
			throw new CustomException("label already exists",HttpStatus.NOT_ACCEPTABLE,null);	
	}
	@Override
	public LabelEntity addNoteToLabel(Long noteid, String token, Long labelid) {
		Long userid=jwt.parseJWT(token);
		isUserExists(userid);
		NoteEntity note=noteimpl.getNoteById(noteid, userid);
		LabelEntity label=getLabelById(labelid);
		boolean present=labelrepo.islabelwithnote(labelid, noteid).isPresent();
		if(present==true)
			throw new CustomException("label already exists",HttpStatus.NOT_ACCEPTABLE,null);	
		note.getLabels().add(label);
		noterepo.save(note);
		return label;
	}
	
	@Override
	public LabelEntity getLabelById(Long labelid)
	{
		System.out.println();
		return labelrepo.getLabelById(labelid).orElseThrow(() -> new CustomException("no label is present",HttpStatus.NOT_ACCEPTABLE,null));
	}
	@Override
	public LabelEntity updateLabel(LabelDto labeldto, String token, Long labelid) {
		Long userid=jwt.parseJWT(token);
		isUserExists(userid);
		LabelEntity label=getLabelById(labelid);
		label.setLabelName(labeldto.getLabelName());
		label.setUpdateDate(LocalDateTime.now());
		labelrepo.save(label);
		return getLabelById(label.getLabelId());
	}
	@Override
	public void deleteLabel(String token, Long labelid) {
		Long userid=jwt.parseJWT(token);
		isUserExists(userid);
		LabelEntity label=getLabelById(labelid);
		labelrepo.setcheck();
		labelrepo.deleteLabelFromNote(labelid);
		labelrepo.deleteLabel(labelid);
		
//		user.getNotes().remove(label);
//		userrepo.save(user);
		
	}
	@Override
	public List<LabelEntity> getAllLabels(String token) {
		
		return labelrepo.getAllLabels(jwt.parseJWT(token)).orElseThrow(() -> new CustomException("no labels found",HttpStatus.NOT_FOUND,null));
	}
	public void deleteLabelFromNote(String token, Long labelid, Long noteid) {
		Long userid=jwt.parseJWT(token);
		isUserExists(userid);
		NoteEntity note=noteimpl.getNoteById(noteid, userid);
		LabelEntity label=labelrepo.getLabelById(labelid).orElseThrow(() -> new CustomException("no label is present",HttpStatus.NOT_FOUND,null));
		int id=labelrepo.islabelwithnote(label.getLabelId(),note.getNoteId()).orElseThrow(() -> new CustomException("label not present",HttpStatus.NOT_FOUND,null));
		System.out.println(id);
		note.getLabels().remove(label);
		noterepo.save(note);
	}
	public List<NoteEntity> getnotesfromlabel(String token, Long labelid) {
		Long userid=jwt.parseJWT(token);
		isUserExists(userid);
		LabelEntity label=labelrepo.getLabelById(labelid).orElseThrow(() -> new CustomException("no label is present",HttpStatus.NOT_FOUND,null));
		List<NoteEntity> notelist=label.getNotes();
		return notelist;
	}
	public void isnoteinlabel(Long noteId,Long labelId)
	{
		labelrepo.isnoteinlabel(noteId,labelId);
	}
	public List<LabelEntity> createLabelAddNote(String token, LabelDto labeldto, Long noteid) {
		Long userid=jwt.parseJWT(token);
		isUserExists(userid);
		NoteEntity note=noteimpl.getNoteById(noteid, userid);
		isLabelExists(labeldto.getLabelName());
		labelentity.setLabelName(labeldto.getLabelName());
		labelentity.setCreateDate(LocalDateTime.now());
		labelentity.setUpdateDate(LocalDateTime.now());
		labelentity.setUserId(userid);
		LabelEntity label2=labelrepo.save(labelentity);
		List<LabelEntity>  labels = labelrepo.addLabelToList(userid);
		labels.add(labelentity);
		addNoteToLabel(noteid, token, label2.getLabelId());
		return labels;
		
	}
	public void deleteAllLabels(String token) {
		Long userid=jwt.parseJWT(token);
		isUserExists(userid);
		labelrepo.deleteAllLabels(userid);
		
	}
	

}