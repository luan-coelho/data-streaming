package br.com.unitins.commons;

import jakarta.ws.rs.FormParam;

import java.io.InputStream;

public class MultipartBody {

    @FormParam("file")
    public InputStream inputStream;

    @FormParam("fileName")
    public String fileName;
}
