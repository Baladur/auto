//GLOBAL VARIABLES
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
var methods = ['$timeout'];
var forceContextUpdate = false;
var tickImg = undefined;
var allowedChars = [];
var spaceExceptions = ["@", "#", "$", "_", '"', '.'];
var currentSeq = [];
var seqIndex = -1;

//CONTEXT OBJECTS
var endObj = {
	word : "end",
	variants : []
};

var textfieldObj = {
	word : "",
	variants : []
};

var buttonObj = {
	word : "",
	variants : []
};

var elementsObj = {
	word : "",
	variants : []
};

var numbersObj = {
	word : "",
	variants : ["0", "1", "2", "3", "4", "5", "6", "7", "8", "9"]
};

var timeObj = {
	word : "",
	variants : ["sec", "min", "h"],
	next : endObj
};

var withTimeObj = {
	word : "with",
	variants : numbersObj,
	next : timeObj
};

var forkEndOrWithTimeObj = {
	fork : true,
	with : withTimeObj,
	end : endObj
};

var clickObj = {
	word : "click",
	variants : elementsObj,
	next : forkEndOrWithTimeObj//forker([{input : "with", obj : withTimeObj}, {input : "end", obj : endObj}])
};

var typeObj = {
	variants : types,
    next : endObj
};

var asObj = {
    word : "as",
    variants : typeObj,
    next : endObj
};

var forkEndOrTypeObj = {
	fork : true,
	as : asObj,
	end : endObj
};

var globalObj = {
	word : "@",
	variants : globalVariables,
	next : forkEndOrTypeObj
};

var localObj = {
	word : "_",
	variants : localVariables,
	next : forkEndOrTypeObj
};

var methodObj = {
	word : "$",
	variants : methods,
	next : forkEndOrTypeObj
};

var constStringObj = {
	word : '"',
	variants : [],
	next : forkEndOrTypeObj
};

var elementTextObj = {
	word : '#',
	variants : elementsObj,
	next : {
		word : ".",
		variants : [".text"],
		next : forkEndOrTypeObj
	}
};

