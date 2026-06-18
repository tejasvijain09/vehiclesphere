package com.vehiclesphere.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Review extends BaseEntity{

    private Integer rating;

    @Column(length = 1000)
    private String comment;
}
