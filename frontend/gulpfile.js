'use strict';

var gulp = require('gulp');
var path = require('path');
var sourceMaps = require('gulp-sourcemaps');
var watch = require('gulp-watch');

// чистим
var clean = require('gulp-rimraf');
gulp.task('clean', function () {
	gulp.src('./build', {read: false})
		.pipe(clean());
});


// компиляция less-файлов
var less = require('gulp-less'),
	LessPluginAutoPrefix = require('less-plugin-autoprefix'),
	autoPrefix = new LessPluginAutoPrefix({browsers: ["last 2 versions"]});

gulp.task('less', function () {
	gulp.src('./styles/less/**/main.less')
		.pipe(less({
			paths  : [path.join(__dirname, 'less', 'includes')],
			plugins: [autoPrefix]
		}))
		.pipe(gulp.dest('./styles/css'));
});

// компиляция coffee-script
var coffee = require('gulp-coffee');
gulp.task('coffee', function () {
	gulp.src('./app/**/*.coffee', {base: './webapp'})
		.pipe(sourceMaps.init())
		.pipe(coffee({bare: true}))
		.pipe(sourceMaps.write('./app'))
		.pipe(gulp.dest('./app'));
});


