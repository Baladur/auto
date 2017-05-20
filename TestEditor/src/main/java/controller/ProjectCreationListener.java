package controller;

import model.Project;

/**
 * Created by roman on 06.05.2017.
 */

public @FunctionalInterface interface ProjectCreationListener {
    void handle(Project project);
}
