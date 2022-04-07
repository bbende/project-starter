var gulp = require('gulp');
var gulpif = require('gulp-if');
var concat = require('gulp-concat');
var cleanCss = require('gulp-clean-css');
var sass = require('gulp-sass');
var uglify = require('gulp-uglify-es').default;
var tildeImporter = require('node-sass-tilde-importer');
var browserify = require('browserify');
var source = require('vinyl-source-stream');
var buffer = require('vinyl-buffer');
var babelify = require('babelify');
var argv = require('minimist')(process.argv.slice(2));

var srcDir = argv.srcDir;
var buildDir = argv.buildDir;
var outDir = argv.outDir;
var minifyFlag = argv.envName === 'dist';

// Copy stylesheets to the buildDir and use that as the source for other tasks
gulp.task('copy-stylesheets', function () {
    return gulp.src([
            srcDir + '/stylesheets/**/*',
            'node_modules/font-awesome/css/font-awesome.css',
            'node_modules/unpoly/unpoly.css'
        ])
        .pipe(gulp.dest(buildDir + '/stylesheets'));
});

// Copy stylesheets to the buildDir and use that as the source for other tasks
gulp.task('copy-javascript', function () {
    return gulp.src(srcDir + '/javascript/**/*')
        .pipe(gulp.dest(buildDir + '/javascript'));
});

// Copy fonts to the outDir, we skip buildDir since there is nothing to do to the fonts
gulp.task('copy-fonts', function () {
    return gulp.src([
        'node_modules/font-awesome/fonts/*'
      ])
        .pipe(gulp.dest(outDir + '/fonts'));
});

// The tilde-importer is required to resolve imports like ~bootstrap/scss/...
// The includePaths avoids the need to prefix all imports with the path to node_modules
gulp.task('compile-sass', gulp.series('copy-stylesheets', function() {
    return gulp.src(buildDir + '/stylesheets/**/*.scss')
        .pipe(sass({
            includePaths: [ './node_modules' ],
            importer: tildeImporter
        }).on('error', sass.logError))
        .pipe(gulp.dest(buildDir + '/stylesheets/'));
}));

// Pack any .css files in the buildDir into application.css in outDir/stylesheets
gulp.task('bundle-css', gulp.series('compile-sass' , function () {
    return gulp.src(buildDir + '/stylesheets/**/*.css')
        .pipe(concat('application.css'))
        .pipe(gulpif(minifyFlag, cleanCss()))
        .pipe(gulp.dest(outDir + '/stylesheets'));
}));

// Creates a bundle for application.js and conditionally calls minify based on dev or prod
gulp.task('bundle-javascript', gulp.series('copy-javascript', function() {
    return browserify( {
            debug: true,
            entries: [buildDir + '/javascript/application.js']
        })
        .transform(babelify, {
            presets: [ ['env', {'useBuiltIns' : 'usage'}] ],
            plugins: ["transform-runtime"]
        })
        .bundle()
        .on('error', function(e) { console.log(e) })
        .pipe(source('application.js'))
        .pipe(buffer())
        .pipe(gulpif(minifyFlag, uglify()))
        .pipe(gulp.dest(outDir + '/javascript'));
}));

gulp.task('default', gulp.parallel('copy-fonts', 'bundle-css', 'bundle-javascript'));

// Watches for changes and executes appropriate targets so we don't have to rebuild during development
gulp.task('watch', function() {
    gulp.watch(srcDir + '/stylesheets/**/*', gulp.series('bundle-css'));
    gulp.watch(srcDir + '/javascript/**/*', gulp.series('bundle-javascript'));
});