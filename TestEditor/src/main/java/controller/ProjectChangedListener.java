package controller;

import model.Project;

/**
 * Created by roman on 07.05.2017.
 */
public @FunctionalInterface interface ProjectChangedListener {
    void handle(Project project);
}
