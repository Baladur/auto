//GLOBAL VARIABLES
var currentKey = "";
var currentValues = [];
var currentOperator = {};
var isLexemOk = true;
var changeOperator = false;
var words = [];
var objs = [];
var backspace = false;
var declaration = false;
var newVariableBuffer = "";
var localVariables = [];
var globalVariables = [];
var methods = ['$timeout'];

var spaceExceptions = ["@", "#", "$", "_", '"'];

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



var globalObj = {
	word : "@",
	variants : globalVariables,
	next : endObj
};

var localObj = {
	word : "_",
	variants : localVariables,
	next : endObj
};

var methodObj = {
	word : "$",
	variants : methods,
	next : endObj
};

var constStringObj = {
	word : '"',
	variants : [],
	next : endObj
};

var stringObj = {
	fork : true,
	'@' : globalObj,
	'_' : localObj,
	'$' : methodObj,
	'"' : constStringObj
};

var assignObj = {
	word : " :",
	variants : [":"],
	next : stringObj
};

var forkNumberAssignObj = {
	fork : true,
	'0' : forkNumberAssignObj,
	'1' : forkNumberAssignObj,
	':' : assignObj
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



var fillObj = {
	word : "fill",
	variants : textfieldObj,
	next : stringObj
};

var equalObj = {
	word : " =",
	variants : ["="],
	next : stringObj
};

var moreObj = {
	word : " >",
	variants : [">"],
	next : stringObj
};

var lessObj = {
	word : " <",
	variants : ["<"],
	next : stringObj
};

var notEqualObj = {
	word : " !=",
	variants : ["!="],
	next : stringObj
};

var compareObj = {
	fork : true,
	'=' : equalObj,
	'>' : moreObj,
	'<' : lessObj,
	'!=' : notEqualObj
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

var assertObj = {
	word : "assert",
	variants : stringObj,
	next : compareObj
};

var mainObj = {
	fork : true,
	click : clickObj,
	fill : fillObj,
	'_' : newLocalVariableObj,
	'@' : newGlobalVariableObj,
	assert : assertForkObj
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
	if (obj == endObj) {
		return 0;
	}
	if (!('word' in obj)) {
		console.log(obj);
		return getVariants(obj);
	} else {
		return obj.word;
	}
}

function getVariants(obj) {
	var variants = [];
	if ('fork' in obj) {
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
	} else {
		var tempObj = obj;
		while ('variants' in tempObj) {
			tempObj = tempObj.variants;
		}
		//////alert("variants" + tempObj);
		return tempObj;
	}
}

function contextWalker() {
	//////alert("cw");
	//////alert(getLastWord());
	//////alert(currentValues.includes(getLastWord()));
	var excludingWordsLast = false;
	var lastWord = getLastWord();
	if (words.length > 0) {
		if (words[words.length-1] == lastWord[0] && currentValues.includes(lastWord.substr(1, lastWord.length))) {
			excludingWordsLast = true;
		}
	}
	if (currentValues.includes(getLastWord()) || excludingWordsLast || newVariableBuffer.length > 0) {
		
		changeOperator = false;
		if ('fork' in currentOperator) {
			currentOperator = currentOperator[getLastWord()];
		} else {
			currentOperator = currentOperator.next;
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
		////alert(words);
		////alert(objs);
		
		currentValues = [];
		
		if (currentOperator == endObj) {
			window.done = true;
			currentValues = [];
			
			//initContext();
			
		} else {
			
			currentValues = getVariants(currentOperator);
			//////alert("currentValues: " + currentValues);
		}
		window.awesomplete.filter = showAllFilter;
		//window.awesomplete.list = currentValues;
		//////alert(words);
	} else {
		window.awesomplete.filter = myFilter;
	}
	if (currentValues.includes(getLastWord())) {
		changeOperator = true;
	}
	
}

function initContext() {
	currentOperator = mainObj;
	currentValues = getVariants(mainObj);
	backspace = false;
	words = [];
	objs = [];
	objs.push(mainObj);
	newVariableBuffer = "";
	window.awesomplete.open();
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
	
	/*if (words.includes(splitted[splitted.length-1])) {
		this.input.value += splitted[splitted.length-1] + ' ';
	}*/
	
	this.input.value += text;
	if (!(spaceExceptions.includes(text))) {
		this.input.value += ' ';
	}
}
		
window.onkeydown = function(e) {
	var key = e.keyCode ? e.keyCode : e.which;
	var input = document.getElementById("countries");
	var inputStr = input.value;
	if (key == 8) {
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
					if (!(spaceExceptions.includes(words[words.length-1]))) {
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
					if (spaceExceptions.includes(words[words.length-1])) {
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
			
			////alert("oldLength = " + inputStr.length);
			////alert("newLength = " + newLength);
			input.value = input.value.substr(0, newLength + 1);
			
		}
		
		if (objs.length == 1) {
			newVariableBuffer = "";
		} else {
			//if (getVariants(objs[objs.length-2]).includes(getLastWord())) {
			if (objs.length - words.length == 2) {
				objs.splice(objs.length-1, 1);
				
			}
			
			
			
		}
		currentOperator = objs[objs.length-1];
		currentValues = getVariants(currentOperator);
	} else if (key == 13) {
		if (inputStr.match(/^\d+$/) != undefined) {
			inputStr += ":";
			window.done = true;
		}
		if (getLastWord()[0] == '"') {
			inputStr += '"';
		}
		
		if (window.done) {
			if (objs[1] == newLocalVariableObj ) {
				localVariables.push(newVariableBuffer);
			} else if (objs[1] == newGlobalVariableObj) {
				globalVariables.push(newVariableBuffer);
			}
			document.getElementById("written").innerHTML += "<p><i>" + inputStr + "\n" + "</i></p>";
			input.value = "";
			window.done = false;
			initContext();
			window.input.focus();
			window.awesomplete.list = currentValues;
			window.awesomplete.open();
		}
	}
	if (window.awesomplete.list.length == 1) {
		
		/*var splitted = this.input.value.split(' ');
		input.value = "";
		for (var i = 0; i < splitted.length - 1; i++) {
			this.input.value += splitted[i] + " ";
		}
		this.input.value += window.awesomplete.list[0] + " ";*/
		window.awesomplete.select();
	}
	setCaretPosition("countries", input.value.length);
	
}

function evaluate() {
	////////alert(1);
	if (!backspace) {
		var input = document.getElementById("countries");
		if (input.value.length == 0) {
			window.awesomplete.list = undefined;
		}
		var matchedCount = 0;
		var matchedHint = "";
		//////alert(currentValues);
		for (var h in currentValues) {
			//////alert("input: " + window.input.value);
			//////alert("hint: " + currentValues[h]);
			if (myFilter(currentValues[h], window.input.value)) {
				//////alert("yeah");
				matchedCount = matchedCount + 1;
				matchedHint = currentValues[h];
			}
		}
		//////alert(matchedCount);
		if (matchedCount == 1) {
			myReplace(matchedHint);
		} else if (matchedCount == 0) {
			if (currentOperator == newLocalVariableObj || currentOperator == newGlobalVariableObj) {
				if (window.input.value[window.input.value.length-1] == ' ') {
					newVariableBuffer = getLastWord();
				}
			}
		}
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
	
	
	
/*	
	if (input.value == "c") {
		input.value = "click ";
		setCaretPosition("countries", input.value.length);
		window.context = 1;
		window.awesomplete.list = ["#SEARCH_BTN", "#LOGIN_BTN"];
		window.context = "c-";
		
	} else if (input.value == "f") {
		input.value = "fill #";
		setCaretPosition("countries", input.value.length);
		window.awesomplete.list = ["#SEARCH_INPUT", "#USERNAME", "#PASSWORD"];
		window.context = "f-";
	}
	
	var s = input.value.replace(/\ $/,'').split(' ');
	if (s.length >= 2) {
		if (s[s.length-1].match(/#.+/) != undefined && window.context[window.context.length-1] != 'e') {
			window.context += "e";
		} else if (s.length == 3 && window.context == "f-e") {
			window.context += "-s";
		}
	}
	if (doDone) {
		if (window.context == "c-e"
	|| window.context == "f-e-s") {
		window.done = true;
	}
	}*/
	
	
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
	console.log(elementsJsonParam["TextField"]);
	textfieldObj.variants = Object.keys(elementsJsonParam["TextField"]);
	buttonObj.variants = Object.keys(elementsJsonParam["Button"]);
	for (var i = 0; i < textfieldObj.variants.length; i++) {
		textfieldObj.variants[i] = "#" + textfieldObj.variants[i];
	}
	for (var i = 0; i < buttonObj.variants.length; i++) {
		buttonObj.variants[i] = "#" + buttonObj.variants[i];
	}
	elementsObj.variants = textfieldObj.variants.concat(buttonObj.variants);
	console.log(elementsObj);
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
	var xmlDoc;
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
	}
	
}







window.onload = function(e) {
	console.log(configXml);
	addElementsJson("kinopoisk");
	//initObjs(elementsJson);
	
	window.input = document.getElementById("countries");
	window.input.oninput = evaluate;
	
	window.awesomplete = new Awesomplete(input, {
			autoFirst : true,
			minChars : "0",
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

