package com.roman.base;

import com.roman.interpretation.command.ElseInterpreter;
import com.roman.interpretation.command.InstructionInterpreter;
import com.roman.interpretation.command.OrElseInterpreter;
import com.roman.interpretation.command.TestInterpreter;
import com.roman.interpretation.expression.*;

/**
 * High-level interpreters
 */
public class Interpreters {
    public static final ArithmeticInterpreter ARITHMETIC = new ArithmeticInterpreter();
    public static final StringInterpreter STRING = new StringInterpreter();
    public static final BooleanInterpreter BOOL = new BooleanInterpreter();
    public static final FunctionInterpreter FUNCTION = new FunctionInterpreter();
    public static final AssignmentInterpreter ASSIGNMENT = new AssignmentInterpreter();
    public static final ValueInterpreter VALUE = new ValueInterpreter();
    public static final TestInterpreter TEST = new TestInterpreter();
    public static final OrElseInterpreter OR_ELSE = new OrElseInterpreter();
    public static final ElseInterpreter ELSE = new ElseInterpreter();
    public static final InstructionInterpreter INSTRUCTION = new InstructionInterpreter();
    public static final ParameterInterpreter PARAMETER = new ParameterInterpreter();
    public static final ErrorInterpreter ERROR = new ErrorInterpreter();
}
