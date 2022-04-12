package com.marksarchi.countylocationservice.county;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.marksarchi.countylocationservice.Domain.CountyEntity;
import com.marksarchi.countylocationservice.dto.CountyDto;
import com.marksarchi.countylocationservice.dto.Feature;
import com.marksarchi.countylocationservice.dto.LocationDto;
import com.marksarchi.countylocationservice.dto.ResponseWrapper;
import com.marksarchi.countylocationservice.interfaces.CountyDtoConvertor;
import com.marksarchi.countylocationservice.interfaces.CountyInfoFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class CountyService {
    ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    @Autowired
    CountyRepository repository;
    @Autowired
    CountyInfoFeignClient client;

    public ResponseWrapper fetchCounties(Pageable pageable) {
        List<CountyDto> response = CountyDtoConvertor.INSTANCE.entityToDto(repository.findAll(pageable).getContent());
        return ResponseWrapper.builder()
                .code(HttpStatus.OK.value())
                .message("Fetched successfully")
                .data(response)
                .build();
    }

    public CountyEntity createCounty(CountyDto dto) {
        System.out.println((dto.toString()));
        UUID id = UUID.randomUUID();
        CountyEntity entity = new CountyEntity();
        entity.setId(id);
        entity.setName(dto.getName());
        entity.setPolygon(dto.getPolygon());
        entity.setCode(dto.getCode());

        return repository.save(entity);
    }

    public CountyEntity updateCounty(CountyDto dto) {
        var county = repository.findById(dto.getId());
        CountyEntity updatedCounty = new CountyEntity();
        if (county.isPresent()) {
            updatedCounty = county.get();
            updatedCounty.setName(dto.getName());
            updatedCounty.setPolygon(dto.getPolygon());
            repository.save(updatedCounty);
        }
        return updatedCounty;
    }

    public CountyEntity getCounty(String countyCode) {
        Optional<CountyEntity> exists = repository.findCountyEntityByCode(countyCode);

        return exists.get();

    }

    public ResponseWrapper checkLocation(LocationDto dto) {
        return findLocation(dto);

    }

    public ResponseWrapper findLocation(LocationDto locationDto) {
        List<CountyEntity> counties = repository.findAll();
        String countyName = null;
        Point point = getLocationPoint(locationDto.getLongitude(), locationDto.getLatitude());
        log.info("Point: {}", point);
        for (CountyEntity county : counties) {
            log.info("Checked for :".concat(county.getName()));

            if (county.getPolygon().contains(point)) {
                System.out.println("Found county: " + county.getName());
                countyName = county.getName();
                break;
            }
        }
        if (countyName != null) {
            return ResponseWrapper.builder()
                    .code(HttpStatus.OK.value())
                    .message("Location found successfully")
                    .data(countyName)
                    .build();
        }
        return ResponseWrapper.builder()
                .code(HttpStatus.OK.value())
                .message("Location not  found any county")
                .data(countyName)
                .build();
    }

    public ResponseWrapper fetchLocationData(LocationDto dto) {

        var response = findLocation(dto);
        if (response.getCode() != HttpStatus.OK.value()) return response;
        var countyName = (String) response.getData();
        var countyInformation = fetchCountyInformation(countyName);
        if(countyInformation.getCode()!= HttpStatus.OK.value()){
            return ResponseWrapper.builder()
                    .code(countyInformation.getCode())
                    .message(countyInformation.getMessage())
                    .data(countyName)
                    .build();
        }

        return countyInformation;


    }

    private ResponseWrapper fetchCountyInformation(String countyName) {
        return client.fetchCountyInfo(countyName);

    }

    public String LocationStream(List<CountyEntity> counties, LocationDto locationDto) {
        String countyName = null;
        var result = counties.stream()
                .filter(county -> county.getPolygon().contains(locationDto.getPoint()))
                .collect(Collectors.toList());
        if (result.isEmpty()) return "Location not found";

        return result.get(0).getName();
    }

    public String Findlocation(List<CountyEntity> counties, LocationDto locationDto) {
        String countyName = null;
        for (CountyEntity entity : counties) {
            if (entity.getPolygon().contains(locationDto.getPoint())) {
                countyName = entity.getName();
                break;
            }
        }
        return countyName;
    }

    public String LocationA(CountyEntity county, LocationDto locationDto) {


        if (county.getPolygon().contains(locationDto.getPoint())) {
            return county.getName();
        }

        return "Location not found";
    }

    public ResponseWrapper checkLocationByCounty(String countyCode, LocationDto locationDto) {
        Optional<CountyEntity> response = repository.findCountyEntityByCode(countyCode);
        if (response.isEmpty())
            return ResponseWrapper.builder()
                    .code(HttpStatus.NOT_FOUND.value())
                    .message("County not found")
                    .build();

        CountyEntity county = response.get();


        if (county.getPolygon().contains(locationDto.getPoint())) {
            return ResponseWrapper.builder()
                    .code(HttpStatus.OK.value())
                    .message("County :" + county.getName())
                    .build();
        }
        return ResponseWrapper.builder()
                .code(HttpStatus.NOT_FOUND.value())
                .message("Location not found in :".concat(county.getName()))
                .build();

    }

    private Point getLocationPoint(String longitude, String latitude) {
        WKTReader reader = new WKTReader();
        Geometry geom = null;
        String pointString = "POINT (" + longitude + " " + latitude + ")";

        try {
            geom = reader.read(pointString);
        } catch (ParseException e) {
            e.printStackTrace();
            log.info("Could not pass the point value provided for location {} : , {}", longitude, latitude);
            return null;
        }
        return (Point) geom;
    }


    public String deleteCounty(UUID id) {
        var entity = repository.findById(id);
        if (entity.isPresent()) {
            repository.delete(entity.get());
            return "Delete territory " + entity.get().getName() + " successfully";
        }
        return "County not found";

    }

    String county = "POLYGON((36.88338163873064 1.2344206110142568, 36.88338163873064 1.2321206110142569,  36.90216108900864  1.2377157341222067, 36.88777614483493  1.2486994813543124, 36.88338163873064  1.2344206110142568))";

    public List<CountyEntity> saveAll(List<CountyDto> territories) {
        var enties = CountyDtoConvertor.INSTANCE.dtoToEntity(territories);
        return repository.saveAll(enties);
    }

    public List<Feature> fetchCounties() throws IOException {
        return JsonFileToDto();
    }

    public List<Feature> JsonFileToDto() throws IOException {

        File dir = new File("target");
        File[] directoryListing = dir.listFiles();
        List<Feature> counties = new ArrayList<>();
        if (directoryListing != null) {
            for (File child : directoryListing) {
                // Do something with child
                Feature county = mapper.readValue(child, Feature.class);
                counties.add(county);
            }
        } else {
            // Handle the case where dir is not really a directory.
            // Checking dir.isDirectory() above would not be sufficient
            // to avoid race conditions with another process that deletes
            // directories.
            log.info("County directory is empty");
        }
        return counties;
    }

    String payload = "{\n" +
            "    \"id\": \"a7a32680-35af-4ac7-b5ae-40f280860444\",\n" +
            "    \"code\": \"GZ3X\",\n" +
            "    \"name\": \"Kiambu\",\n" +
            "    \"polygon\": {\n" +
            "        \"type\": \"Polygon\",\n" +
            "        \"coordinates\": [\n" +
            "            [\n" +
            "                [\n" +
            "                    36.70455338,\n" +
            "                    -1.28159149\n" +
            "                ],\n" +
            "                [\n" +
            "                    36.69322373,\n" +
            "                    -1.27800435\n" +
            "                ],\n" +
            "                [\n" +
            "                    36.70930827,\n" +
            "                    -1.27529293\n" +
            "                ],\n" +
            "                [\n" +
            "                    36.71494728,\n" +
            "                    -1.28024421\n" +
            "                ],\n" +
            "                [\n" +
            "                    36.70685338,\n" +
            "                    -1.28159149\n" +
            "                ],\n" +
            "                [\n" +
            "                    36.70455338,\n" +
            "                    -1.28159149\n" +
            "                ],\n" +
            "                [\n" +
            "                    36.70455338,\n" +
            "                    -1.28159149\n" +
            "                ]\n" +
            "            ]\n" +
            "        ]\n" +
            "    }\n" +
            "}";


}
