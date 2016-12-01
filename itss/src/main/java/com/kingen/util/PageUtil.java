package com.kingen.util;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * 分页工具
 *
 * @author wj
 */
public class PageUtil {

    public static int PAGE_SIZE = 25;

    public static int[] init(Page<?> page, HttpServletRequest request) {
        int pageNumber = Integer.parseInt(StringUtils.defaultIfBlank(request.getParameter("page"), "1"));
        page.setPage(pageNumber);
        int pageSize = Integer.parseInt(StringUtils.defaultIfBlank(request.getParameter("limit"), String.valueOf(PAGE_SIZE)));
        page.setLimit(pageSize);
        int firstResult = page.getFirstResult()- 1;
        int maxResults = page.getLimit();
        return new int[]{firstResult, maxResults};
    }

}
