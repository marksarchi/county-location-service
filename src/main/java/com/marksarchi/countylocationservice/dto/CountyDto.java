package com.marksarchi.countylocationservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Polygon;

import java.io.Serializable;
import java.util.UUID;

@Data
@NoArgsConstructor
public class CountyDto implements Serializable {
    private UUID id;
    private String code;
    private String name;
   // @JsonSerialize(using = GeometrySerializer.class)
   // @JsonDeserialize(contentUsing = GeometryDeserializer.class)
    @JsonIgnore
    private Polygon polygon;
}
