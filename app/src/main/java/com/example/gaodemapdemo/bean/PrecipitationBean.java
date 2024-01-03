package com.example.gaodemapdemo.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PrecipitationBean {

    @JsonProperty("code")
    private String code;
    @JsonProperty("updateTime")
    private String updateTime;
    @JsonProperty("fxLink")
    private String fxLink;
    @JsonProperty("summary")
    private String summary;
    @JsonProperty("refer")
    private ReferDTO refer;
    @JsonProperty("minutely")
    private List<MinutelyDTO> minutely;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getFxLink() {
        return fxLink;
    }

    public void setFxLink(String fxLink) {
        this.fxLink = fxLink;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public ReferDTO getRefer() {
        return refer;
    }

    public void setRefer(ReferDTO refer) {
        this.refer = refer;
    }

    public List<MinutelyDTO> getMinutely() {
        return minutely;
    }

    public void setMinutely(List<MinutelyDTO> minutely) {
        this.minutely = minutely;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ReferDTO {
        @JsonProperty("sources")
        private List<String> sources;
        @JsonProperty("license")
        private List<String> license;

        public List<String> getSources() {
            return sources;
        }

        public void setSources(List<String> sources) {
            this.sources = sources;
        }

        public List<String> getLicense() {
            return license;
        }

        public void setLicense(List<String> license) {
            this.license = license;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class MinutelyDTO {
        @JsonProperty("fxTime")
        private String fxTime;
        @JsonProperty("precip")
        private String precip;
        @JsonProperty("type")
        private String type;

        public String getFxTime() {
            return fxTime;
        }

        public void setFxTime(String fxTime) {
            this.fxTime = fxTime;
        }

        public String getPrecip() {
            return precip;
        }

        public void setPrecip(String precip) {
            this.precip = precip;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}
