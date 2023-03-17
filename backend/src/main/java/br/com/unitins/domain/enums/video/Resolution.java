package br.com.unitins.domain.enums.video;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum Resolution {

    HD("1280", "720"),
    SD("720", "480");

    String width;
    String height;

    @Override
    public String toString() {
        return this.width + "x" + this.height;
    }
}

