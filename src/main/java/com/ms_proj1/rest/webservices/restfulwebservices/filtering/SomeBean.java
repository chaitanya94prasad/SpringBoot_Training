package com.ms_proj1.rest.webservices.restfulwebservices.filtering;

import com.fasterxml.jackson.annotation.JsonFilter;

//we can also ignore fields at class level
//instead of using JSONIgnore in specific fields
//@JsonIgnoreProperties({"filed1"})
//for dynamic filtering we have added @JsonFilter("SomeBeanFilter")
//which is actually the filter name we will be using insoide our API
//this API is present in FilteringController
//filteringDynamic -> check this for the implementation
@JsonFilter("SomeBeanFilter")
public class SomeBean {
    private String filed1;

//    to filter a particular field consdier field2 was apssword and we don't want it to go in the the response
//    we can add JsonIgnore and this will not be returned hence we filter it out
//    @JsonIgnore
    private String filed2;
    private String filed3;

    public SomeBean(String filed1, String filed2, String filed3) {
        this.filed1 = filed1;
        this.filed2 = filed2;
        this.filed3 = filed3;
    }

    public String getFiled1() {
        return filed1;
    }

    public void setFiled1(String filed1) {
        this.filed1 = filed1;
    }

    public String getFiled2() {
        return filed2;
    }

    public void setFiled2(String filed2) {
        this.filed2 = filed2;
    }

    public String getFiled3() {
        return filed3;
    }

    public void setFiled3(String filed3) {
        this.filed3 = filed3;
    }

    @Override
    public String toString() {
        return "SomeBean{" +
                "filed1='" + filed1 + '\'' +
                ", filed2='" + filed2 + '\'' +
                ", filed3='" + filed3 + '\'' +
                '}';
    }
}
