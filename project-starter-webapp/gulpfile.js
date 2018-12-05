var gulp = require('gulp');
var gulpif = require('gulp-if');
var concat = require('gulp-concat');
var minify = require('gulp-minify');
var cleanCss = require('gulp-clean-css');
var sass = require('gulp-sass');
var util = require('gulp-util');
var uglify = require('gulp-uglify');
var tildeImporter = require('node-sass-tilde-importer');
var browserify = require('browserify');
var source = require('vinyl-source-stream');
var buffer = require('vinyl-buffer');

var srcDir = util.env.srcDir;
var buildDir = util.env.buildDir;
var outDir = util.env.outDir;
var minifyFlag = util.env.envName === 'prod';

// Copy stylesheets to the buildDir and use that as the source for other tasks
gulp.task('copy-stylesheets', function () {
    return gulp.src(srcDir + '/stylesheets/**/*')
        .pipe(gulp.dest(buildDir + '/stylesheets'));
});

// Copy stylesheets to the buildDir and use that as the source for other tasks
gulp.task('copy-javascript', function () {
    return gulp.src(srcDir + '/javascript/**/*')
        .pipe(gulp.dest(buildDir + '/javascript'));
});

// The tilde-importer is required to resolve imports like ~bootstrap/scss/...
// The includePaths avoids the need to prefix all imports with the path to node_modules
gulp.task('compile-sass', ['copy-stylesheets'], function() {
    return gulp.src(buildDir + '/stylesheets/**/*.scss')
        .pipe(sass({
            includePaths: [ './node_modules' ],
            importer: tildeImporter
        }).on('error', sass.logError))
        .pipe(gulp.dest(buildDir + '/stylesheets/'));
});

// Pack any .css files in the buildDir into application.css in outDir/stylesheets
gulp.task('pack-css', ['compile-sass'] , function () {
    return gulp.src(buildDir + '/stylesheets/**/*.css')
        .pipe(concat('application.css'))
        .pipe(gulpif(minifyFlag, cleanCss()))
        .pipe(gulp.dest(outDir + '/stylesheets'));
});

// Creates a bundle for application.js and conditionally calls minify based on dev or prod
gulp.task('bundle-javascript', ['copy-javascript'], function() {
    return browserify({ entries: [buildDir + '/javascript/application.js'] })
        .bundle()
        .pipe(source('application.js'))
        .pipe(buffer())
        .pipe(gulpif(minifyFlag, uglify()))
        .pipe(gulp.dest(outDir + '/javascript'));
});

gulp.task('default', ['pack-css', 'bundle-javascript']);

// Watches for changes and executes appropriate targets so we don't have to rebuild during development
gulp.task('watch', function() {
    gulp.watch([
        srcDir + '/stylesheets/**/*',
        srcDir + '/javascript/**/*'
    ], ['pack-css', 'bundle-javascript']);
});