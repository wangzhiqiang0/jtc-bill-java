package uk.tw.jtc.enums;

import lombok.Getter;

@Getter
public enum UsageTypeEnum {
    PHONE("phone"),
    SMS("sms");

    UsageTypeEnum(String type) {
        this.type = type;
    }

    private String type;
}
