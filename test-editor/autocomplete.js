//GLOBAL VARIABLES
var linesCount = 0;
var currentValues = [];
var currentOperator = {};
var words = [];
var objs = [];
var backspace = false;
var declaration = false;
var newVariableBuffer = "";
var localVariables = ["_local"];
var globalVariables = [];
var types = ["string", "int", "double", "boolean"];
var comparators = ["==", "!=", "<", ">"];
var methods = ['$timeout'];
var forceContextUpdate = false;
var tickImg = undefined;
var lastFont = undefined;
var allowedChars = [];
var spaceExceptions = ["@", "#", "$", "_", '"'];
var sequences = [];
var sequenceHistory = [];
var endOption = false;
var seq = {
    i : -1,
    j : -1,
	links : [],
    inSequence : function() {
        return seq.i > -1 && seq.j > -1;
    },
    get : function(index) {
        return sequences[seq.i][index];
    },
	current : function() {
		return sequences[seq.i][seq.j];
	},
    newSequence : function(obj) {
        sequences.push([]);
		seq.links.push({ 
			i : seq.i,
			j : seq.j
		});
        seq.i = sequences.length - 1;
        seq.j = 0;
		makeSequence(obj);
    },
	push : function(obj) {
		sequences[seq.i].push(obj);	
	},
    next : function() {
		var temp_i = seq.i;
		var temp_j = seq.j + 1;
		var seek = temp_i;
        while (sequences[temp_i][temp_j] == endObj) {
			if (temp_i == 0) {
				return endObj;
			} else {
				var link = seq.links[seek--];
				temp_i = link.i;
				temp_j = link.j + 1;
			}
		}
		return sequences[temp_i][temp_j];
    },
    previous : function() {
        console.log("not implemented");
    },
	forward : function() {
		seq.j++;
		var seek = seq.i;
        while (seq.current() == endObj) {
			if (seq.i == 0) {
				return endObj;
			} else {
				var link = seq.links[seek--];
				seq.i = link.i;
				seq.j = link.j + 1;
			}
		}
		return seq.current();
	},
	back : function() {
		while (true) {
			if (seq.j - 1 < 0) {
				var link = seq.links[seq.i];
				seq.i = link.i;
				seq.j = link.j;
				break;
			}
			var backIndex = seq.links.indexOf({
				i : seq.i,
				j : seq.j-1
			});
			if (backIndex >= 0) {
				seq.i = backIndex;
				seq.j = sequences[seq.i].length-1;
			} else {
				seq.j--;
				break;
			}
		}
		return seq.current();	
	}
};

//HTML ELEMENTS
var mainInput = document.getElementById('mainInput');
var mainInputWrapper;
var written = document.getElementById('written');
var lineNumbersColumn = document.getElementById('linenumbers');

//CONTEXT OBJECTS
var endObj = {
	nt : true,
	prod : ["end"]
};

//Value objects
var textfieldValuesObj = {
	prod : []
};

var buttonValuesObj = {
	prod : []
};

var globalPrefixObj = {
    prod : ["@"]
};

var globalValuesObj = {
    prod : globalVariables
};

var globalObj = {
	nt : true,
    seq : [globalPrefixObj, globalValuesObj, endObj]
};

var localPrefixObj = {
	prod : ["_"]
};

var localValuesObj = {
    prod : localVariables
};

var localObj = {
    nt : true,
    seq : [localPrefixObj, localValuesObj, endObj]
};

var methodPrefixObj = {
	prod : ["$"]
};

var methodValuesObj = {
    prod : methods
};

var methodObj = {
    nt : true,
    seq : [methodPrefixObj, methodValuesObj, endObj]
};

var constStringPrefixObj = {
    prod : '"'
};

var newStringObj = {
    prod : "newstr"
};

var constStringObj = {
	nt : true,
    seq : [constStringPrefixObj, endObj]
};

var numbersObj = {
	prod : ["0", "1", "2", "3", "4", "5", "6", "7", "8", "9"]
};

var numericObj = {
    nt : true,
    variants : [{
		nt : true,
		seq : [numbersObj, endObj]
	}, methodObj]
};

//Operator objects
var timeObj = {
	prod : ["sec", "min", "h"]
};

var withObj = {
    prod : ["with"]
};

