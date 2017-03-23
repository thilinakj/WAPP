package com.thilinas.twallpapers.customs;

/**
 * Created by Thilina on 17-Mar-17.
 */
public class TestS {
    private static TestS ourInstance = new TestS();

    public static TestS getInstance() {
        return ourInstance;
    }

    private TestS() {
    }
}
