package com.example.moviemate.movie.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
public class ChatRoom {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "chatroom_name", nullable = false)
  private String chatroomName;

  @OneToMany(mappedBy = "chatRoom")
  private List<ChatMember> chatMembers = new ArrayList<>();
}
