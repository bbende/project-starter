var gulp = require('gulp');
var gulpif = require('gulp-if');
var concat = require('gulp-concat');
var minify = require('gulp-minify');
var cleanCss = require('gulp-clean-css');
var sass = require('gulp-sass');
var util = require('gulp-util');
var tildeImporter = require('node-sass-tilde-importer');

// Copy resources to the buildDir and use that as the source for other tasks
gulp.task('copy-stylesheets', function () {
    var srcDir = util.env.srcDir;
    var buildDir = util.env.buildDir;

    return gulp.src(srcDir + '/stylesheets/**/*')
        .pipe(gulp.dest(buildDir + '/stylesheets'));
});

// The tilde-importer is required to resolve imports like ~bootstrap/scss/...
// The includePaths avoids the need to prefix all imports with the path to node_modules
gulp.task('compile-sass', ['copy-stylesheets'], function() {
    var buildDir = util.env.buildDir;

    return gulp.src(buildDir + '/stylesheets/**/*.scss')
        .pipe(sass({
            includePaths: [ './node_modules' ],
            importer: tildeImporter
        }).on('error', sass.logError))
        .pipe(gulp.dest(buildDir + '/stylesheets/'));
});

// Pack any .css files in the buildDir into application.css in outDir/stylesheets
gulp.task('pack-css', ['compile-sass'] , function () {
    var buildDir = util.env.buildDir;
    var outDir = util.env.outDir;
    var minifyFlag = util.env.envName === 'prod';

    return gulp.src(buildDir + '/stylesheets/**/*.css')
        .pipe(concat('application.css'))
        .pipe(gulpif(minifyFlag, cleanCss()))
        .pipe(gulp.dest(outDir + '/stylesheets'));
});

gulp.task('copy-external-javascript', function () {
    var srcDir = util.env.srcDir;
    var buildDir = util.env.buildDir;

    return gulp.src(srcDir + '/stylesheets/**/*')
        .pipe(gulp.dest(buildDir + '/stylesheets'));
});

gulp.task('default',
    [
        'pack-css'
    ]);

gulp.task('watch', function() {
    var srcDir = util.env.srcDir;
    gulp.watch([srcDir + '/stylesheets/**/*'], ['pack-css']);
});