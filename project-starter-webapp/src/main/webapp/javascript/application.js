import './load-jquery';
import 'rails-ujs';

import Turbolinks from "turbolinks";
Turbolinks.start();

import { Application } from "stimulus";
import NavController from "./controllers/nav_controller";

const application = Application.start();
application.register("nav", NavController);

import 'bootstrap-material-design'
