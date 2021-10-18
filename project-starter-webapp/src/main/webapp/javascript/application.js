import './load-jquery';

import 'unpoly/unpoly.es5';
import 'unpoly/unpoly-migrate';
up.log.enable()

import { Application } from "@hotwired/stimulus";
import AppController from "./controllers/app_controller";
import NavController from "./controllers/nav_controller";

const application = Application.start();
application.register("app", AppController);
application.register("nav", NavController);

import 'bootstrap'
