(function() {
	
	/** ****************************************** **/
	/** MODIFY SPECIFIC CERTIFICATION INFORMATIONS **/
	
	var hash = "f3d1d0d92fa60c8e87bd1f3c0e847334e8ab69a7524485057460fb23b36f15dc";
	var widthLogo = "100px";
	var descriptionLogoFontFamily="Montserrat"; //Font from Google Fonts >> https://fonts.googleapis.com/css?family=Montserrat
	var descriptionLogoFontSize="8px";
	
	var certificationServer = "http://192.168.1.37:8080/";
	/** ****************************************** **/
	/** ****************************************** **/

	var scriptName = "byochain.js";
	var jQuery;
	var jqueryPath = "http://ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js";
	var jqueryVersion = "1.10.2";
	var scriptTag;

	/** ****** Get reference to self (scriptTag) ******** */
	var allScripts = document.getElementsByTagName('script');
	var targetScripts = [];
	for ( var i in allScripts) {
		var name = allScripts[i].src
		if (name && name.indexOf(scriptName) > 0)
			targetScripts.push(allScripts[i]);
	}

	scriptTag = targetScripts[targetScripts.length - 1];

	/** ****** helper function to load external scripts ******** */
	function loadScript(src, onLoad) {
		var script_tag = document.createElement('script');
		script_tag.setAttribute("type", "text/javascript");
		script_tag.setAttribute("src", src);

		if (script_tag.readyState) {
			script_tag.onreadystatechange = function() {
				if (this.readyState == 'complete'
						|| this.readyState == 'loaded') {
					onLoad();
				}
			};
		} else {
			script_tag.onload = onLoad;
		}
		(document.getElementsByTagName("head")[0] || document.documentElement)
				.appendChild(script_tag);
	}

	/** ****** helper function to load external css ******** */
	function loadCss(href) {
		var link_tag = document.createElement('link');
		link_tag.setAttribute("type", "text/css");
		link_tag.setAttribute("rel", "stylesheet");
		link_tag.setAttribute("href", href);
		(document.getElementsByTagName("head")[0] || document.documentElement)
				.appendChild(link_tag);
	}

	/** ****** load jquery into 'jQuery' variable then call main ******* */
	if (window.jQuery === undefined
			|| window.jQuery.fn.jquery !== jqueryVersion) {
		loadScript(jqueryPath, initjQuery);
	} else {
		initjQuery();
	}

	function initjQuery() {
		jQuery = window.jQuery.noConflict(true);
		main();
	}
	
	/** ****** starting point for your widget ******* */
	function main() {
		jQuery(document).ready(function($) {
			var certificationLogoURL;
			var token;
			$.ajax({
				url : certificationServer+"api/v1/certifications/token/" + hash,
				type : "GET",
				dataType : "jsonp",
				timeout: 15000,
				success : function(result) {
					if (result.code == 5000) {
					token = result.data;
					$.ajax({
						url : certificationServer+"api/v1/certifications/check/"
							+ hash
							+ "/"
							+ token,
						type : "GET",
						dataType : "jsonp",
						timeout: 15000,
						success : function(result) {
							certificationLogoURL = result.data.logo;
							// console.log(result);
							if (result.code == 1010) {
								var styles = {fontFamily : descriptionLogoFontFamily,fontSize : descriptionLogoFontSize};
								$('#byochain').css(styles);
								$('<style />')
									.html("@import url('https://fonts.googleapis.com/css?family="+descriptionLogoFontFamily+"');")
									.appendTo($('#byochain'));
								$('<a />')
									.attr("href",certificationServer+"api/v1/certifications/check/"+ hash + "/" + token)
									.attr("target","_blank")
									.attr("id","checkCertification")
									.appendTo($('#byochain'));
								$('<img />')
									.attr('src',certificationLogoURL)
									.attr('title',"Certification Logo")
									.attr('alt',"Certification Logo")
									.width(widthLogo)
									.appendTo($('#checkCertification'));
								$('<p />')
									.html("<b>"
											+ result.message
											+ "</b><br>hash: <b>"
											+ hash
											+ "</b><br>"
											+ "token: <b>"
											+ token
											+ "</b>"
									)
									.appendTo($('#byochain'));
								} else if (result.code == 1011 || result.code == 1013) {
									var styles = { fontFamily : descriptionLogoFontFamily, fontSize : descriptionLogoFontSize };
									$('#byochain').css(styles);
									$('<style />')
										.html("@import url('https://fonts.googleapis.com/css?family="+descriptionLogoFontFamily+"');")
										.appendTo($('#byochain'));
									$('<img />')
										.attr('src',certificationLogoURL)
										.attr('title',"Certification Logo")
										.attr('alt',"Certification Logo")
										.width(widthLogo)
										.css('opacity','0.1')
										.appendTo($('#byochain'));
									$('<p />')
										.html("<b>"
												+ result.message
												+ "</b><br>hash: <b>"
												+ hash
												+ "</b>"
										)
										.appendTo($('#byochain'));
								} // end if
							} //end success function
						})//end ajax
						.fail(function (jqXHR, textStatus, errorThrown) {
							var styles = { fontFamily : descriptionLogoFontFamily, fontSize : descriptionLogoFontSize };
							$('#byochain').css(styles);
							$('<style />')
								.html("@import url('https://fonts.googleapis.com/css?family="+descriptionLogoFontFamily+"');")
								.appendTo($('#byochain'));
							$('<p />')
								.html("<b>"
										+ "<b>BYOChain Certification Server error</b> > " + errorThrown + " > Status : " + textStatus + " > Server URL : " + certificationServer
								)
								.appendTo($('#byochain'));
						});
					}// end if result code 5000
					else {
						var styles = { fontFamily : descriptionLogoFontFamily, fontSize : descriptionLogoFontSize };
						$('#byochain').css(styles);
						$('<style />')
							.html("@import url('https://fonts.googleapis.com/css?family="+descriptionLogoFontFamily+"');")
							.appendTo($('#byochain'));
						$('<p />')
							.html("<b>"
									+ result.message
									+ "</b><br>hash: <b>"
									+ hash
									+ "</b>"
							)
							.appendTo($('#byochain'));
					}//else
				}// end success function
			}) //end ajax
			.fail(function (jqXHR, textStatus, errorThrown) {
				var styles = { fontFamily : descriptionLogoFontFamily, fontSize : descriptionLogoFontSize };
				$('#byochain').css(styles);
				$('<style />')
					.html("@import url('https://fonts.googleapis.com/css?family="+descriptionLogoFontFamily+"');")
					.appendTo($('#byochain'));
				$('<p />')
					.html("<b>"
							+ "<b>BYOChain Certification Server error</b> > " + errorThrown + " > Status : " + textStatus + " > Server URL : " + certificationServer
					)
					.appendTo($('#byochain'));
			});
		}); //end ready
	}//end main
})();