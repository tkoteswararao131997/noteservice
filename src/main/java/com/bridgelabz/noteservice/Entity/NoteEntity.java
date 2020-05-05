package com.bridgelabz.noteservice.Entity;

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
public class NoteEntity implements Comparable<NoteEntity>{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long noteId;
	@NotBlank(message = "title must not be blank")
	private String title;
	private String description;
	private String color;
	private boolean isPinned;
	private String reminde;
	private boolean isArchieve;
	private LocalDateTime createDate;
	private LocalDateTime UpdateDate;
	private boolean isTrashed;
	
	@ManyToMany(cascade = {CascadeType.MERGE},fetch = FetchType.LAZY)
	private List<LabelEntity> labels;
	
//	@ManyToMany(cascade = {CascadeType.MERGE},fetch = FetchType.LAZY)
//	private List<UserEntity> collaborators;

	@Override
	public int compareTo(NoteEntity arg0) {
		// TODO Auto-generated method stub
		return 0;
	}
	

}