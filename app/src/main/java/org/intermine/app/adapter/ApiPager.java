package org.intermine.app.adapter;

import java.io.Serializable;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class ApiPager implements Iterator<ApiPager>, Serializable {
    private static final long serialVersionUID = 1L;

    private final int mTotal;
    private final int mCurrentPage;
    private final int mPerPage;

    public ApiPager(int total, int currentPage, int perPage) {
        mTotal = total;
        mCurrentPage = currentPage;
        mPerPage = perPage;
    }

    public int getPerPage() {
        return mPerPage;
    }

    public int getTotal() {
        return mTotal;
    }

    public int getCurrentPage() {
        return mCurrentPage;
    }

    public boolean hasMorePages() {
        return (mCurrentPage + 1) * mPerPage < mTotal;
    }

    @Override
    public boolean hasNext() {
        return hasMorePages();
    }

    @Override
    public ApiPager next() {
        if (hasNext())
            return new ApiPager(getTotal(), getCurrentPage() + 1, getPerPage());

        throw new NoSuchElementException("No more pages.");
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

    public final static ApiPager NULL_PAGER = new ApiPager(-1, -1, -1);
}
