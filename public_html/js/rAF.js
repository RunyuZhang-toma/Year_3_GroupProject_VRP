/*

Filename: rAF.css

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

// http://paulirish.com/2011/requestanimationframe-for-smart-animating/
// http://my.opera.com/emoller/blog/2011/12/20/requestanimationframe-for-smart-er-animating

// requestAnimationFrame polyfill by Erik Möller. fixes from Paul Irish and Tino Zijdel

// MIT license

(function() {
    var lastTime = 0;
    var vendors = ['ms', 'moz', 'webkit', 'o'];
    for(var x = 0; x < vendors.length && !window.requestAnimationFrame; ++x) {
        window.requestAnimationFrame = window[vendors[x]+'RequestAnimationFrame'];
        window.cancelAnimationFrame = window[vendors[x]+'CancelAnimationFrame']
            || window[vendors[x]+'CancelRequestAnimationFrame'];
    }

    if (!window.requestAnimationFrame)
        window.requestAnimationFrame = function(callback, element) {
            var currTime = new Date().getTime();
            var timeToCall = Math.max(0, 16 - (currTime - lastTime));
            var id = window.setTimeout(function() { callback(currTime + timeToCall); },
                timeToCall);
            lastTime = currTime + timeToCall;
            return id;
        };

    if (!window.cancelAnimationFrame)
        window.cancelAnimationFrame = function(id) {
            clearTimeout(id);
        };
}());