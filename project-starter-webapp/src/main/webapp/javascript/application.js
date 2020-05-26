import './load-jquery';
import 'unpoly';

import { Application } from "stimulus";
import AppController from "./controllers/app_controller";
import NavController from "./controllers/nav_controller";

const application = Application.start();
application.register("app", AppController);
application.register("nav", NavController);

import 'bootstrap'
