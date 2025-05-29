package com.deep.chat.controllers;

import com.deep.chat.config.AppConstants;
import com.deep.chat.entities.Message;
import com.deep.chat.entities.Room;
import com.deep.chat.repositories.RoomRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/rooms")
@CrossOrigin(AppConstants.FRONT_END_BASE_URL)
public class RoomController {

    public RoomController(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }
    private RoomRepository roomRepository;

    @PostMapping
    public ResponseEntity<?> createRoom (@RequestBody String roomId){
        if (roomRepository.findByRoomId(roomId)!=null){
            return ResponseEntity.badRequest().body("Room already exists!");
        }
        // Created new Room
        Room room = new Room();
        room.setRoomId(roomId);
        Room savedRoom = roomRepository.save(room);
        return ResponseEntity.status(HttpStatus.CREATED).body(room);
    }

    // Get room : join
    @GetMapping("/{roomId}")
    public ResponseEntity<?> joinRoom(@PathVariable String roomId){
        Room room = roomRepository.findByRoomId(roomId);
        if(room==null){
            return ResponseEntity.badRequest().body("Room not found!!");
        }
        return ResponseEntity.ok(room);
    }


    // Get message of room
    @GetMapping("/{roomId}/messages")
    public ResponseEntity<List<Message>> getMessages(@PathVariable String roomId, @RequestParam(value = "page", defaultValue = "0", required = false) int page, @RequestParam(value = "size", defaultValue = "20", required = false) int size){
        Room room = roomRepository.findByRoomId(roomId);
        if (room == null){
            return ResponseEntity.badRequest().build();
        }
        // Get messages
        // Pagination
        List<Message> messages = room.getMessages();

        int start = Math.max(0, messages.size()-(page+1)*size);
        int end = Math.min(messages.size(), start+size);
        List<Message> paginatedMessages = messages.subList(start, end);

        return ResponseEntity.ok(paginatedMessages);
    }
}
