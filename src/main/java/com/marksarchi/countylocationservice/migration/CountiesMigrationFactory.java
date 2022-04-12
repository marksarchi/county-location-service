package com.marksarchi.countylocationservice.migration;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.marksarchi.countylocationservice.dto.CountyDto;
import com.marksarchi.countylocationservice.geom.Feature1;
import com.marksarchi.countylocationservice.geom.Root;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@Slf4j
public class CountiesMigrationFactory {

    public Polygon getTerritoryPolygon(String value) {
        WKTReader reader = new WKTReader();
        Geometry geom = null;
        String TERRITORY_BOUNDARY =
                "POLYGON ((36.68704392 -1.26708969, 36.65133836 -1.2445385, 36.65274171 -1.22187153, 36.66513127 -1.19920454, 36.69477327 -1.13204332, 36.75325358 -1.1486352, 36.87079106 -1.14536796, 36.91539877 -1.2046609, 36.90988168 -1.27596657, 36.84408601 -1.33182182, 36.78698364 -1.36275216, 36.71683555 -1.37171521, 36.68230581 -1.34972309, 36.66927697 -1.33204681, 36.65717484 -1.29283247, 36.64627435 -1.27352542, 36.64121033 -1.25662089, 36.68704392 -1.26708969, 36.68704392 -1.26708969, 36.68704392 -1.26708969, 36.68704392 -1.26708969, 36.68704392 -1.26708969, 36.68704392 -1.26708969, 36.68704392 -1.26708969, 36.68704392 -1.26708969, 36.68704392 -1.26708969, 36.68704392 -1.26708969, 36.68704392 -1.26708969, 36.68704392 -1.26708969, 36.68704392 -1.26708969, 36.68704392 -1.26708969, 36.68704392 -1.26708969, 36.68704392 -1.26708969))";

        try {
            geom = reader.read(value);
        } catch (ParseException e) {
            e.printStackTrace();
            System.out.printf("Could not pass the point value provided for location %s : ", TERRITORY_BOUNDARY);
            return null;
        }
        return (Polygon) geom;
    }

    public List<CountyDto> buildPolygons() throws IOException {
        var polygonStrings = constructPolygonStringActual();
        List<CountyDto> counties = new ArrayList<>();
        for (CountyWrapper s : polygonStrings) {
            Polygon polygon = getTerritoryPolygon(s.getPolygon());
            CountyDto dto = new CountyDto();
            dto.setPolygon(polygon);
            dto.setId(UUID.randomUUID());
            dto.setName(s.getCountName());
            dto.setCode(RandomStringUtils.randomAlphanumeric(4).toUpperCase());
            counties.add(dto);
        }
        return counties;
    }

    public List<CountyWrapper> constructPolygonString() throws IOException {
        ObjectMapper om = new ObjectMapper();
        Root root = om.readValue(new File("kenyan-counties.zip.json"), Root.class);
        ArrayList<Feature1> features = root.getFeatures();


        ArrayList<Object> rs = features.get(0).getGeometry().getCoordinates().get(0).get(32);

        ArrayList<ArrayList<Object>> coord1 = features.get(0).getGeometry().getCoordinates().get(0);
        ArrayList<String> finalList = new ArrayList<>();
        for (ArrayList<Object> list : coord1) {
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i <= list.size() - 1; i++) {
                if (i == 1) {
                    stringBuilder.append(" ").append(list.get(i));
                    break;

                } else {
                    stringBuilder.append(list.get(i));
                }

            }
            finalList.add(stringBuilder.toString());

        }
        String output = String.format("POLYGON((%s))", finalList).replaceAll("\\[|]", "");
        System.out.println(output);
        CountyWrapper wrapper = new CountyWrapper();
        wrapper.setPolygon(output);
        wrapper.setCountName(features.get(0).getProperties().getcOUNTY());
        return Arrays.asList(wrapper);
    }

    public List<CountyWrapper> constructPolygonStringActual() throws IOException {
        ObjectMapper om = new ObjectMapper();
        Root root = om.readValue(new File("kenyan-counties.zip.json"), Root.class);
        ArrayList<Feature1> features = root.getFeatures();
        ArrayList<ArrayList<Object>> coord1 = features.get(0).getGeometry().getCoordinates().get(0);
        ArrayList<String> polygonStrings = new ArrayList<>();
        ArrayList<CountyWrapper> territoryWrappers = new ArrayList<>();

        //  ArrayList<String> finalList = new ArrayList<>();
        // String output = "";
        for(int f=40;f<47;f++) {
            ArrayList<String> finalList = new ArrayList<>();
            String output = "";
            for (ArrayList<Object> list : features.get(f).getGeometry().getCoordinates().get(0)) {
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i <= list.size() - 1; i++) {
                    if (i == 1) {
                        stringBuilder.append(" ").append(list.get(i));
                        break;

                    } else {
                        stringBuilder.append(list.get(i));
                    }

                }
                finalList.add(stringBuilder.toString());

            }
            output = String.format("POLYGON((%s))", finalList).replaceAll("\\[|]", "");
            // System.out.println(output);
            CountyWrapper wrapper = new CountyWrapper();
            wrapper.setPolygon(output);
            wrapper.setCountName(features.get(f).getProperties().getcOUNTY());
            territoryWrappers.add(wrapper);

            polygonStrings.add(output);
            System.out.println("Done for"+features.get(f).getProperties().getcOUNTY());
        }
        return territoryWrappers;
    }
}
