/**
 * Created by Chen on 2016/4/20.
 */
(function () {
    'use strict';

    angular
        .module('starter')
        .controller('MainController', MainController);

    MainController.$inject = ['$http', '$scope', 'commonUtil', 'logger', '$interval', '$timeout', '$location'];
    function MainController($http, $scope, commonUtil, logger, $interval, $timeout, $location) {
        var vm = this;
        vm.requestProcessing=false;

        vm.id = $location.search().id;
        vm.cmd="";

        vm.onCmd=function(){
            if(""==vm.cmd || undefined==vm.cmd || ""==vm.id || undefined==vm.id){
                alert("cmd is empty!");
                return;
            }

            vm.requestProcessing=true;
                $http({
                    method: 'post',
                    url: commonUtil.internalBaseUrl.url,
                    //url:'http://www.mobisoftwarestudio.com',
                    //url:'https://api.shanbay.com/bdc/search/',
                    //url:'http://127.0.0.1:8080/',
                    //headers:{'Content-Type': 'application/json, text/plain, */*', 'Access-Control-Allow-Origin':'*'},
                    params: {
                        'cmd': vm.cmd,
                        'id': vm.id
                    }
                })
                .then(function(rsp){
                    logger.debug("rsp:"+JSON.stringify(rsp, null, 2));
                    //window.alert("OK");
                    vm.requestProcessing=false;
                    alert("cmd sent.");
                })
                .catch(function(rsp){
                    var error=JSON.stringify(rsp, null, 2);
                    logger.error("rsp:"+error);
                    //window.alert("Error:\n"+error);
                    vm.requestProcessing=false;
                    alert("cmd send failed, Error:\n"+error);
                });
        };
    }
})();

