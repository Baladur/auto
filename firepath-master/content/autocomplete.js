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
var types = ["string", "int", "double", "boolean"];
var methods = ['$timeout'];
var forceContextUpdate = false;
var tickImg = undefined;
var allowedChars = [];
var spaceExceptions = ["@", "#", "$", "_", '"', '.'];

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
	word : "as",
	variants : types,
	next : endObj
};

var forkEndOrTypeObj = {
	fork : true,
	as : typeObj,
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
		next : endObj
	}
};

var stringObj = {
    word : ['@', '_', '$', '"', '#'],
	fork : true,
	'@' : globalObj,
	'_' : localObj,
	'$' : methodObj,
	'"' : constStringObj,
	'#' : elementTextObj
};

var assignObj = {
	word : " :",
	variants : [":"],
	next : stringObj
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
	word : ["=", "<", ">", "!="],
	variants : ["=", "<", ">", "!="],
	next : stringObj
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
	/*if (obj == endObj) {
		return 0;
	}*/
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
		variants = tempObj;
	}
	
	return variants;
}

function contextWalker() {
	//////alert("cw");
	//////alert(getLastWord());
	//////alert(currentValues.indexOf(getLastWord()));
	var excludingWordsLast = false;
	var lastWord = getLastWord();
	if (words.length > 0) {
		console.log(words);
		console.log(lastWord[0]);
		if (words[words.length-1] == lastWord[0] && currentValues.indexOf(lastWord.substr(1, lastWord.length)) >= 0) {
			excludingWordsLast = true;
		}
	}
	console.log(excludingWordsLast);
	if (currentValues.indexOf(getLastWord()) >= 0 || excludingWordsLast || forceContextUpdate) {
		excludingWordsLast = false;
		forceContextUpdate = false;
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
			window.done = false;
			currentValues = getVariants(currentOperator);
			handleEnd();
			//////alert("currentValues: " + currentValues);
		}
		window.awesomplete.filter = showAllFilter;
		//window.awesomplete.list = currentValues;
		//////alert(words);
        initAllowedChars();
	} else {
		window.awesomplete.filter = myFilter;
	}
	if (currentValues.indexOf(getLastWord()) >= 0) {
		changeOperator = true;
	}
    
	
}

function handleEnd() {
    if (currentValues.indexOf("end") >= 0) {
        window.done = true;
        currentValues.splice(currentValues.indexOf("end"), 1);
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
    handleTickImg();
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
	
	this.input.value += text;
    console.log("myreplace text = " + text);
	if (!(spaceExceptions.indexOf(text) >= 0)) {
        console.log("condition is true");
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
        console.log(inputChar);
        if ((allowedChars.indexOf(inputChar) < 0 && !((window.input.value.length == 0 || window.input.value.match(/^\d+$/) != undefined) && inputChar >= 1 && inputChar <= 9 ))
           || (getVisibleHints().count == 0 && window.awesomplete.filter == myFilter)) {
            console.log("visible hints : " + getVisibleHints().count);
            console.log("input char = " + inputChar);
            console.log("allowedChars = " + allowedChars);
            console.log("first condition = " + (allowedChars.indexOf(inputChar) < 0 && !((window.input.value.length == 0 || window.input.value.match(/^\d+$/) != undefined) && inputChar >= 1 && inputChar <= 9 )));
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

window.onkeypress = function(e) {
    excludeWrongChars(e);
}
		
window.onkeydown = function(e) {
	var key = e.keyCode ? e.keyCode : e.which;
	var input = window.input;
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
			
			////alert("oldLength = " + inputStr.length);
			////alert("newLength = " + newLength);
			input.value = input.value.substr(0, newLength + 1);
			
		}
		
		if (objs.length == 1) {
			newVariableBuffer = "";
		} else {
			//if (getVariants(objs[objs.length-2]).indexOf(getLastWord())) {
			if (objs.length - words.length == 2) {
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
				localVariables.push(newVariableBuffer);
			} else if (objs[1] == newGlobalVariableObj) {
				globalVariables.push(newVariableBuffer);
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
		if (matchedCount == 1) {
			myReplace(matchedHint);
		} else if (matchedCount == 0) {
			if (currentOperator == newLocalVariableObj || currentOperator == newGlobalVariableObj) {
				if (window.input.value[window.input.value.length-1] == ' ') {
					newVariableBuffer = getLastWord();
					forceContextUpdate = true;
				}
			} else if (currentOperator == constStringObj) {
				if (window.input.value.substr(window.input.value.length-2, window.input.value.length) == '" ') {
					forceContextUpdate = true;
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
	handleTickImg();
	
}

function handleTickImg() {
    if (window.done) {
        if (tickImg == undefined) {
            tickImg = document.createElement('img');
		    tickImg.setAttribute("src", "tick.png");
            tickImg.setAttribute("width", "20");
            tickImg.setAttribute("height", "20");
            document.querySelector("div.awesomplete>div.awesomplete").appendChild(tickImg);
        }
		
		
	} else {
        if (tickImg != undefined) {
            document.querySelector("div.awesomplete>div.awesomplete").removeChild(tickImg);
            tickImg = undefined;
        }
		
	}
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
	
	window.input = document.getElementById("mainInput");
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

