import FileUtils from '../util/file-utils'

class StateManager {
    constructor() {
        this.states = new Map();
    }

    putState(stateKey, state) {
        console.log("put state");
        console.log(state);
        this.states.set(stateKey, state);
    }

    getState(stateKey) {
        let stateFromMap = this.states.get(stateKey);
        if (stateFromMap == undefined) {
            console.log("projectName:");
            console.log(stateKey[0]);
            return {
                elementsJson: FileUtils.loadElementsJson(stateKey[0]),
                lines: [{
                    id: 1,
                    words: []
                }],
                currentLine: 1,
                done: false,
                lineCount: 1
            };
        }
        return stateFromMap;
    }
}

export let stateManager = new StateManager();
