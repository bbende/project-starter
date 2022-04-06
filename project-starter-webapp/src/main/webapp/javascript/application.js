// Latest Bootstrap no longer requires jQuery
//import './load-jquery';

// Bootstrap
// Use the bundle which already includes popperjs and avoids issue with babel loading popper
// Error: Couldn't resolve extends clause of ./.config/babel.config
// See: https://github.com/twbs/bootstrap/issues/23694
import 'bootstrap/dist/js/bootstrap.bundle'

// Unpoly
import 'unpoly/unpoly.es5';
up.log.enable()

// Stimulus
import { Application } from "@hotwired/stimulus";
import AppController from "./controllers/app_controller";

const application = Application.start();
application.register("app", AppController);

