import { Controller } from "stimulus";

export default class extends Controller {

    initialize() {
        // This needs to be called every time body is replaced with new content and since this controller
        // is for the nav which is part of the layout of every page, we can call this here instead of application.js
        $(document).ready(function() { $('body').bootstrapMaterialDesign(); });
    }

    toggle() {
        if ($('#navbarSide').hasClass('navbar-side-reveal')) {
            $('#navbarSide').removeClass('navbar-side-reveal');
            $('main').removeClass('push-main');
        } else {
            $('#navbarSide').addClass('navbar-side-reveal');
            $('main').addClass('push-main');
        }
    }

}