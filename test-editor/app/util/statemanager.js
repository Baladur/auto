import FileUtils from '../util/file-utils'
import Context from '../editor/context'
import CodeLogic from '../containers/code.container'

class StateManager {
    constructor() {
        this.projects = new Map();
    }

    putState(projectName, name, state) {
        console.log("put state");
        let project = this.projects.get(projectName);
        if (project == undefined) {
            project = new Map();
        }
        project.set(name, state);
        this.projects.set(projectName, project);
    }

    getState(projectName, name) {
        let project = this.projects.get(projectName);
        let stateFromMap = project == undefined ? undefined : project.get(name);
        if (stateFromMap == undefined) {
            console.log("projectName:");
            console.log(projectName);
            const elementsJson = FileUtils.loadElementsJson(projectName);
            return {
                elementsJson: elementsJson,
                lines: [{
                    id: 1,
                    words: []
                }],
                currentLine: 1,
                done: false,
                lineCount: 1,
				key: "n/a",
                context: new Context(elementsJson, CodeLogic.getInitialData())
            };
        }
        return stateFromMap;
    }
}

export let stateManager = new StateManager();
