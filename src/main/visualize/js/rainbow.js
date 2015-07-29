angular.module('rainbowApp', ['treeControl'])
    .controller('RainbowController', function() {

        var rainbow = this;

        rainbow.selectableProjects = [{
            id: 1,
            label: 'camel'
        }, {
            id: 2,
            label: 'test'
        }, {
            id: 3,
            label: 'data-hierarchical'
        }];

        rainbow.selectedProject = 0;

        rainbow.initializeProject = function() {
            console.log(rainbow.selectedProject);

            if (rainbow.selectedProject != 0) {
                d3.json('camel.json', function(error, data) {
                    rainbow.getSelectableSizeMetrics(data);
                    rainbow.updateTree(data);
                });

            } else {
                console.log("No project selected - reset all");
                rainbow.selectAll();
            }
        };

        rainbow.selectableSizeMetrics = [];
        rainbow.selectedSizeMetric = 0;

        rainbow.getSelectableSizeMetrics = function() {
            // TODO get data from json
            rainbow.selectableSizeMetrics = [{
                id: 1,
                label: 'LOC'
            }, {
                id: 2,
                label: 'Fields'
            }, {
                id: 3,
                label: 'params'
            }];
        };

        rainbow.resetAll = function() {
            rainbow.selectableSizeMetrics = [];
            rainbow.selectedSizeMetric = 0;
        };

        rainbow.selectedPackage;

        rainbow.showSelected = function(node) {
            rainbow.selectedPackage = node;
        };

        rainbow.treeOptions = {
            nodeChildren: "children",
            dirSelectable: true,
            injectClasses: {
                ul: "a1",
                li: "a2",
                liSelected: "a7",
                iExpanded: "a3",
                iCollapsed: "a4",
                iLeaf: "a5",
                label: "a6",
                labelSelected: "a8"
            }
        };

        rainbow.dataForTheTree;

        rainbow.updateTree = function(data) {
            var treeData = data;
            // TODO removeLeafNodes does not work
            //rainbow.removeLeafNodes(treeData);
            rainbow.dataForTheTree = treeData;
        };

        //rainbow.removeLeafNodes = function(node) {
        //    for (var i = 0; i < node.children.length; i++) {
        //        if (node.children[i].children.length == 0) {
        //            //console.log("remove leaf" + node.children[i]);
        //            node.children.splice(i, 1);
        //        } else {
        //            rainbow.removeLeafNodes(node.children[i]);
        //        }
        //    }
        //};

    });