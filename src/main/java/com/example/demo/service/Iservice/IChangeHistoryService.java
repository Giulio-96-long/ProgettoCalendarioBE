package com.example.demo.service.Iservice;

import java.time.LocalDateTime;

import com.example.demo.entity.Note;
import com.example.demo.entity.User;

public interface IChangeHistoryService {

	void saveChange(String modifiedEntity, Note note, String changeType, String previousData, String newData,
			User modifiedBy, LocalDateTime modificationDate);

}
