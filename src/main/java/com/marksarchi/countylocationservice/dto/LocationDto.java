package com.marksarchi.countylocationservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Point;
@Data
@NoArgsConstructor
@JsonIgnoreProperties
public class LocationDto {
    String longitude;
    String latitude;
    Point point;
}
