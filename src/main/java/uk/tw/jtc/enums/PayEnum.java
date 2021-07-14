package uk.tw.jtc.enums;

public enum PayEnum {
    ACTIVE("active"),
    PAID("paid");
    private String status;

    PayEnum(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
