package br.com.unitins.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum Resolution {

    HIGH("1920", "1080"),
    MEDIUM("1280", "720"),
    LOW("854", "480");

    String width;
    String height;
}

