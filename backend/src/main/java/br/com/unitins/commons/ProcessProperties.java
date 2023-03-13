package br.com.unitins.commons;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProcessProperties {

    private Long videoId;
    private MultipartBody multipartBody;
}