var stringObj = {
    name : "stringObj",
    nt : true,
    variants : [globalObj, localObj, methodObj, constStringObj, elementTextObj],
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

var fillObj = {
	word : "fill",
	variants : textfieldObj,
	next : stringObj
};

var equalObj = {
	word : "=="
};

var moreObj = {
	word : ">"
};

var lessObj = {
	word : "<"
};

var notEqualObj = {
	word : "!="
};

var compareObj = {
    name : "compareObj",
	nt : true,
	variants : [equalObj, moreObj, lessObj, notEqualObj]
};

var assertLocalObj = {
	word : "_",
	variants : localVariables,
	next : compareObj
};

var assertGlobalObj = {
	word : "@",
	variants : globalVariables,
	next : compareObj
};

var assertForkObj = {
	word : "assert",
	fork : true,
    '_' : assertLocalObj,
    '@' : assertGlobalObj
};

var conditionObj = {
    seq : [stringObj, compareObj, stringObj]
};

var assertObj = {
    word : "assert",
	seq : [conditionObj, endObj]
};

var ifObj = {
    word : "if",
    seq : [conditionObj]
};

var mainObj = {
	fork : true,
	click : clickObj,
	fill : fillObj,
	'_' : newLocalVariableObj,
	'@' : newGlobalVariableObj,
	assert : assertObj
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
	if (!('word' in obj)) {
		return getVariants(obj);
	} else {
		return obj.word;
	}
}

function getVariants(obj) {
    console.log("getVariants start");
	var variants = [];
	if ('fork' in obj) {
        console.log("in fork");
		for (var key in obj) {
			//////alert(key);
			if (key != 'fork' && key != 'word') {
				var word = getWord(obj[key]);
				////////alert(word);
				if (word != 0) {
					if (word instanceof Array) {
						variants = variants.concat(word);
					} else {
						variants.push(word);
					}
				}
			}
		}
		//////alert("variants: " + variants);
		return variants;
	} else if ('nt' in obj) {
        console.log("in nt");
        for (var i in obj.variants) {
            var word = getWord(obj.variants[i]);
            if (word instanceof Array) {
                variants = variants.concat(word);
            } else {
                variants.push(word);
            }
        }
    } else if ('seq' in obj) {
        console.log("in seq");
        return getVariants(obj.seq[0]);
    } else if ('variants' in obj) {
        console.log("in variants");
		var tempObj = obj;
		while ('variants' in tempObj) {
			tempObj = tempObj.variants;
		}
		//////alert("variants" + tempObj);
		variants = tempObj;
	} else if ('next' in obj) {
        console.log("in next");
		var word = getWord(obj.next);
        if (word instanceof Array) {
            variants = variants.concat(word);
        } else {
            variants.push(word);
        }
	} else if (currentSeq.length > 0 && seqIndex < currentSeq.length-1) {
        console.log("last condition variants:");
        var word = getWord(currentSeq[seqIndex+1]);
        if (word instanceof Array) {
            variants = variants.concat(word);
        } else {
            variants.push(word);
        }
    } else {
        console.log("wtf");
    }
	
	return variants;
}

function makeSequence(obj) {
    console.log("making sequence for ");
    console.log(obj);
    console.log("sequence: ");
    console.log(obj.seq);
    for (var i in obj.seq) {
        if ('seq' in obj.seq[i]) {
            makeSequence(obj.seq[i]);
        } else {
            currentSeq.push(obj.seq[i]);
            console.log("pushed obj to currentSeq: " + obj['word']);
        }
    }
}

function contextWalker() {
	//////alert("cw");
	//////alert(getLastWord());
	//////alert(currentValues.indexOf(getLastWord()));
    console.log("in context walker");
	var excludingWordsLast = false;
	var lastWord = getLastWord();
	if (words.length > 0) {
		if (!isVariableAssignment() && words[words.length-1] == lastWord[0] && currentValues.indexOf(lastWord.substr(1, lastWord.length)) >= 0) {
			excludingWordsLast = true;
		}
	}
    console.log("curentValues = " + currentValues);
	console.log("lastWord = " + getLastWord());
	if ((currentValues.indexOf(getLastWord()) >= 0 && !isVariableAssignment()) || excludingWordsLast || forceContextUpdate) {
		excludingWordsLast = false;
		forceContextUpdate = false;
		if ('fork' in currentOperator) {
			currentOperator = currentOperator[getLastWord()];
            console.log("new operator after fork: ");
            console.log(currentOperator);
			if (currentOperator == undefined) {
				var variantsFromSeq = getVariants(currentSeq[seqIndex+1]);
				for (var i in variantsFromSeq) {
					if (variantsFromSeq[i] == getLastWord()) {
						seqIndex++;
						currentOperator = currentSeq[seqIndex];
            			console.log("seq instead of fork: ");
            			console.log(currentOperator);
						console.log("seqIndex = " + seqIndex);
					}
				}
			}
		} else if ('next' in currentOperator) {
			currentOperator = currentOperator.next;
            if (currentSeq.length > 0) {
                if (currentOperator == endObj && seqIndex < currentSeq.length-1) {
                    if ('variants' in currentOperator) {
                        currentOperator = currentOperator.variants;
                    } else {
                        seqIndex++;
                        currentOperator = currentSeq[seqIndex];
                    }
                    
                }
            }
            
			console.log("new operator after next: ");
            console.log(currentOperator);
		} else {
            //if we are here, it means we are in sequence
            
            seqIndex++;
            currentOperator = currentSeq[seqIndex];
            console.log("new operator after seq: ");
            console.log(currentOperator);
			console.log("seqIndex = " + seqIndex);
            
        }
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
        if ('seq' in currentOperator) {
            console.log("in condition of seq");
            currentSeq = [];
            makeSequence(currentOperator);
            seqIndex = -1;
			console.log("currentSeq: " + currentSeq);
            if (getLastWord() != currentOperator.word) {
                myReplace(currentOperator.word);
            }
            
        } else if ('nt' in currentOperator) {
            console.log("in nt");
            var changedOperator = false;
            for (var i in currentOperator.variants) {
                console.log("word from variant:" + currentOperator.variants[i].word);
                console.log("last word: " + getLastWord());
                if (currentOperator.variants[i].word == getLastWord()) {
                    currentOperator = currentOperator.variants[i];
                    console.log("new operator after deeping into nt: ");
                    console.log(currentOperator);
                    changedOperator = true;
                    break;
                }
            }
            /*if (!changedOperator) {
                console.log("calling contextWalker again");
                contextWalker();
                return;
            }*/
        }
		////alert(words);
		////alert(objs);
		
		currentValues = [];
		
		if (currentOperator == endObj) {
			window.done = true;
			currentValues = [];
			
			//initContext();
			
		} else {
			window.done = false;
			currentValues = getVariants(currentOperator);
			handleEnd();
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
	} else {
		window.awesomplete.filter = myFilter;
	}
    console.log("context walker finished");
}

function handleEnd() {
    if (currentValues.indexOf("end") >= 0) { 
		if (currentSeq.length == 0 || (currentSeq.length > 0 && seqIndex == currentSeq.length-1 || (currentSeq[currentSeq.length-1] == endObj && seqIndex == currentSeq.length-2))) {
            console.log(currentSeq);
        	window.done = true;
		}
		currentValues.splice(currentValues.indexOf("end"), 1);
		console.log("condition of variants concat: ");
		console.log(currentSeq.length > 0 && seqIndex < currentSeq.length-1);
		if (currentSeq.length > 0 && seqIndex < currentSeq.length-1) {
			//currentOperator = currentSeq[seqIndex + 1];
			currentValues = currentValues.concat(getVariants(currentSeq[seqIndex + 1]));
			//seqIndex++;
			console.log("currentValues after concat: " + currentValues);
		}
	}
	
}

function initContext() {
	currentOperator = mainObj;
	currentValues = getVariants(mainObj);
    currentSeq = [];
    seqIndex = -1;
	backspace = false;
	words = [];
	objs = [];
	objs.push(mainObj);
	newVariableBuffer = "";
	window.awesomplete.open();
    initAllowedChars();
    initTickImg();
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
	
	this.input.value += text;
	if (!(spaceExceptions.indexOf(text) >= 0)) {
		this.input.value += ' ';
	}
}

function colorizer(str) {
	var array = str.split(' ');
	var keyWords = ["click", "fill", "assert", ":", "with", "sec", "min", "h", "as"];
    var varPrefixes = ["_", "@", "$"];
	var inStr = false;
	var div = document.createElement('div');
	document.getElementById("written").appendChild(div);
	for (var i in array) {
		var color = "000000";
		if (inStr) {
			color = "00B000";
		}
		
		if (keyWords.indexOf(array[i]) >= 0) {
			color = "0000FF";
		} else if (array[i][0] == '"') {
			inStr = true;
			color = "00B000";
		} else if (array[i][0] == '#') {
			color = "990000";
		}
		var font = document.createElement("font");
		div.appendChild(font);
		font.setAttribute("color", color);
		if (varPrefixes.indexOf(array[i][0]) >= 0) {
			font.innerHTML = "<i>" + array[i] + " </i>";
		} else {
			font.innerHTML = array[i] + ' ';
		}
		
		if (array[i][array[i].length-1] == '"') {
			inStr = false;
		}
	}
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
			colorizer(inputStr);
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
        } else if (currentOperator == constStringObj) {
            var lw = getLastWord();
            if (lw[0] == '"' && lw[lw.length-1] == '"' && lw.length >= 2) {
				forceContextUpdate = true;
                window.input.value += ' ';
            }
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
        tickImg = document.createElement('img');
        tickImg.setAttribute("src", "tick.png");
        tickImg.setAttribute("width", "20");
        tickImg.setAttribute("height", "20");
        document.querySelector("div.awesomplete>div.awesomplete").appendChild(tickImg); 
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
	textfieldObj.variants = Object.keys(elementsJsonParam["TextField"]);
	buttonObj.variants = Object.keys(elementsJsonParam["Button"]);
	for (var i = 0; i < textfieldObj.variants.length; i++) {
		textfieldObj.variants[i] = "#" + textfieldObj.variants[i];
	}
	for (var i = 0; i < buttonObj.variants.length; i++) {
		buttonObj.variants[i] = "#" + buttonObj.variants[i];
	}
	elementsObj.variants = textfieldObj.variants.concat(buttonObj.variants);
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
	initContext();
	window.input.focus();
	window.awesomplete.list = currentValues;
	window.awesomplete.open();
}

