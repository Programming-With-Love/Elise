package com.hnqc.ironhand.common;

public class CommonConfig {
    private String broadId;
    private String messageService;
    private Long workerId;
    private Long dataCenterId;
    private String rootFilePath;

    public String getBroadId() {
        return broadId;
    }

    public void setBroadId(String broadId) {
        this.broadId = broadId;
    }

    public String getMessageService() {
        return messageService;
    }

    public void setMessageService(String messageService) {
        this.messageService = messageService;
    }

    public Long getWorkerId() {
        return workerId;
    }

    public void setWorkerId(Long workerId) {
        this.workerId = workerId;
    }

    public Long getDataCenterId() {
        return dataCenterId;
    }

    public void setDataCenterId(Long dataCenterId) {
        this.dataCenterId = dataCenterId;
    }

    public String getRootFilePath() {
        return rootFilePath;
    }

    public void setRootFilePath(String rootFilePath) {
        this.rootFilePath = rootFilePath.replaceAll("(^/)(/$)", "");
    }
}
