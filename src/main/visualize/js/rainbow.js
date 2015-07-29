var sort_by = function(field, reverse, primer){

    var key = primer ?
        function(x) {return primer(x[field])} :
        function(x) {return x[field]};

    reverse = !reverse ? 1 : -1;

    return function (a, b) {
        return a = key(a), b = key(b), reverse * ((a > b) - (b > a));
    }
};

var scale = 1.3;
var radius = function (d) {
    var size = 0;
    if (d.statistics != null && d.statistics.methods != null) {
        size = scale * (1 + Math.pow(d.statistics.methods + d.statistics.fields, 2 / 5));
    } else if (d.children != null && d.children.length > 0) {
        size = 3.5;
    }
    if (size == null || size == 0) {
        size = 1;
    }
    return size;
};

angular.module('rainbowApp', ['treeControl'])
    .controller('RainbowController', function () {

        var rainbow = this;

        rainbow.visualize = false;
        rainbow.myFlower = null;

        rainbow.toggleVisualization = function () {
            rainbow.visualize = !rainbow.visualize;

            if (rainbow.visualize) {
                rainbow.myFlower = new CodeFlower("#visualization", 1000, 1000);
                rainbow.myFlower.update(rainbow.selectedPackage);
            } else {
                rainbow.myFlower = null;
            }
        };


        rainbow.selectableProjects = [{
            id: 1,
            label: 'examples/camel.json'
        }, {
            id: 2,
            label: 'examples/camel-full.json'
        }, {
            id: 3,
            label: 'examples/lmsapp.json'
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

            rainbow.updateTableView(node);
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
            // TODO removeLeafNodes does not work
            //rainbow.removeLeafNodes(treeData);
            rainbow.dataForTheTree = data;
            rainbow.showSelected(data);
        };

        rainbow.addFullyQualifiedName = function (node, path) {
            node.name = path + "/" + node.name;

            for(var i = 0; i < node.children.length; i++) {
                rainbow.addFullyQualifiedName(node.children[i], path + "/" + node.name);
            }
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

        rainbow.tableViewData = [];

        rainbow.updateTableView = function (node) {
            rainbow.tableViewData = [];

            rainbow.updateTableViewRecursive(node, "");
            rainbow.tableViewData.sort(sort_by('color', false, parseInt));
        };

        rainbow.updateTableViewRecursive = function (node, path) {
            if (node.color != null && node.children.length == 0) {
                var copyNode = JSON.parse(JSON.stringify(node));
                copyNode.name = path + "/" + node.name;
                rainbow.tableViewData.push(copyNode);
            }

            for(var i = 0; i < node.children.length; i++) {
                rainbow.updateTableViewRecursive(node.children[i], path + "/" + node.name);
            }
        };

    });