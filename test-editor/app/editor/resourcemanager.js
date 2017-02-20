import FileUtils from '../util/file-utils'

const remote = require('electron').remote;
const path = remote.require('path');

class ResourceManager {
    static getElementsFile(projectName) {
        const config = FileUtils.loadJsonFile(remote.getGlobal('configPath'));
        const elementsPath = config.configs.find(
            c => c['project-name'] == projectName
        )['elements-path'];
        return FileUtils.loadJsonFile(path.join(elementsPath, 'elements.json'));
    }
}

export default ResourceManager