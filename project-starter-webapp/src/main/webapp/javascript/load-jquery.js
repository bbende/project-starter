// Exposes jQuery and Popper to other libraries like bootstrap-material
// Must be first import in application.js, see - https://stackoverflow.com/a/51271892

window.$ = window.jQuery = require('jquery');
window.Popper = require('popper.js');
