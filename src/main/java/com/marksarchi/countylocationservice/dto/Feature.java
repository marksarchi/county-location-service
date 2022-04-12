package com.marksarchi.countylocationservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.marksarchi.countylocationservice.geom.Properties;
import lombok.*;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Polygon;
import org.n52.jackson.datatype.jts.GeometryDeserializer;
import org.n52.jackson.datatype.jts.GeometrySerializer;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
//@TypeDefs({ @TypeDef(name = "json", typeClass = JsonType.class) })
public class Feature implements Serializable {

    public String type;
    @JsonSerialize(using = GeometrySerializer.class,as = Polygon.class)
    @JsonDeserialize(contentUsing = GeometryDeserializer.class,as = Polygon.class)
    @JsonProperty("geometry")
   // @JsonIgnore
    public Geometry geometry;
    public Properties properties;




    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }


}