var withTimeObj = {
	nt : true,
    seq : [withObj, numericObj, timeObj, endObj]
};

var forkEndOrWithTimeObj = {
	nt : true,
	variants : [withTimeObj, endObj]
};

var elementPrefixObj = {
    prod : ["#"]
};

var clickWordObj = {
	prod : ["click"]
};

var elementsValuesObj = {
	prod : []
};

var elementsObj = {
	nt : true,
	seq : [elementPrefixObj, elementsValuesObj, endObj]
};

var textfieldObj = {
    nt : true,
    seq : [elementPrefixObj, textfieldValuesObj, endObj]
};

var clickObj = {
	nt : true,
	seq : [clickWordObj, elementsObj, forkEndOrWithTimeObj, endObj]
};

var asObj = {
    prod : ["as"]
};

var typeObj = {
	prod : types
};

var asTypeObj = {
    nt: true,
    seq : [asObj, typeObj, endObj]
};

var forkEndOrTypeObj = {
	nt : true,
	variants : [asTypeObj, endObj]
};

var funTextObj = {
    prod : [".text"]
};

var elementTextObj = {
    nt : true,
	seq : [elementPrefixObj, elementsObj, funTextObj, endObj]
};

var stringObj = {
    name : "stringObj",
    nt : true,
    variants : [globalObj, localObj, methodObj, constStringObj, elementTextObj]
};

var assignObj = {
	word : ":",
	seq : [stringObj, endObj]
};

var newLocalVariableObj = {
	word : "_",
	variants : localVariables,
	next : assignObj
};

var newGlobalVariableObj = {
	word : "@",
	variants : globalVariables,
	next : assignObj
};

var newVarObj = {
    nt : true,
    variants : [newLocalVariableObj, newGlobalVariableObj]
};

var fillWordObj = {
    prod : ["fill"]
};

var fillObj = {
	nt : true,
	seq : [fillWordObj, textfieldObj, stringObj, forkEndOrTypeObj, endObj]
};

var compareObj = {
    name : "compareObj",
	prod : comparators
};

var conditionObj = {
    nt : true,
    seq : [stringObj, forkEndOrTypeObj, compareObj, stringObj, forkEndOrTypeObj, endObj]
};

var assertWordObj = {
    prod : ["assert"]
};

var assertObj = {
    nt : true,
	seq : [assertWordObj, conditionObj, endObj]
};

var ifWordObj = {
    prod : ["if"]
};

var ifObj = {
    nt : true,
    seq : [ifWordObj, conditionObj, endObj]
};

var waitWordObj = {
	prod : ["wait"]
};

var waitObj = {
	nt : true,
	seq : [waitWordObj, conditionObj, forkEndOrWithTimeObj, endObj]
};

var mainObj = {
	nt : true,
    variants : [assertObj, clickObj, fillObj, waitObj]
};




//FUNCTIONS
function getLastWord() {
	var splitted = window.input.value.split(' ');
	//////alert(splitted);
	if (splitted.length > 1) {
		if (splitted[splitted.length-1].length == 0) {
			return splitted[splitted.length - 2];
		}
 		return splitted[splitted.length-1];
	}
	return window.input.value;
}

function getWord(obj) {
	/*if (obj == endObj) {
		return 0;
	}*/
	if ('nt' in obj) {
        if (obj == endObj) {
			var hints = [];
			if (seq.next() != endObj) {
				hints = getWord(seq.forward());
				seq.back();
			}
			if (hints.indexOf("end") < 0) {
				hints = hints.concat("end");
			}
			return hints;
        }
        return collectHints(obj);
    } else {
        if ('prod' in obj) {
            return obj.prod;
        }
    }
}

function collectHints(obj) {
    var result = [];
    if ('nt' in obj) {
        
            //currentValues.concat(obj.prod);
        if ('variants' in obj) {
            for (var variant of obj.variants) {
                result = result.concat(getWord(variant));           
            }
        } else if ('seq' in obj) {
            result = result.concat(getWord(obj.seq[0]));
        }
    } else {
        if (seq.inSequence()) {
            result = result.concat(getWord(seq.next()));
        }
    }
    return result;
}

function makeSequence(obj) {
	_makeSequence(obj);
	seq.push(endObj);
}

