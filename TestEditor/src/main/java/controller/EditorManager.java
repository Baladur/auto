package controller;

import context.*;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import model.*;
import util.*;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * All business logic goes here.
 * There must be one instance per main window.
 */
@Slf4j
public class EditorManager {
    private DataManager dataManager;
    private Project project;
    @Setter private ProjectChangedListener projectChangedListener;
    private SyntaxAnalyzer currentAnalyzer;
    private Map<String, SyntaxAnalyzer> analyzers = new HashMap<>();
    private List<Type> types;

    public EditorManager() throws DataException {
        try {
            this.dataManager = new DBDataManager();
            this.types = new ArrayList<>(Arrays.asList(JsonSerializer.deserialize(Paths.get(Resources.STANDARD_BINDING), Type[].class)));
//            this.currentAnalyzer = new SyntaxAnalyzer(Resources.GRAMMAR, Resources.START_NONTERMINAL, types.toArray(new Type[0]));
//            currentAnalyzer.initStack();
        } catch (IOException e) {
            throw new DataException(Messages.INTERNAL_ERROR, e);
        }
    }

    public void setCurrentProject(Project project) {
        this.project = project;
        if (projectChangedListener != null) {
            projectChangedListener.handle(project);
        }
    }

    /**
     * This method creates new project according to given POJO.
     * Actions: create project file in the root path.
     *
     * @param project POJO with project information
     */
    public void initProject(Project project) throws DataException {
        dataManager.saveProjectFile(project);
        setCurrentProject(project);
    }

    /**
     * This method opens project file and sets it as current project.
     *
     * @param projectFilePath path to project file
     * @throws DataException
     */
    public void openProject(Path projectFilePath) throws DataException {
        setCurrentProject(dataManager.readProjectFile(projectFilePath));
    }

    public void analyzeContext(Hint selectedHint) throws SyntaxAnalyzeException {
        currentAnalyzer.processInput(selectedHint);
    }

    public Set<Hint> getCurrentHints() {
        return currentAnalyzer.getCurrentHints();
    }

    public void addInputEventListener(InputEventListener listener) {
        currentAnalyzer.addInputEventListener(listener);
    }

    public void addContextEndEventListener(ContextEndEventListener listener) {
        currentAnalyzer.addContextEndEventListener(listener);
    }

    public void addMovePositionEventListener(MovePositionEventListener listener) {
        currentAnalyzer.addMovePositionEventListener(listener);
    }

    public void appendInput(String appendix) {
        if (appendix != null) {
            currentAnalyzer.getContextState().appendInput(appendix);
        }
    }

    public String getInput() {
        return currentAnalyzer.getContextState().getCurrentInput();
    }

    public void deleteLastChar() {
        currentAnalyzer.getContextState().deleteLastChar();
        System.out.println("Deleted last char.");
    }

    public void openNewTest(String testName) throws DataException {
        try {
            analyzers.put(testName, new SyntaxAnalyzer(Resources.GRAMMAR, Resources.START_NONTERMINAL, types));
            switchTest(testName);
        } catch (IOException e) {
            throw new DataException(Messages.INTERNAL_ERROR);
        }
    }

    public void switchTest(String testName) {
        currentAnalyzer = analyzers.get(testName);
        currentAnalyzer.moveToCurrentPosition();
        System.out.println(currentAnalyzer.toString());
        System.out.println(testName);
    }

    public void setPosition(int line, int column) {
        currentAnalyzer.getContextState().setCurrentPosition(new CodePosition(line + 1, column + 1));
    }
}
