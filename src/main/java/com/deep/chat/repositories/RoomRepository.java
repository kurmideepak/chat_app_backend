package com.deep.chat.repositories;

import com.deep.chat.entities.Room;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RoomRepository extends MongoRepository<Room, String> {
//Get room by room id
    Room findByRoomId(String roomId);
}
