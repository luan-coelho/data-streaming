package br.com.unitins.rest.filters;

import lombok.Getter;

import javax.ws.rs.QueryParam;

@Getter
public class VideoFilter {

    @QueryParam("title")
    private String title;
    @QueryParam("description")
    private String description;
}
