package com.ccnode.codegenerator.nextgenerationparser.parsedresult.update;

import java.util.List;

/**
 * Created by bruce.ge on 2016/12/12.
 */
public class ParsedUpdateDto {
    private List<ParsedUpdate> updateList;

    private List<ParsedUpdateError> errorList;

    public List<ParsedUpdate> getUpdateList() {
        return updateList;
    }

    public void setUpdateList(List<ParsedUpdate> updateList) {
        this.updateList = updateList;
    }

    public List<ParsedUpdateError> getErrorList() {
        return errorList;
    }

    public void setErrorList(List<ParsedUpdateError> errorList) {
        this.errorList = errorList;
    }
}
