import ResourceManager from './resourcemanager'
import Context from './context'
import CodeLogic from '../containers/code.container'

class StateManager {
    constructor() {
        this.projects = new Map();
    }

    putState({ projectName, name }, state) {
        console.log(`putState with projectName [${projectName}], name [${name}]`);
        let project = this.projects.get(projectName);
        if (project == undefined) {
            project = new Map();
        }
        project.set(name, state);
        this.projects.set(projectName, project);
    }

    getState({ projectName, name }) {
        let project = this.projects.get(projectName);
        let stateFromMap = project == undefined ? undefined : project.get(name);
        if (stateFromMap == undefined) {
            console.log("projectName:");
            console.log(projectName);
            const elementsJson = ResourceManager.getElementsFile(projectName);
            return {
                elementsJson: elementsJson,
                lines: [{
                    id: 1,
                    words: [""]
                }],
                currentLine: 1,
                done: true,
                lineCount: 1,
                context: new Context(elementsJson, CodeLogic.getInitialData())
            };
        }
        return stateFromMap;
    }
}

export let stateManager = new StateManager();
