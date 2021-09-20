package pagination;

import jakarta.servlet.http.HttpServletRequest;
import org.hibernate.query.Query;

public class Paginator {
    private Integer pageId;
    private Integer pageSize;

    public Paginator(HttpServletRequest request){
        String sPageId = request.getParameter("page_id");
        String sPageSize = request.getParameter("page_size");

        if (sPageId != null)
            pageId = Integer.parseInt(sPageId);

        if (sPageSize != null)
            pageSize = Integer.parseInt(sPageSize);


        if (pageId != null && pageId > 0)
            pageId--;
        else
            pageId = null;

        if (pageSize == null || pageSize <= 0)
            pageSize = null;
    }

    public <T> void preparePaginator(Query<T> query){
        if (pageId != null && pageSize != null) {
            query.setFirstResult(pageId * pageSize);
            query.setMaxResults(pageSize);
        }
    }
}
