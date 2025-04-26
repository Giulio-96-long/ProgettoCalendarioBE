package com.example.demo.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.example.demo.entity.ChangeHistory;
import com.example.demo.entity.Note;
import com.example.demo.entity.User;
import com.example.demo.repository.ChangeHistoryRepository;
import com.example.demo.service.Iservice.IChangeHistoryService;

@Service
public class ChangeHistoryService implements IChangeHistoryService {
	
	private final ChangeHistoryRepository changeHistoryRepository;
	
	public ChangeHistoryService(ChangeHistoryRepository changeHistoryRepository) {
		this.changeHistoryRepository = changeHistoryRepository;}
	
	@Override
	public void saveChange(
	    String modifiedEntity,
	    Note note,
	    String changeType,
	    String previousData,
	    String newData,
	    User modifiedBy,
	    LocalDateTime modificationDate
	) {
	    ChangeHistory changeLog = new ChangeHistory();

	    changeLog.setModifiedEntity(modifiedEntity);
	    changeLog.setNote(note);
	    changeLog.setChangeType(changeType);
	    changeLog.setPreviousData(previousData); 
	    changeLog.setNewData(newData);            
	    changeLog.setModifiedBy(modifiedBy);
	    changeLog.setModificationDate(modificationDate != null ? modificationDate : LocalDateTime.now());

	    changeHistoryRepository.save(changeLog);
	}
}
