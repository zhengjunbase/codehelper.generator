package com.ccnode.codegenerator.nextgenerationparser.buidler;

/**
 * Created by bruce.ge on 2016/12/12.
 */
public class ParamInfo {
    private String paramType;

    private String paramAnno;

    private String paramValue;

    public String getParamType() {
        return paramType;
    }

    public void setParamType(String paramType) {
        this.paramType = paramType;
    }

    public String getParamAnno() {
        return paramAnno;
    }

    public void setParamAnno(String paramAnno) {
        this.paramAnno = paramAnno;
    }

    public String getParamValue() {
        return paramValue;
    }

    public void setParamValue(String paramValue) {
        this.paramValue = paramValue;
    }

    public static final class ParamInfoBuilder {
        private String paramType;
        private String paramAnno;
        private String paramValue;

        private ParamInfoBuilder() {
        }

        public static ParamInfoBuilder aParamInfo() {
            return new ParamInfoBuilder();
        }

        public ParamInfoBuilder withParamType(String paramType) {
            this.paramType = paramType;
            return this;
        }

        public ParamInfoBuilder withParamAnno(String paramAnno) {
            this.paramAnno = paramAnno;
            return this;
        }

        public ParamInfoBuilder withParamValue(String paramValue) {
            this.paramValue = paramValue;
            return this;
        }

        public ParamInfo build() {
            ParamInfo paramInfo = new ParamInfo();
            paramInfo.setParamType(paramType);
            paramInfo.setParamAnno(paramAnno);
            paramInfo.setParamValue(paramValue);
            return paramInfo;
        }
    }
}
