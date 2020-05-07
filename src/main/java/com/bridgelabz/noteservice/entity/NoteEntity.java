package com.bridgelabz.noteservice.entity;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import lombok.Data;

@Data
@Entity
@Table(name="notes")
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class,scope = NoteEntity.class)
public class NoteEntity{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long noteId;
	@NotBlank(message = "title must not be blank")
	private String title;
	private Long userId;
	private String description;
	private String color="white";
	private boolean isPinned=false;
	private String reminde=null;
	private boolean isArchieve=false;
	private LocalDateTime createDate;
	private LocalDateTime UpdateDate=null;
	private boolean isTrashed=false;
	@ManyToMany(cascade = {CascadeType.MERGE},fetch = FetchType.LAZY)
	private List<LabelEntity> labels;

}