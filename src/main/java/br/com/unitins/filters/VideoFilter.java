package br.com.unitins.filters;

import lombok.Getter;

import jakarta.ws.rs.QueryParam;

@Getter
public class VideoFilter {

    @QueryParam("title")
    private String title;
    @QueryParam("description")
    private String description;
}