function _makeSequence(obj) {
    console.log("making sequence for ");
    console.log(obj);
    console.log("sequence: ");
    console.log(obj.seq);
    for (var i in obj.seq) {
        if ('seq' in obj.seq[i]) {
            _makeSequence(obj.seq[i]);
        } else {
			if (obj.seq[i] != endObj) {
				seq.push(obj.seq[i]);	
			}
        }
    }
}

function getNextOperator(obj) {
	if (obj == endObj) {
		return getNextOperator(seq.forward());
	}
    if ('nt' in obj) {
        if ('variants' in obj) {
            for (var variant of obj.variants) {
                if (getWord(variant).indexOf(getLastWord()) >= 0) {
                    return getNextOperator(variant);
                }
            }
            return obj;
        } else if ('seq' in obj) {
			seq.newSequence(obj);
			return getNextOperator(seq.current());
        }
    } else {
        if (seq.inSequence()) {
            var next = seq.next();
            if (seq.current() == constStringPrefixObj) {
                return seq.current();
            }
			if ('nt' in next) {
				return seq.forward();
			} else {
				if (getWord(next).indexOf(getLastWord()) >= 0) {
					return getNextOperator(seq.forward());
				}
				return seq.current();
			}
			
        } else {
            console.log("unexpected");
            return null;
        }
    }
        
}

function contextWalker() {
	//////alert("cw");
	//////alert(getLastWord());
	//////alert(currentValues.indexOf(getLastWord()));
    console.log("in context walker");
	var lastWord = getLastWord();
	if (words.length > 0) {
		if (!isVariableAssignment() && words[words.length-1] == lastWord[0] && currentValues.indexOf(lastWord.substr(1, lastWord.length)) >= 0) {
			forceContextUpdate = true;
		}
	}
	if ((currentValues.indexOf(getLastWord()) >= 0 && !isVariableAssignment()) || forceContextUpdate) {
		forceContextUpdate = false;
        //change operator
        currentOperator = getNextOperator(currentOperator);
		
		if (words.length > 0) {
			if (words[words.length-1] == lastWord[0]) {
				words.push(lastWord.substr(1, lastWord.length));
			} else {
				words.push(lastWord);
			}
		} else {
			words.push(lastWord);
		}
		
		objs.push(currentOperator);
        console.log("after change of currentOperator");
        
		currentValues = [];
		
		if (currentOperator == endObj) {
			window.done = true;
			currentValues = [];
			
			//initContext();
			
		} else {
			window.done = false;
			currentValues = collectHints(currentOperator);
			handleEnd();
            handleConstString();
			/*if (currentSeq.length > 0 && seqIndex < currentSeq.length-1) {
				if (currentValues.length == 0) {
					currentValues = currentValues.concat(getVariants(currentSeq[seqIndex + 1]));
					seqIndex++;
				}
			}*/
			//////alert("currentValues: " + currentValues);
		}
		window.awesomplete.filter = showAllFilter;
		//window.awesomplete.list = currentValues;
		//////alert(words);
        initAllowedChars();
        colorizer();
	} else {
		window.awesomplete.filter = myFilter;
	}
    if (currentValues.length == 1 && !endOption && currentOperator != constStringPrefixObj) {
        myReplace(currentValues[0]);
        forceContextUpdate = true;
        contextWalker();
    } else {
        endOption = false;
    }
    console.log("context walker finished");
}

function handleEnd() {
    if (currentValues.indexOf("end") >= 0) {
        endOption = true;
		if (!seq.inSequence() || (seq.next() == endObj)) {
            console.log(sequences);
        	window.done = true;
		}
		currentValues.splice(currentValues.indexOf("end"), 1);
		console.log("condition of variants concat: ");
		if (sequences[seq.i].length > 0 && seq.j < sequences[seq.i].length-1) {
			//currentValues = currentValues.concat(collectHints(seq.next));
			//currentOperator = currentSeq[seqIndex + 1];
			//currentValues = currentValues.concat(collectHints(currentSeq[seqIndex + 1]));
			//seqIndex++;
			console.log("currentValues after concat: " + currentValues);
		}
	}
	
}

function handleConstString() {
    if (isConstStringType()) {
        currentValues = [];
        window.awesomplete.close();
    }
}

