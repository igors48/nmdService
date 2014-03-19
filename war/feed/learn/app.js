//{ "task" : [ {"claimant":{"name":"t t"},"serviceLines":[{"description":"Samsung PS43F4500AWXXU","categoryName":"TVs"},{"description":"Goji Tinchy Stryder: On Cloud 9 On-ear headphones","categoryName":"Headphones"}]} ] }



Ext.application({
    requires: ['Ext.container.Viewport'],
    name: 'AM',

    appFolder: 'app',

    launch: function() {
        Ext.create('Ext.container.Viewport', {
            layout: 'fit',
            items: [
                {
                    xtype: 'serviceLineList',
                    title: 'Users',
                }
            ]
        });
    }
});


