import $ from 'jquery';
import { Controller } from "stimulus";

export default class extends Controller {

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