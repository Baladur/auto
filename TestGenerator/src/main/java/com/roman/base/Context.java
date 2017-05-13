package com.roman.base;

import com.roman.model.Element;
import com.roman.model.Elements;
import com.roman.model.Type;
import com.roman.model.Variable;
import com.roman.util.ProcessException;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

/**
 * Created by roman on 26.03.2017.
 */
@Getter
public class Context {
    @Setter private String testName;
    private int lineCount = 0;
    private int startColumn = 0;
    private int endColumn = 0;
    private List<Type> types = new ArrayList<>();
    private List<Variable> variables = new ArrayList<Variable>();
    @Setter private boolean inParameterDeclaration = false;
    private Deque<InterpretationError> errors = new ArrayDeque<>();
    @Setter private Elements elements;

    public void changeColumnCount(int newColumnCount) {
        startColumn = endColumn + 1;
        endColumn = newColumnCount;
    }

    public void skipColumns(int columnCount) {
        startColumn += columnCount;
    }

    /**
     * This method moves line cursor.
     */
    public void newLine() {
        startColumn = 0;
        endColumn = 0;
        lineCount++;
    }

    /**
     * This method saves interpretation error.
     */
    public void addError(String errorMessage) {
        errors.add(new InterpretationError(errorMessage, lineCount, startColumn));
    }

    /**
     * This method saves interpretation error with column shift relative to current reader pointer.
     */
    public void addError(String errorMessage, int columnShift) {
        errors.add(new InterpretationError(errorMessage, lineCount, startColumn + columnShift));
    }

    public void addTypes(Type[] types) {
        Collections.addAll(this.types, types);
    }

    /**
     * Get type by script name.
     *
     * @param typeName
     * @return
     */
    public Type getType(String typeName) throws ProcessException {
        return types.stream()
                .filter(t -> t.scriptName.equals(typeName))
                .findFirst()
                .orElseThrow(() -> new ProcessException(String.format("Type '%s' is undefined.", typeName)));
    }

    /**
     * Get type of UI element by script name.
     *
     * @param elementName
     * @return type of element
     */
    public String getElementType(String elementName) {
        //TODO: rewrite this!
        if (elements.Button.contains(elementName)) {
            return "Button";
        }
        if (elements.TextField.contains(elementName)) {
            return "TextField";
        }
        if (elements.Select.contains(elementName)) {
            return "Select";
        }
        if (elements.Button.contains(elementName)) {
            return "Table";
        }
        return null;
    }

    /**
     * This method checks whether variable had been declared earlier and if so types compatibility.
     */
    public boolean addVariable(Variable variable) throws ProcessException {
        if (variables.contains(variable)) {
            Variable initialVariable = variables.get(variables.indexOf(variable));
            Type initialType = initialVariable.getType();
            if (!variable.getType().equals(initialType)) {
                addError(String.format("Variable '%s' of type '%s' is assigned with value of type '%s'.",
                        variable.getName(), initialType.scriptName, variable.getType().scriptName));
                return false;
            }
            initialVariable.setConcreteValue(variable.getConcreteValue());
        } else {
            variables.add(variable);
        }
        return true;
    }

    public Variable getVariableByName(String name) throws ProcessException {
        return variables.stream()
                .filter(var -> var.getName().equals(name))
                .findFirst()
                .orElseThrow(() -> new ProcessException(String.format("Variable '%s' is undefined.")));
    }

    public boolean isVariableDeclared(String variableName) {
        return variables.stream()
                .filter(var -> var.getName().equals(variableName))
                .findAny()
                .isPresent();
    }

    public String toString() {
        StringBuilder variablesInfo = new StringBuilder();
        for (Variable var : variables) {
            variablesInfo.append("\t").append(var.toString()).append("\n");
        }
        return String.format("CONTEXT:\ntypes : %s\nvariables:\n\t%s",
                types.toString(), variablesInfo.toString());
    }
}
