angular.module('rainbowApp', ['treeControl'])
    .controller('RainbowController', function () {

        var rainbow = this;

        rainbow.selectableProjects = [{
            id: 1,
            label: 'camel.json'
        }, {
            id: 2,
            label: 'test TODO'
        }, {
            id: 3,
            label: 'data-hierarchical TODO'
        }];

        rainbow.selectedProjectId = 0;

        rainbow.initializeProject = function () {
            var selectedProject = rainbow.searchSelectedProjectById(rainbow.selectedProjectId);

            if (rainbow.selectedProjectId != 0) {
                d3.json(selectedProject.label, function (error, resultJson) {
                    rainbow.getSelectableSizeMetrics(resultJson.meta);
                    rainbow.updateTree(resultJson.data);
                });
            } else {
                console.log("No project selected - reset all");
                rainbow.selectAll();
            }
        };

        rainbow.searchSelectedProjectById = function (id) {
            for (var i = 0; i < rainbow.selectableProjects.length; i++) {
                if (rainbow.selectableProjects[i].id == id) {
                    return rainbow.selectableProjects[i];
                }
            }

            return 0;
        };

        rainbow.selectableSizeMetrics = [];
        rainbow.selectedSizeMetric = 0;

        rainbow.getSelectableSizeMetrics = function (metaData) {
            rainbow.selectableSizeMetrics = [];
            for (var i = 0; i < metaData.metrics.length; i++) {
                var metric = {};
                // to keep 0 as default or not selected id
                metric.id = i + 1;
                metric.label = metaData.metrics[i];

                rainbow.selectableSizeMetrics.push(metric);
            }
        };

        rainbow.resetAll = function () {
            rainbow.selectableSizeMetrics = [];
            rainbow.selectedSizeMetric = 0;
        };

        rainbow.selectedPackage;

        rainbow.showSelected = function (node) {
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

        rainbow.updateTree = function (data) {
            var treeData = data;
            // TODO removeLeafNodes does not work
            //rainbow.removeLeafNodes(treeData);
            rainbow.dataForTheTree = treeData;
            rainbow.selectedPackage = treeData;
        };

        rainbow.showTreeNodeView = false;

        rainbow.toggleTreeNodeView = function () {
            rainbow.showTreeNodeView = !rainbow.showTreeNodeView;
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