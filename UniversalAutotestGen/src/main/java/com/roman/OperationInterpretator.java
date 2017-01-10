package com.roman;

/**
 * Created by roman on 27.11.2016.
 */
public class OperationInterpretator extends BaseInterpretator {
    protected OperationInterpretator(BaseInterpretator baseInterpretator) {
        this.baseInterpretator = baseInterpretator;
    }

    public void interpret(String line) {
        interpret();
    }

    private void interpret() {
        String line = baseInterpretator.getCurrentLine();
        Step currentStep = baseInterpretator.getLastStep();
        if (line.startsWith("go")) {
            String url = line.substring(3, line.length());
            currentStep.addOperation(new GoOperation(url));

        } else if (line.startsWith("set")) {
            String args = line.substring(4, line.length());
            boolean saveElementName = false;
            boolean saveText = false;
            String elementName = "", text = "";
            for (int i = 0; i < args.length(); i++) {
                if (args.charAt(i) == '#') {
                    saveElementName = true;
                    continue;
                }
                if (args.charAt(i) == '"') {
                    if (saveText) {
                        saveText = false;
                        continue;
                    } else {
                        saveText = true;
                        continue;
                    }
                }
                if (saveElementName) {
                    if (args.charAt(i) == ' ') {
                        saveElementName = false;
                        continue;
                    } else {
                        elementName += args.charAt(i);
                    }
                }
                if (saveText) {
                    text += args.charAt(i);
                }
            }
            baseInterpretator.getLastStep().addOperation(new SetOperation("TextField", elementName, text));
        } else if (line.startsWith("click")) {
            if (line.startsWith("click link")) {
                String elementName = line.substring(11, line.length());
                if (elementName.startsWith("#")) {
                    elementName = elementName.substring(1, elementName.length());
                }
                baseInterpretator.getLastStep().addOperation(new ClickOperation("Link", elementName));
            } else {
                String elementName = line.substring(6, line.length());
                if (elementName.startsWith("#")) {
                    elementName = elementName.substring(1, elementName.length());
                }
                baseInterpretator.getLastStep().addOperation(new ClickOperation("Button", elementName));
            }

        } else if (line.startsWith("wait")) {
            String[] command = line.split(" ");
            Time time = null;
            if (command[2].equals("h")) {
                time = Time.HOURS;
            } else if (command[2].equals("min")) {
                time = Time.MINUTES;
            } else if (command[2].equals("sec")) {
                time = Time.SECONDS;
            }
            baseInterpretator.getLastStep().addOperation(new WaitOperation(time, command[1]));
        }
    }
}
