(function () {
    'use strict';

    angular
        .module('starter')
        .factory('commonUtil', commonUtil);

    commonUtil.$inject = ['logger', '$q', '$timeout', '$interval', '$location'];


    function commonUtil(logger, $q, $timeout, $interval, $location) {

        //var internalBaseUrl={url: 'http://127.0.0.1:8089/op'};
        var internalBaseUrl={url: 'send'};
        var service = {
            internalBaseUrl: internalBaseUrl,
            generateWSUrl: generateWSUrl,
            startsWith: startsWith,
            formatJson: formatJson,
            formatXml: formatXml
        };
        return service;



        function generateWSUrl() {
            if (internalBaseUrl.url && internalBaseUrl.url.length > 0) {
                return "ws" + internalBaseUrl.url.slice(internalBaseUrl.url.indexOf(':')) + "/notification";
            }
            var absUrl = $location.absUrl();
            //logger.log("$location.absUrl()=" + absUrl);
            //logger.log("$location.url()=" + $location.url());
            var rlt = absUrl.slice(absUrl.indexOf(':'), absUrl.lastIndexOf($location.url()));
            rlt = rlt.replace(/\/#/g, '');
            if (rlt.lastIndexOf('/') == rlt.length - 1) {
                rlt = rlt.substring(0, rlt.length - 1);
            }
            return "ws" + rlt + "/notification";
        }

        function startsWith(s, prefix) {
            return 0 == s.indexOf(prefix)
        }

        function formatJson(json, indentChars) {

            function repeat(s, count) {
                return new Array(count + 1).join(s);
            }

            var i = 0,
                il = 0,
                tab = (typeof indentChars !== "undefined") ? indentChars : "    ",
                newJson = "",
                indentLevel = 0,
                inString = false,
                currentChar = null;
            for (i = 0, il = json.length; i < il; i += 1) {
                currentChar = json.charAt(i);
                switch (currentChar) {
                    case '{':
                    case '[':
                        if (!inString) {
                            newJson += currentChar + "\n" + repeat(tab, indentLevel + 1);
                            indentLevel += 1;
                        } else {
                            newJson += currentChar;
                        }
                        break;
                    case '}':
                    case ']':
                        if (!inString) {
                            indentLevel -= 1;
                            newJson += "\n" + repeat(tab, indentLevel) + currentChar;
                        } else {
                            newJson += currentChar;
                        }
                        break;
                    case ',':
                        if (!inString) {
                            newJson += ",\n" + repeat(tab, indentLevel);
                        } else {
                            newJson += currentChar;
                        }
                        break;
                    case ':':
                        if (!inString) {
                            newJson += ": ";
                        } else {
                            newJson += currentChar;
                        }
                        break;
                    case ' ':
                    case "\n":
                    case "\t":
                        if (inString) {
                            newJson += currentChar;
                        }
                        break;
                    case '"':
                        if (i > 0 && json.charAt(i - 1) !== '\\') {
                            inString = !inString;
                        }
                        newJson += currentChar;
                        break;
                    default:
                        newJson += currentChar;
                        break;
                }
            }
            return newJson;
        }

        function formatXml(xml){
            return vkbeautify.xml(xml);
        }
    }


})();
