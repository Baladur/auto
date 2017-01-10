// Called once when the dialog displays
function onLoad() {
  // Use the arguments passed to us by the caller
  document.getElementById("elementPath").value = window.arguments[0].value;
 
}

// Called once if and only if the user clicks OK
function onOK() {
   // Return the changed arguments.
   // Notice if user clicks cancel, window.arguments[0].out remains null
   // because this function is never called
	var file = Components.classes["@mozilla.org/file/local;1"].
                            createInstance(Components.interfaces.nsILocalFile);

    file.initWithPath("C:\\Program Files (x86)\\Mozilla Firefox\\browser\\extensions\\uat.jar");
	var oProcess = Components.classes["@mozilla.org/process/util;1"].
                                        createInstance(Components.interfaces.nsIProcess);
										alert(oProcess);
    oProcess.init(file);
	var args = [document.getElementById("elementClass").value,
	document.getElementById("elementName").value,
	"\"xpath" + document.getElementById("elementPath").value + "\"",
	document.getElementById("elementDir").value,
	document.getElementById("elementPackage").value];
	alert("\"xpath" + document.getElementById("elementPath").value + "\"");
	oProcess.run(false, args, 5);
	return args;
	
}
