package com.example.demo.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.shareDto.ShareRequestDto;
import com.example.demo.service.Iservice.ShareService;

@RestController
@RequestMapping("api/share")
public class ShareController {
   
    private final ShareService shareService;
    
    public ShareController(ShareService shareService){
		this.shareService = shareService;}

   
    @PostMapping("/{noteId}")
    public ResponseEntity<?> shareNote(
            @PathVariable Long noteId,
            @RequestBody List<ShareRequestDto> recipients
    ) {
        shareService.shareNote(noteId, recipients);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{noteId}/removeForMe")
    public ResponseEntity<?> removeForMe(
            @PathVariable Long noteId) {      
        shareService.removeForMe(noteId);
        return ResponseEntity.ok().build();
    }
  

    @DeleteMapping("/{noteId}/{recipientUserId}")
    public ResponseEntity<?> revokeShare(
            @PathVariable Long noteId,
            @PathVariable Long recipientUserId
    ) {       
        shareService.revokeShare(noteId, recipientUserId);
        return ResponseEntity.ok().build();
    }
}

