function toggle(aNode) {	
	if (aNode.parentNode.getAttribute("state") == "collapsed") {
		aNode.parentNode.setAttribute("state", "expanded");
		aNode.className = "exp";
						
		aNode.nextSibling.className = "expanded";
	}
	else {
		aNode.parentNode.setAttribute("state", "collapsed");
		aNode.className = "col";
								
		aNode.nextSibling.className = "collapsed";
	}	
}

function checkOpen(aNode) {
	if (aNode.parentNode.getAttribute("state") == "collapsed") {
		aNode.parentNode.setAttribute("state", "expanded");
		aNode.className = "exp";
						
		aNode.nextSibling.className = "expanded";
	}
}

function toggle_source(aNode) {
	var value = aNode.innerHTML;
	var pre = aNode.parentNode.nextSibling;
	
	if (value == "show") {				
		aNode.innerHTML = "hide";
		sh_highlight(pre);
		pre.style.display = "block";
	}
	else {
		pre.style.display = "none";
		aNode.innerHTML = "show";
	}
}

function sh_highlight(pre) {					
	var htmlClasses = sh_getClasses(pre);
	for (var j = 0; j < htmlClasses.length; j++) 
	{
		var htmlClass = htmlClasses[j].toLowerCase();
		if (htmlClass === 'sh_sourcecode') {
			continue;
		}
		if (htmlClass.substr(0, 3) === 'sh_') {
			var language = htmlClass.substring(3);
			if (language in sh_languages) {
				sh_highlightElement(pre, sh_languages[language]);
			}
			else if (typeof(prefix) === 'string' && typeof(suffix) === 'string') {
				sh_load(language, pre, prefix, suffix);
			}
			else {
				throw 'Found <pre> element with class="' + htmlClass + '", but no such language exists';
			}
			break;
		}
	}
}