function initContext() {
	currentOperator = mainObj;
	currentValues = collectHints(mainObj);
    sequences = [];
	seq.links = [];
    seq.i = -1;
    seq.j = -1;
	backspace = false;
	endOption = false;
	words = [];
	objs = [];
	objs.push(mainObj);
	newVariableBuffer = "";
	window.awesomplete.open();
    initAllowedChars();
	//////alert(currentValues);
}
function myFilter(hint, input) {
	//////alert(currentValues);
	var arr = input.split(' ');
	if (arr.length > 1) {
		var lastWord = getLastWord();
		return hint.toLowerCase().indexOf(lastWord.toLowerCase()) > -1;
	}
	return hint.toLowerCase().indexOf(input.toLowerCase()) > -1;
	
}

function showAllFilter(hint, input) {
	return true;
}

function myReplace(text) {
	//lert(text);
	if (this.input.value.length != 0) {
		var splitted = this.input.value.split(' ');
		this.input.value = "";
		for (var i = 0; i < splitted.length - 1; i++) {
			this.input.value += splitted[i] + " ";
		}
	}
	
	/*if (words.indexOf(splitted[splitted.length-1])) {
		this.input.value += splitted[splitted.length-1] + ' ';
	}*/
    console.log("replacing with text = " + text + ".");
	this.input.value += text;
}

function newLiner() {
    linesCount++;
    //linenumber
    var lineNumber = document.createElement('div');
    lineNumber.setAttribute('class', 'linenumber');
    lineNumber.innerHTML = linesCount;
    lineNumber.appendChild(tickImg);
    lineNumbersColumn.appendChild(lineNumber);
    //newline
    var div = document.createElement('div');
    div.setAttribute('id', linesCount);
    console.log(div);
    div.appendChild(mainInputWrapper);
    written.appendChild(div);
    //tickImg
    handleTickImg();
}

function requiresSpace(str) {
    var addSpace = false;
    if (spaceExceptions.indexOf(str[0]) >= 0) {
        if (str.length > 1) {
            addSpace = true;
        }
    } else {
        addSpace = true;
    }
    if (addSpace) {
        str = str + ' ';
    }

    return str;
}

function initLastFont(str) {
    var createNew = false;
    if (spaceExceptions.indexOf(str[0]) >= 0) {
        if (str.length == 1) {
            createNew = true;
        }
    } else {
        createNew = true;
    }
    if (createNew) {
        lastFont = document.createElement('span');
        console.log('mainInputWrapper:');
        console.log(mainInputWrapper);
        document.getElementById(linesCount).insertBefore(lastFont, mainInputWrapper);
    }
}

function colorizer() {
    var str = mainInput.value;
    console.log("colorizer, str = " + str + ".");
	var keyWords = ["click", "fill", "assert", "wait", ":", "with", "sec", "min", "h", "as"];
    var varPrefixes = ["_", "@", "$"];
	var inStr = false;
    var className = 'ordinary';
    if (inStr) {
        className = 'const-string';
    }

    if (keyWords.indexOf(str.trim()) >= 0) {
        className = 'keyword';
    } else if (str[0] == '"') {
        inStr = true;
        className = "const-string";
    } else if (str[0] == '#') {
        className = "element-name";
    } else if (varPrefixes.indexOf(str[0]) >= 0) {
        className = "variable-name";
    }
    str = requiresSpace(str);
    initLastFont(str);
    lastFont.setAttribute("class", className);
    lastFont.innerHTML = str;
    if (str[str.length-1] == '"') {
        inStr = false;
    }
	mainInput.value = '';
}

function excludeWrongChars(event) {
    if (currentOperator == newLocalVariableObj || currentOperator == newGlobalVariableObj || currentOperator == constStringObj
       || window.input.value.match(/^\d+$/) != undefined) {
      return;  
    } else {
        if (allowedChars.length == 0) {
            initAllowedChars();
        }
        var inputChar = String.fromCharCode(event.keyCode ? event.keyCode : event.which);
        if (inputChar >= 'A' && inputChar <= 'Z') {
            inputChar = inputChar.toLowerCase();
        }
        if ((allowedChars.indexOf(inputChar) < 0 && !((window.input.value.length == 0 || window.input.value.match(/^\d+$/) != undefined) && inputChar >= 1 && inputChar <= 9 ))
           || (getVisibleHints().count == 0 && window.awesomplete.filter == myFilter)) {
            event.preventDefault();
        }
        
    }
}

