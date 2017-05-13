package com.roman.util;

/**
 * Created by roman on 23.04.2017.
 */
public interface InputTestReaderListener {
    public void onNewLine();
    public void onColumnCountChanged(int newColumnCount);
    public void onError(String errorMessage);
}
