package com.example.moviemate.movie.entity;

import com.example.moviemate.movie.dto.PeopleDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;

import jakarta.persistence.Id;

import jakarta.persistence.Transient;
import lombok.*;
import org.springframework.data.domain.Persistable;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class People implements Persistable<String> {

  @Id
  private String id;

  @Column(nullable = false)
  private String peopleNm;

  @Column
  private String peopleNmEn;

  @Column
  private String repRoleNm;

  @Column(length = 500)
  private String filmoNames;
  public void updateFromDto(PeopleDto peopleDto) {
    this.peopleNm = peopleDto.getPeopleNm();
    this.peopleNmEn = peopleDto.getPeopleNmEn();
    this.repRoleNm = peopleDto.getRepRoleNm();
    this.filmoNames = peopleDto.getFilmoNames();
  }

  @Transient
  protected boolean isNew = true;

  @Override
  public String getId() {
    return id;
  }

  @Override
  public boolean isNew() {
    return isNew;
  }

  public void setIsNewStatus(boolean isNew) {
    this.isNew = isNew;
  }

}
