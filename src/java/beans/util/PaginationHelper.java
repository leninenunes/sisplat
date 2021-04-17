package beans.util;

import javax.faces.model.DataModel;

public abstract class PaginationHelper {

    private int pageSize;
    private int page;

    public PaginationHelper(int pageSize) {
        this.pageSize = pageSize;
    }

    public abstract int getItemsCount();

    public abstract DataModel createPageDataModel();

    public int getPageFirstItem() {
        return page * pageSize;
    }

    public int getPage() {
        return page;
    }

    public int getTotalPage() {
        return ((getItemsCount() + (pageSize - 1)) / pageSize) - 1;
    }

    public int getPageLastItem() {
        int i = getPageFirstItem() + pageSize - 1;
        int count = getItemsCount() - 1;
        if (i > count) {
            i = count;
        }
        if (i < 0) {
            i = 0;
        }
        return i;
    }

    public void firstPage() {
        page = 0;
    }

    public void lastPage() {
        page = ((getItemsCount() + (pageSize - 1)) / pageSize) - 1;
    }

    public void primaryPage() {
        page = page;
    }

    public void secondPage() {
        page = page + 1;
    }

    public void thirdPage() {
        page = page + 2;
    }

    public boolean isHasPrimaryPage() {
        return page + 1 <= (getItemsCount() + (pageSize - 1)) / pageSize;
    }

    public boolean isHasSecondPage() {
        return page + 2 <= (getItemsCount() + (pageSize - 1)) / pageSize;
    }

    public boolean isHasThirdPage() {
        return page + 3 <= (getItemsCount() + (pageSize - 1)) / pageSize;
    }

    public boolean isHasNextPage() {
        return (page + 1) * pageSize + 1 <= getItemsCount();
    }

    public void nextPage() {
        if (isHasNextPage()) {
            page++;
        }
    }

    public boolean isHasPreviousPage() {
        return page > 0;
    }

    public void previousPage() {
        if (isHasPreviousPage()) {
            page--;
        }
    }

    public int getPageSize() {
        return pageSize;
    }

    public String getDisabledPreviousPage() {
        if (isHasPreviousPage()) {
            return "";
        }
        return "disabled";
    }

    public String getDisabledNextPage() {
        if (isHasNextPage()) {
            return "";
        }
        return "disabled";
    }

    public String getActivePrimaryPage() {
        if (page + 1 == page + 1) {
            return "active";
        }
        return "";
    }

    public String getActiveSecondPage() {
        if (page + 1 == page + 2) {
            return "active";
        }
        return "";
    }

    public String getActiveThirdPage() {
        if (page + 1 == page + 3) {
            return "active";
        }
        return "";
    }
}
