package ru.itmo.data.validators;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

public class PaginatorValidator implements Validator<HttpServletRequest>{
    @Override
    public List<String> validate(HttpServletRequest request) {
        List<String> errorList = new ArrayList<>();

        String sPageId = request.getParameter("page_id");
        String sPageSize = request.getParameter("page_size");

        if (sPageId != null){
            Integer pageId = null;
            try{
                pageId = Integer.parseInt(sPageId);
            }catch (NumberFormatException e){
                errorList.add("page id should be positive not zero number");
            }
            if (pageId != null && pageId <= 0){
                errorList.add("page id should be positive not zero number");
            }
        }

        if (sPageSize != null){
            Integer pageSize = null;
            try{
                pageSize = Integer.parseInt(sPageSize);
            }catch (NumberFormatException e){
                errorList.add("page size should be positive not zero number");
            }
            if (pageSize != null && pageSize <= 0){
                errorList.add("page size should be positive not zero number");
            }
        }


        if(sPageId == null && sPageSize != null || sPageId != null && sPageSize == null)
            errorList.add("page id and page size should be specified or omitted together");

        return errorList;
    }
}
