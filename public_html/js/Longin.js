/*

Filename: Login.js

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

/*function login(){
    var username = document.getElementById("ID").value;
    var password = document.getElementById("PASSWORD").value;
    if(username==""){
        $.jGrowl("Account field cannot be empty", { header: 'Faild to login' });
    }else if(password==""){
        $.jGrowl("Password field cannot be empty", { header: 'Faild to login' });
    }else{
        AjaxFunc();
    }
}*/

function login(){
    var username = document.getElementById("ID").value;
    var password = document.getElementById("PASSWORD").value;
    if(username==""){
        $.jGrowl("Account field cannot be empty", { header: 'Faild to login' });
    }else if(password==""){
        $.jGrowl("Password field cannot be empty", { header: 'Faild to login' });
    }else if(username!="team201810"){
        $.jGrowl("Incorret account or password", { header: 'Faild to login' });
    }else if(password!="UAVFlag85"){
        $.jGrowl("Incorret account or password", { header: 'Faild to login' });
    }else{
        window.location.href="control_website/index.html";
    }
}

function AjaxFunc()
{
    var username = document.getElementById("ID").value;
    var password = document.getElementById("PASSWORD").value;
    $.ajax({
        type: 'get',
        url: "",
        dataType: "json",
        data: {"username": username,"password": password},
        success: function (data) {

        },
        error: function (xhr, type) {
            console.log(xhr);
        }
    });
}
