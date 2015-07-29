angular.module('rainbowApp', [])
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
                // TODO get json data
                var data;

                rainbow.getSelectableSizeMetrics(data);
            } else {
                console.log("No project selected - reset all");
                rainbow.selectAll();
            }
        };

        rainbow.getSelectableSizeMetrics = function() {
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

        rainbow.selectableSizeMetrics = [];

        rainbow.selectedSizeMetric = 0;


    });