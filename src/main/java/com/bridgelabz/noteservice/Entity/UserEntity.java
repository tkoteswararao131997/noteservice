package com.bridgelabz.noteservice.Entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name="users")
public class UserEntity implements Serializable{
    public UserEntity(String string, int i) {
		this.name=string;
		this.userid=(long) i;
	}

	public UserEntity() {
		// TODO Auto-generated constructor stub
	}

	private static final long serialVersionUID = 7156526077883281623L;
    
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long userid;
	private String name;
	private String password;
	private String mobileNumber;
	private String email;
	private LocalDateTime createDate;
	private LocalDateTime updateDate;
	private boolean isVerifyEmail=false;
	private String profile;
	@OneToMany(cascade = {CascadeType.ALL},fetch = FetchType.LAZY)
	@JoinColumn(name="userid")
	private List<NoteEntity> notes;
//	
//	@OneToMany(cascade = {CascadeType.ALL},fetch = FetchType.LAZY)
//	@JoinColumn(name="userid")
//	private List<LabelEntity> labels;
//	
//	@ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH},fetch = FetchType.LAZY,mappedBy = "collaborators")
//	private List<NoteEntity> collaboratorNotes;
}
