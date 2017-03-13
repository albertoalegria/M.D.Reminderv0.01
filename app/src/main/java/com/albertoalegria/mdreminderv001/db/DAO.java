package com.albertoalegria.mdreminderv001.db;

import java.util.ArrayList;

/**
 * Created by alberto on 03/03/17.
 */

public interface DAO<T> {
    void create(T t);
    T retrieve(String name);
    void update(T t);
    void delete(String name);
    ArrayList<T> retrieveAll();
}
