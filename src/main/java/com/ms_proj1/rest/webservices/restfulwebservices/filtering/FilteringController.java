package com.ms_proj1.rest.webservices.restfulwebservices.filtering;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
public class FilteringController {

    @GetMapping("/filtering")
    public SomeBean filtering() {
        return new SomeBean("value1","value2","value3");
    }

//    here too because field2 has been restricted so it will not be returned for any element in the list
    @GetMapping("/filtering-list")
    public List<SomeBean> filteringList() {
        return Arrays.asList(new SomeBean("value1","value2","value3"),
                new SomeBean("value4","value5","value6"));
    }
    //    above 2 functions are examples of static filtering
//    here the filter applied is applicable on both the functions

//    we are doing this for dynamic filtering
//    in dynamic filtering we can restrict filter for one specific API and other API would have a different filter
//    this is why it has to be done at API level and not the actual class
//    for this we take help of MappingJacksonValue class to add filters accordingly
    @GetMapping("/filtering-dynamic")
    public MappingJacksonValue filteringDynamic() {
        SomeBean someBean = new SomeBean("value1","value2","value3");
        MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(someBean);

//        this is creating a filter
        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("filed1","filed2");
//        here we use to add the filter to filterprovider class
        FilterProvider filterProvider = new SimpleFilterProvider().addFilter("SomeBeanFilter",filter);
//        at this point we are actually adding filters
        mappingJacksonValue.setFilters(filterProvider);
        return mappingJacksonValue;
    }

    @GetMapping("/filtering-dynamic-list")
    public MappingJacksonValue filteringDynamicList() {

        List<SomeBean> someBeanList = Arrays.asList(new SomeBean("value1","value2","value3"),
                new SomeBean("value4","value5","value6"));
        MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(someBeanList);

        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("filed2","filed3");
        FilterProvider filterProvider = new SimpleFilterProvider().addFilter("SomeBeanFilter",filter);
        mappingJacksonValue.setFilters(filterProvider);
        return mappingJacksonValue;
    }



}