function initAllowedChars() {
    allowedChars = [];
    for (var i = 0; i < currentValues.length; i++) {
        var hint = currentValues[i];
        for (var j = 0; j < hint.length; j++) {
            if (hint == 'end') {
                continue;
            }
            var char = hint[j];
            if (allowedChars.indexOf(char) < 0) {
                allowedChars.push(char.toLowerCase());
            }
                
        }
    }
}

function isFirefox() {
    return navigator.userAgent.toLowerCase().indexOf('firefox') > -1;
}

window.onkeypress = function(e) {
    excludeWrongChars(e);
    if (isFirefox()) {
        var key = e.keyCode ? e.keyCode : e.which;
        var input = window.input;
        var inputStr = input.value;
        if (key == 8) {
            e.preventDefault();
            backspace = true;
            if (window.done) {
                window.done = false;
            }
            if (words.length > 0) {
                var newLength = 0;
                ////alert(words);
                ////alert(objs);


                if (getLastWord() == words[words.length-1] || getLastWord().substr(1, getLastWord().length) == words[words.length-1])  {
                    if (words.length >= 1) {
                        if (!(spaceExceptions.indexOf(words[words.length-1]) >= 0)) {
                            newLength = newLength - 1;
                            ////alert(newLength);
                        }
                    }
                    newLength = newLength + inputStr.length - words[words.length-1].length;
                    ////alert(newLength);
                    words.splice(words.length-1, 1);
                    ////alert(words);
                } else {
                    ////alert("else");
                    //alert(getLastWord());
                    if (words.length >= 1) {
                        if (spaceExceptions.indexOf(words[words.length-1]) >= 0) {
                            newLength = newLength + 1;
                            ////alert(newLength);
                        }
                    }
                    newLength = newLength + inputStr.length - getLastWord().length;
                    //alert(newLength);
                }

                if (objs.length == 1) {
                    newLength = newLength - 1;
                }
                input.value = input.value.substr(0, newLength);
            }

            if (objs.length == 1) {
                newVariableBuffer = "";
            } else {
                //if (getVariants(objs[objs.length-2]).indexOf(getLastWord())) {
                if (objs.length - words.length == 2) {// || (currentOperator == assignObj && getLastWord().length > 1)) {
                    objs.splice(objs.length-1, 1);

                }



            }
            currentOperator = objs[objs.length-1];
            currentValues = getVariants(currentOperator);
            initAllowedChars();
            handleEnd();
            window.awesomplete.filter = showAllFilter;
            evaluate();
        }
    }
    
}
		
window.onkeydown = function(e) {
	var key = e.keyCode ? e.keyCode : e.which;
	var input = window.input;
	var inputStr = input.value;
	if (key == 8 && !isFirefox()) {
		backspace = true;
		if (window.done) {
			window.done = false;
		}
		if (words.length > 0) {
			var newLength = 0;
			////alert(words);
			////alert(objs);
			
			
			if (getLastWord() == words[words.length-1] || getLastWord().substr(1, getLastWord().length) == words[words.length-1])  {
				if (words.length >= 1) {
					if (!(spaceExceptions.indexOf(words[words.length-1]) >= 0)) {
						newLength = newLength - 1;
						////alert(newLength);
					}
				}
				newLength = newLength + inputStr.length - words[words.length-1].length;
				////alert(newLength);
				words.splice(words.length-1, 1);
				////alert(words);
			} else {
				////alert("else");
				//alert(getLastWord());
				if (words.length >= 1) {
					if (spaceExceptions.indexOf(words[words.length-1]) >= 0) {
						newLength = newLength + 1;
						////alert(newLength);
					}
				}
				newLength = newLength + inputStr.length - getLastWord().length;
				//alert(newLength);
			}
			
			if (objs.length == 1) {
				newLength = newLength - 1;
			}
			input.value = input.value.substr(0, newLength + 1);
			
		}
		
		if (objs.length == 1) {
			newVariableBuffer = "";
		} else {
			//if (getVariants(objs[objs.length-2]).indexOf(getLastWord())) {
			if (objs.length - words.length == 2) {// || (currentOperator == assignObj && getLastWord().length > 1)) {
				objs.splice(objs.length-1, 1);
				
			}
			
			
			
		}
		currentOperator = objs[objs.length-1];
		currentValues = getVariants(currentOperator);
        initAllowedChars();
        handleEnd();
        window.awesomplete.filter = showAllFilter;
	} else if (key == 18) {
		e.preventDefault();
		e.stopPropagation();
		if (inputStr.match(/^\d+$/) != undefined) {
			inputStr += ":";
			window.done = true;
		}
		
		if (window.done) {
			
			if (objs[1] == newLocalVariableObj ) {
                if (localVariables.indexOf(newVariableBuffer) < 0) {
                    localVariables.push(newVariableBuffer);
                }
			} else if (objs[1] == newGlobalVariableObj) {
                if (globalVariables.indexOf(newVariableBuffer) < 0) {
                    globalVariables.push(newVariableBuffer);    
                }
			}
			//document.getElementById("written").innerHTML += "<p>" + inputStr + "\n" + "</p>";
			colorizer();
            newLiner();
			input.value = "";
			window.done = false;
			initContext();
			window.input.focus();
			window.awesomplete.list = currentValues;
			window.awesomplete.open();
		}
	}
	if (window.awesomplete.list instanceof Array && window.awesomplete.list.length == 1) {
		
		/*var splitted = this.input.value.split(' ');
		input.value = "";
		for (var i = 0; i < splitted.length - 1; i++) {
			this.input.value += splitted[i] + " ";
		}
		this.input.value += window.awesomplete.list[0] + " ";*/
		window.awesomplete.select();
	}
	setCaretPosition("mainInput", input.value.length);
	
}

