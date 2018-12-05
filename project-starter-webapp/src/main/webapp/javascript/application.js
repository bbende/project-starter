require('bootstrap');
require("rails-ujs");

let Turbolinks = require("turbolinks");
Turbolinks.start();

let $ = require('jquery')

$(document).ready(function() {
    // Toggle navbarSide when button is clicked
    $('#navbarSideButton').on('click', function() {
        if ( $('#navbarSide').hasClass('navbar-side-reveal') ) {
            $('#navbarSide').removeClass('navbar-side-reveal');
            $('main').removeClass('push-main');
        } else {
            $('#navbarSide').addClass('navbar-side-reveal');
            $('main').addClass('push-main');
        }
    });
});