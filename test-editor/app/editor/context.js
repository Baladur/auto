import Sequence from './sequence'

const END = {
    nt : true,
    prod : ["end"]
};

const CLICK = {
    prod: ["click"]
};

const FILL = {
    prod: ["fill"]
};

const ASSERT = {
    prod: ["assert"]
};

const IF = {
    prod: ["if"]
};

const AS = {
    prod: ["as"]
};

const WITH = {
    prod: ["with"]
};

const TIME = {
    prod: ["sec", "min", "h"]
};

const LOCAL = {
    prod: ["_"]
};

const GLOBAL = {
    prod: ["@"]
};

const METHOD = {
    prod: ["$"]
};

const ELEMENT = {
    prod: ["#"]
};

const CONST_STRING = {
    prod: ['"']
};

const WAIT = {
    prod: ["wait"]
};

const comparators = ["==", "!=", "<", ">"];

class Context {
    constructor(elementsJson, initialData) {
        this.elementsJson = elementsJson;
        this.seq = new Sequence();
        this.localVariables = initialData.localVariables;
        this.globalVariables = initialData.globalVariables;
        this.types = initialData.types;
        this.methods = initialData.methods;
        this.seq = {};
        this.sequenceHistory = [];

        //Value objects
        this.textfieldValuesObj = {
            prod : []
        };

        this.buttonValuesObj = {
            prod : []
        };

        this.globalValuesObj = {
            prod : this.globalVariables
        };

        this.globalObj = {
            nt : true,
            seq : [GLOBAL, this.globalValuesObj, END]
        };

        this.localValuesObj = {
            prod : this.localVariables
        };

        this.localObj = {
            nt : true,
            seq : [LOCAL, this.localValuesObj, END]
        };

        this.methodValuesObj = {
            prod : this.methods
        };

        this.methodObj = {
            nt : true,
            seq : [METHOD, this.methodValuesObj, END]
        };

        this.newStringObj = {
            prod : "newstr"
        };

        this.constStringObj = {
            nt : true,
            seq : [CONST_STRING, END]
        };

        this.numbersObj = {
            prod : ["0", "1", "2", "3", "4", "5", "6", "7", "8", "9"]
        };

        this.numericObj = {
            nt : true,
            variants : [{
                nt : true,
                seq : [this.numbersObj, END]
            }, this.methodObj]
        };

        this.newLocalVariableObj = {

        };

        this.newGlobalVariableObj = {

        };

        //Operator objects

        this.withTimeObj = {
            nt : true,
            seq : [WITH, this.numericObj, TIME, END]
        };

        this.forkEndOrWithTimeObj = {
            nt : true,
            variants : [this.withTimeObj, END]
        };

        this.elementsValuesObj = {
            prod : []
        };

        this.elementsObj = {
            nt : true,
            seq : [ELEMENT, this.elementsValuesObj, END]
        };

        this.textfieldObj = {
            nt : true,
            seq : [ELEMENT, this.textfieldValuesObj, END]
        };

        this.clickObj = {
            nt : true,
            seq : [CLICK, this.elementsObj, this.forkEndOrWithTimeObj, END]
        };

        this.typeObj = {
            prod : this.types
        };

        this.asTypeObj = {
            nt: true,
            seq : [AS, this.typeObj, END]
        };

        this.forkEndOrTypeObj = {
            nt : true,
            variants : [this.asTypeObj, END]
        };

        this.funTextObj = {
            prod : [".text"]
        };

        this.elementTextObj = {
            nt : true,
            seq : [ELEMENT, this.elementsObj, this.funTextObj, END]
        };

        this.stringObj = {
            name : "stringObj",
            nt : true,
            variants : [this.globalObj, this.localObj, this.methodObj, this.constStringObj, this.elementTextObj]
        };

        this.assignObj = {
            word : ":",
            seq : [this.stringObj, END]
        };

        this.fillObj = {
            nt : true,
            seq : [FILL, this.textfieldObj, this.stringObj, this.forkEndOrTypeObj, END]
        };

        this.compareObj = {
            name : "compareObj",
            prod : comparators
        };

        this.conditionObj = {
            nt : true,
            seq : [this.stringObj, this.forkEndOrTypeObj, this.compareObj, this.stringObj, this.forkEndOrTypeObj, END]
        };

        this.assertObj = {
            nt : true,
            seq : [ASSERT, this.conditionObj, END]
        };

        this.ifObj = {
            nt : true,
            seq : [IF, this.conditionObj, END]
        };

        this.waitObj = {
            nt : true,
            seq : [WAIT, this.conditionObj, this.forkEndOrWithTimeObj, END]
        };

        this.mainObj = {
            nt : true,
            variants : [this.assertObj, this.clickObj, this.fillObj, this.waitObj]
        };
        this.init();
    }

    /****PUBLIC PROPERTIES****/

    static get END() {
        return END;
    }

    get allowedCharacters() {
        let allowedChars = [];
        console.log("current hints:");
        console.log(this._currentHints);
        this._currentHints.forEach(hint => {
            for (let i in hint) {
                if (allowedChars.indexOf(hint[i]) < 0) {
                    allowedChars.push(hint[i].toLowerCase());
                }
            }
        });
        console.log("allowed characters:");
        console.log(allowedChars);
        return allowedChars;
    }

    get currentHints() {
        return this._currentHints.map(hint => ({ word: hint }));
    }

    get shouldExcludeCharacters() {
        return !(this._currentOperator == this.newLocalVariableObj
        || this._currentOperator == this.newGlobalVariableObj
        || this._currentOperator == this.constStringObj);
    }

    get current() {
        return this._currentOperator;
    }

    /****PUBLIC METHODS****/

    init() {
        this._currentOperator = this.mainObj;
        this._currentHints = this._collectHints(this._currentOperator);
        if (this.seq != {}) {
            this.sequenceHistory.push(this.seq);
        }
        this.seq = new Sequence();
        this.words = [];
        this.newVariableBuffer = "";
        this._initObjs();
    }

    forward() {

    }

    /****PRIVATE METHODS****/

    _initObjs() {
        this.textfieldValuesObj.prod = Object.keys(this.elementsJson["TextField"]);
        this.buttonValuesObj.prod = Object.keys(this.elementsJson["Button"]);

        this.textfieldValuesObj.prod.forEach(tf => tf = "#" + tf);
        this.buttonValuesObj.prod.forEach(b => b = "#" + b);

        this.elementsValuesObj.prod = this.textfieldValuesObj.prod.concat(this.buttonValuesObj.prod);
    }

    _getWord(obj) {
        if ('nt' in obj) {
            if (obj == END) {
                let hints = [];
                if (this.seq.next() != END) {
                    hints = getWord(this.seq.forward());
                    this.seq.back();
                }
                if (hints.indexOf("end") < 0) {
                    hints = hints.concat("end");
                }
                return hints;
            }
            return this._collectHints(obj);
        } else {
            if ('prod' in obj) {
                return obj.prod;
            }
        }
    }

    _collectHints(obj) {
        let result = [];
        if ('nt' in obj) {
            if ('variants' in obj) {
                for (let variant of obj.variants) {
                    result = result.concat(this._getWord(variant));
                }
            } else if ('seq' in obj) {
                result = result.concat(this._getWord(obj.seq[0]));
            }
        } else {
            if (this.seq.inSequence()) {
                result = result.concat(this._getWord(this.seq.next()));
            }
        }
        return result;
    }
}

export default Context