function getVisibleHints() {
    var mc = 0;
    var mh = "";
		//////alert(currentValues);
	for (var h in currentValues) {
			//////alert("input: " + window.input.value);
			//////alert("hint: " + currentValues[h]);
        if (myFilter(currentValues[h], window.input.value)) {
                    //////alert("yeah");
            mc = mc + 1;
            mh = currentValues[h];
        }
    }
    return {
        count : mc,
        hint : mh
    };
}

function isVariableAssignment() {
    return currentOperator == newLocalVariableObj || currentOperator == newGlobalVariableObj;
}

function isConstStringType() {
    var word = getLastWord();
    return currentOperator == constStringPrefixObj && !(word[word.length-1] == '"' && word[0] == '"' && word.length > 1);
}

function evaluate() {
	////////alert(1);
	if (!backspace) {
		var input = window.input;
		if (input.value.length == 0) {
			window.awesomplete.list = [];
		}
		var visibleHints = getVisibleHints();
        var matchedCount = visibleHints.count;
        var matchedHint = visibleHints.hint;
		//////alert(matchedCount);
        if (isVariableAssignment()) {
            if (window.input.value[window.input.value.length-1] == ' ') {
                newVariableBuffer = getLastWord();
                forceContextUpdate = true;
            }
        } else if (isConstStringType()) {
            /*var lw = getLastWord();
            if (lw[0] == '"' && lw[lw.length-1] == '"' && lw.length >= 2) {
				forceContextUpdate = true;
                window.input.value += ' ';
            }*/
            forceContextUpdate = true;
            window.input.value += ' ';
        } else {
            if (matchedCount == 1) {
                 myReplace(matchedHint);
            }
           
        }
//		if (matchedCount == 1 && !isVariableAssignment()) {
//			myReplace(matchedHint);
//		} else if (matchedCount != 1) {
//			if (isVariableAssignment()) {
//				if (window.input.value[window.input.value.length-1] == ' ') {
//                    console.log("end of variable assignment");
//					newVariableBuffer = getLastWord();
//					forceContextUpdate = true;
//				}
//			} else if (currentOperator == constStringObj) {
//                var lw = getLastWord();
//				if (lw[0] == '"' && lw[lw.length-1] == '"') {
//					forceContextUpdate = true;
//                    window.input.value += ' ';
//				}
//			}
//		}
		contextWalker();
	} else {
		backspace = false;
	}
	//alert(currentValues);
	window.input.focus();
	window.awesomplete.list = currentValues;
	if (currentOperator != endObj) {
		window.awesomplete.open();
	} else {
		window.awesomplete.close();
	}
	handleTickImg();
	
}

