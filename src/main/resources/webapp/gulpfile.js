var gulp = require('gulp');
var del = require('del');
var rename = require("gulp-rename");
gulp.task('default', [
    'copy-dist'
], function(cb){});

var libList=[
    'bower_components/bootstrap/dist/js/bootstrap.min.js',
    'bower_components/angular-bootstrap/ui-bootstrap-tpls.min.js',
    'bower_components/font-awesome/css/font-awesome.min.css',
    'bower_components/font-awesome/fonts/**',
    'bower_components/font-awesome/fonts/*',
    'bower_components/bootstrap/dist/css/bootstrap.min.css',
    'bower_components/jquery/dist/jquery.min.js',
    'bower_components/angular/angular.min.js'
];

gulp.task('clean-bower', function(cb){
    del('bower_components/**', {force:true});
});

gulp.task('copy-dist', function (cb) {
    for(var i=0;i<libList.length;i++){
        if(-1==libList[i].indexOf('*')){
            gulp.src('downloaded/'+libList[i])
                .pipe(rename(libList[i]))
                .pipe(gulp.dest('./'));
        }else{
            gulp.src('downloaded/'+libList[i])
                .pipe(gulp.dest(libList[i].substring(0, libList[i].lastIndexOf(('/')))+'/'));
        }

    }
});

