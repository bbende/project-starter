// jQuery
import {$,jQuery} from 'jquery';
window.$ = $;
window.jQuery = jQuery;

// Bootstrap
// Use the bundle which already includes popper and avoids issue with babel loading popper
// Error: Couldn't resolve extends clause of ./.config/babel.config
// See: https://github.com/twbs/bootstrap/issues/23694
import 'bootstrap/dist/js/bootstrap.bundle'

// Unpoly
import 'unpoly/unpoly.es5';
up.log.enable()
