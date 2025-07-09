package com.example.demo.service.Iservice;

import java.util.List;

import com.example.demo.dto.shareDto.ShareRequestDto;
import com.example.demo.entity.User;

public interface ShareService {

	void revokeShare(Long noteId, Long recipientUserId);
	
	void shareNote(Long noteId, List<ShareRequestDto> recipients);

	
	void removeForMe(Long noteId);


}
