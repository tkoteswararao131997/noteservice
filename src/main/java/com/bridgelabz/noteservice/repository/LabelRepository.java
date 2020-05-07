package com.bridgelabz.noteservice.repository;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.bridgelabz.noteservice.entity.LabelEntity;

@Repository
public interface LabelRepository extends JpaRepository<LabelEntity, String> {
	@Query(value = "select label_name from labels where label_name=?1",nativeQuery = true)
	Optional<String> isLabelExists(String label_name);
	
	@Query(value="select * from labels where label_id=?1",nativeQuery = true)
	Optional<LabelEntity> getLabelById(Long label_id);
	
	@Query(value = "select * from labels where user_id=?1",nativeQuery = true)
	Optional<List<LabelEntity>> getAllLabels(Long labelid);

	@Query(value = "select notes_note_id from notes_labels where labels_label_id=?1 and notes_note_id=?2 ",nativeQuery = true)
	Optional<Integer> islabelwithnote(Long labelId, Long noteId);
	
	@Modifying
	@Transactional
	@Query(value="SET FOREIGN_KEY_CHECKS=0",nativeQuery = true)
	void setcheck();
	
	@Modifying
	@Transactional
	@Query(value="delete labels,notes_labels from labels inner join notes_labels on labels.label_id=notes_labels.labels_label_id where labels.label_id=?1",nativeQuery = true)
	void deleteLabelFromNote(Long labelId);
	@Modifying
	@Transactional
	@Query(value="delete from labels where label_id=?1",nativeQuery = true)
	void deleteLabel(Long labelId);
	@Query(value = "select notes_note_id from notes_labels where notes_labels.labels_label_id=?2 and notes_labels.notes_note_id",nativeQuery = true)
	Optional<Integer> isnoteinlabel(Long noteId,Long labelId);

	@Query(value="select * from labels where user_id=?1",nativeQuery = true)
	List<LabelEntity> addLabelToList(Long userid);
	@Query(value="delete * from labels where user_id=?1",nativeQuery = true)
	void deleteAllLabels(Long userid);
}
