var tabIndex = 0;
//Here is logic of working with tabs
function openNewTab() {
	console.log("opening new tab");
	var tab = document.createElement("div");
	tab.setAttribute("class", "tab-item");
	var tabSpan = document.createElement("span");
	tabSpan.setAttribute("class", "icon icon-cancel icon-close-tab");
	tabSpan.onclick = closeTab;
	tab.innerHTML += "Tab " + tabIndex++;
	tab.onclick = chooseTab;
	tab.appendChild(tabSpan);
	//tab.innerHTML += "Tab";
	document.getElementById("tabs").insertBefore(tab, document.getElementById("tabPlus"));
	tab.click();
}

function closeTab(event) {
	console.log("closing tab");
//	var tabs = document.querySelectorAll(".tab-item");
//	console.log(tabs);
//	if (tabs.length > 1) {
//		tabs[tabs.indexOf(event.target)-1].setAttribute("class", "tab-item active");
//	}
	document.getElementById("tabs").removeChild(event.target.parentElement);
	
}

function chooseTab(event) {
	console.log("choosing tab");
	var tabs = document.querySelectorAll(".tab-item");
	console.log(tabs);
	for (var t of tabs) {
		t.setAttribute("class", "tab-item");
	}
	event.target.setAttribute("class", "tab-item active");
}