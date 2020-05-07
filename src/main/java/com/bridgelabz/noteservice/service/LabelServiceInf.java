package com.bridgelabz.noteservice.service;

import java.util.List;

import com.bridgelabz.noteservice.dto.LabelDto;
import com.bridgelabz.noteservice.entity.LabelEntity;

public interface LabelServiceInf {
	LabelEntity createLabel(LabelDto labeldto, String token);

	LabelEntity addNoteToLabel(Long noteid, String token,Long labelid);

	LabelEntity updateLabel(LabelDto labeldto, String token, Long labelid);

	List<LabelEntity> getAllLabels(String token);

	void deleteLabel(String token, Long labelid);

	LabelEntity getLabelById(Long labelid);
}
