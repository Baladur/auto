const alt = 18;
const backspace = 8;

class EditorKeys {
    constructor() {
        this._alt = alt;
        this._backspace = backspace;
    }

    /**
     * @return {number}
     */
    static get NEW_LINE() {
        return 18;
    }

    /**
     * @return {number}
     */
    static get DELETE() {
        return 8;
    }
}

export default EditorKeys