package br.com.unitins.commons;

import javax.ws.rs.FormParam;
import java.io.File;
import java.io.InputStream;

public class MultipartBody {

    @FormParam("file")
    public File file;

    @FormParam("file")
    public InputStream inputStream;

    @FormParam("fileName")
    public String fileName;
}
