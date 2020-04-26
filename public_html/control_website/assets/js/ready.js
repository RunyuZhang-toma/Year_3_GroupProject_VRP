/*

Filename: ready.css

This front-end file is prepared by Team 2018-10 of COMP 2043.GRP. University of Nottingham, Ningbo, China.
The supervisor of Team-10 is prof. Ruibin BAI.
The developer group consists six people, Runyu ZHANG, Qichen ZHANG, Yinglun LI, Huixing ZHANG, Zeyu ZHANG, and Yundan WANG.
The project is No.14 UAV Patrol System.

REFERENCE:

[1] Titile: Free Templates of Websites 
    Author: CSSMoban (Templates Home)
    Date: 2018
	 Code Version: 1.0
    License: MIT
	 Availability: http://www.cssmoban.com (accessed: Mar 1, 2019)

[2] Title: Websites Templates
    Author: Bootstrap
    Date: 2018
    Code Version: 4.0.0
    License: MIT (https://github.com/twbs/bootstrap/blob/master/LICENSE)
    Availability: https://github.com/twbs/bootstrap (accessed: Mar 7, 2019)

*/

$(function () {
	$('[data-toggle="tooltip"]').tooltip()
});

jQuery(document).ready(function(){
    jQuery('.scrollbar-inner').scrollbar();
});

$(document).ready(function(){

	var toggle_sidebar = false,
	toggle_topbar = false,
	nav_open = 0,
	topbar_open = 0;

	if(!toggle_sidebar) {
		$toggle = $('.sidenav-toggler');

		$toggle.click(function() {
			if (nav_open == 1){
				$('html').removeClass('nav_open');
				$toggle.removeClass('toggled');
				nav_open = 0;
			}  else {
				$('html').addClass('nav_open');
				$toggle.addClass('toggled');
				nav_open = 1;
			}
		});
		toggle_sidebar = true;
	}

	if(!toggle_topbar) {
		$topbar = $('.topbar-toggler');

		$topbar.click(function(){
			if (topbar_open == 1) {
				$('html').removeClass('topbar_open');
				$topbar.removeClass('toggled');
				topbar_open = 0;
			} else {
				$('html').addClass('topbar_open');
				$topbar.addClass('toggled');
				topbar_open = 1;
			}
		});
		toggle_topbar = true;
	}

//select all
$('[data-select="checkbox"]').change(function(){
	$target = $(this).attr('data-target');
	$($target).prop('checked', $(this).prop("checked"));
})

});