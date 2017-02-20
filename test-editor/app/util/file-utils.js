const remote = require('electron').remote
const fs = remote.require('fs')

class FileUtils {
    constructor() {
        console.log('created')
    }

    static loadFile(path) {
        return fs.readFileSync(path, 'UTF8')
    }

    static loadJsonFile(path) {
        console.log(`Loading json file with path [${path}]`);
        return JSON.parse(
            FileUtils.loadFile(path)
        );
    }
}

export default FileUtils