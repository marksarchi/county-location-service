package com.marksarchi.countylocationservice.Domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Polygon;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "counties")
@Data
@NoArgsConstructor
public class CountyEntity {
    @Id
    public UUID id;
    public String code;
    public String name;
    @Column(columnDefinition = "geometry")
    public Polygon polygon;
}
