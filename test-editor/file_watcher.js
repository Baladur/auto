process.on("message", function(message) { 
    var fs = require('fs');
    console.log("in child process");
    fs.watchFile(elementsJsonPath, function(curr, prev) {
        console.log("blabla");
        if (curr.mtime.getTime() - prev.mtime.getTime() > 0) {
            console.log(elementsJsonPath);
            scriptLoader([elementsJsonPath], function() {
                console.log("loading script: " + elementsJsonPath)
                initObjs(elementsJson);
            });
        }
    });
});