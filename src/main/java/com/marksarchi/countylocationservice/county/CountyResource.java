package com.marksarchi.countylocationservice.county;


import com.marksarchi.countylocationservice.dto.CountyDto;
import com.marksarchi.countylocationservice.dto.LocationDto;
import com.marksarchi.countylocationservice.migration.CountiesMigrationFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/county")
public class CountyResource {
    @Autowired
    CountyService countyService;

    @Autowired
    CountiesMigrationFactory factory;

//    @GetMapping
//    public ResponseEntity fetchCounties() throws IOException {
//        var features = countyService.getFeatures();
//        return ResponseEntity.ok()
//                .body(features);
//    }
//    @GetMapping
//    public ResponseEntity fetchCountiesPageble(Pageable pageable) throws IOException {
//        var features = service.fetchCounties(pageable);
//        return ResponseEntity.ok()
//                .body(features);
//    }
    @GetMapping("/counties")
    public ResponseEntity fetchCounty(Pageable pageable) throws IOException {
        var resp = countyService.fetchCounties(pageable);
        return ResponseEntity.status(resp.getCode())
                .body(resp);
    }

    @PostMapping
    public ResponseEntity createCounty(@RequestBody CountyDto dto) {
        log.info(dto.toString());
        var response = countyService.createCounty(dto);
        return ResponseEntity.ok()
                .body(response);
    }
    @PutMapping
    public ResponseEntity updateCounty(@RequestBody CountyDto dto) {
        log.info(dto.toString());
        var response = countyService.updateCounty(dto);
        return ResponseEntity.ok()
                .body(response);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity deleteCounty(@PathVariable UUID id) {

        var response = countyService.deleteCounty(id);
        return ResponseEntity.ok()
                .body(response);
    }

    @PostMapping("/check-county")
    public ResponseEntity checkLocation(@RequestBody LocationDto dto) {
        log.info(dto.toString());
        var response = countyService.checkLocation(dto);
        return ResponseEntity.ok()
                .body(response);
    }

    @PostMapping("/check-location")
    public ResponseEntity checkLocationByCounty(@RequestParam String countyCode ,@RequestBody LocationDto dto) {
        log.info(dto.toString());
        var response = countyService.checkLocationByCounty(countyCode,dto);
        return ResponseEntity.status(response.getCode())
                .body(response);

    }

    @PostMapping("/info")
    public ResponseEntity fetchLocationData(@RequestBody LocationDto dto) {
        log.info(dto.toString());
        var response = countyService.fetchLocationData(dto);
        return ResponseEntity.status(response.getCode())
                .body(response);

    }
    @GetMapping("/migrate")
    public ResponseEntity migrateCounties() throws IOException {
        var territories = factory.buildPolygons();
        var resp = countyService.saveAll(territories);
        return ResponseEntity.ok()
                .body(resp);
    }
}
