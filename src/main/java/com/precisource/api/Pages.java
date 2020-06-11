package com.precisource.api;

public class Pages {

    private static final int MAX_OFFSET = 1000000;
    private static final int MAX_LIMIT = 50;
    private static final int DEFAULT_LIMIT = 10;

    public static int safeLimit(int limit) {
        limit = limit <= 0 ? DEFAULT_LIMIT : limit;
        return limit > MAX_LIMIT ? MAX_LIMIT : limit;
    }

    public static int safeOffset(int offset) {
        offset = offset < 0 ? 0 : offset;
        return offset > MAX_OFFSET ? MAX_OFFSET : offset;
    }

    public static Integer validateLimit(Integer limit) {
        if (limit == null || limit <= 0) {
            limit = DEFAULT_LIMIT;
        }
        return limit > MAX_LIMIT ? MAX_LIMIT : limit;
    }

    public static Integer validateOffset(Integer offset) {
        if (offset == null || offset < 0) {
            offset = 0;
        }
        return offset > MAX_OFFSET ? MAX_OFFSET : offset;
    }
}
