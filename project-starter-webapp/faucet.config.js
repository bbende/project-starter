let targetBaseDir = "./target/classes/static"
const path = require('path');

module.exports = {
    js: [{
        source: "./src/main/webapp/javascript/application.js",
        target: targetBaseDir + "/javascript/application.js"
    }],
    sass: [{
        source: "./src/main/webapp/stylesheets/application.scss",
        includePaths: ["./node_modules"],
        target: targetBaseDir + "/stylesheets/application.css"
    }],
    static: [{
        source: "./src/main/webapp/images",
        target: targetBaseDir + "/images"
    }],
    manifest: {
        target: "./target/classes/manifest.json",
        key: 'short',
        webRoot: targetBaseDir
    }
};