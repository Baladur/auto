const remote = require('electron').remote
const fs = remote.require('fs')
const path = remote.require('path')

class FileUtils {
    constructor() {
        console.log('created')
    }

    static loadElementsJson(projectName) {
        let elementsJson
        console.log("path = " + remote.getGlobal('configPath'))
        const config = JSON.parse(
            fs.readFileSync(remote.getGlobal('configPath'), 'UTF8')
        )
        const elementsPath = config.configs.find(
            c => c['project-name'] == projectName
        )['elements-path']
        elementsJson = JSON.parse(
            fs.readFileSync(path.join(elementsPath, 'elements.json'), 'UTF8')
        )
        return elementsJson
    }
}

export default FileUtils