function handleTickImg() {
    if (window.done) {
        tickImg.style.visibility = "visible";
    } else {
        tickImg.style.visibility = "hidden";
    }
}

function initTickImg() {
    if (tickImg == undefined) {
        tickImg = document.createElement('span');
        tickImg.setAttribute("class", "fa fa-check");
        //document.querySelector("div.awesomplete>div.awesomplete").appendChild(tickImg);
    }
    handleTickImg();
}

function setCaretPosition(elemId, caretPos) {
    var elem = document.getElementById(elemId);

    if(elem != null) {
        if(elem.createTextRange) {
            var range = elem.createTextRange();
            range.move('character', caretPos);
            range.select();
        }
        else {
            if(elem.selectionStart) {
                elem.focus();
                elem.setSelectionRange(caretPos, caretPos);
            }
            else
                elem.focus();
        }
    }
}

function initObjs(elementsJsonParam) {
	textfieldValuesObj.prod = Object.keys(elementsJsonParam["TextField"]);
	buttonValuesObj.prod = Object.keys(elementsJsonParam["Button"]);
	for (var i = 0; i < textfieldValuesObj.prod.length; i++) {
		textfieldValuesObj.prod[i] = "#" + textfieldValuesObj.prod[i];
	}
	for (var i = 0; i < buttonValuesObj.prod.length; i++) {
		buttonValuesObj.prod[i] = "#" + buttonValuesObj.prod[i];
	}
	elementsValuesObj.prod = textfieldValuesObj.prod.concat(buttonValuesObj.prod);
}

function scriptLoader(scripts, callback) {

    var count = scripts.length;

    function urlCallback(url) {
        return function () {
            console.log(url + ' was loaded (' + --count + ' more scripts remaining).');
            if (count < 1) {
                callback();
            }
        };
    }

    function loadScript(url) {
        var s = document.createElement('script');
        s.setAttribute('src', url);
        s.onload = urlCallback(url);
        document.head.appendChild(s);
    }

    for (var script of scripts) {
        loadScript(script);
    }
}

function addElementsJson(projectName) {
    for (var i = 0; i < configJson.configs.length; i++) {
        if (configJson.configs[i]['project-name'] == projectName) {
            var path = "file://" + configJson.configs[i]['elements-path'].replace(/\\/g, "/") + '/elements.json';
            scriptLoader([path], function() {
				initObjs(elementsJson);
			});
            break;
        }
    }
    
    
    
	/*var xmlDoc;
	if (window.DOMParser)
	{
		parser = new DOMParser();
		xmlDoc = parser.parseFromString(configXml, "text/xml");
	} else // Internet Explorer
	{
		xmlDoc = new ActiveXObject("Microsoft.XMLDOM");
		xmlDoc.async = false;
		xmlDoc.loadXML(configXml);
	}
	var configs = xmlDoc.getElementsByTagName("config");
	for (var i = 0; i < configs.length; i++) {
		if (configs[i].getAttribute("project-name") == projectName) {
			console.log(configs[i].childNodes[0].childNodes[0].nodeValue);
			var path = configs[i].childNodes[0].childNodes[0].nodeValue.replace(/\\/g, "/") + '/elements.json';
			console.log(path);
			scriptLoader([path], function() {
				initObjs(elementsJson);
			});
			//document.getElementById("elementsJsonScript").innerHTML = '<script type="text/javascript" src="' + 'file:///' + path + '"</script>';
		}
	}*/
	
}







window.onload = function(e) {
	addElementsJson("kinopoisk");
	//initObjs(elementsJson);
	
	window.input = document.getElementById("mainInput");
	window.input.oninput = evaluate;
	
	window.awesomplete = new Awesomplete(input, {
			autoFirst : true,
			minChars : "0",
            maxItems : 99999,
			filter : myFilter,
			list : [],
			replace : myReplace
		});
	window.addEventListener("awesomplete-close", function(e){
  // The popup just closed.
  contextWalker();

  window.awesomplete.list = currentValues;
  if (currentOperator != endObj) {
	  window.awesomplete.open();
  }
}, false);
    mainInputWrapper = document.querySelector('div.awesomplete');
    initTickImg();
    newLiner();
	initContext();
	window.input.focus();
	window.awesomplete.list = currentValues;
	window.awesomplete.open();
}

