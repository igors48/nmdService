Ext.define('RepairValuation.controller.Controller', {
    extend: 'Ext.app.Controller',

    views: [
        'ServiceLineList'
    ],
    
    stores: [
        'ServiceLineStore',
        'ClaimantStore'
    ],
    
    init: function() {
        this.control({
            'viewport > panel': {
                render: this.onPanelRendered
            }
        });
    },

    onPanelRendered: function() {
        console.log('The panel was rendered');
    }
    
});