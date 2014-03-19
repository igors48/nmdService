Ext.define('RepairValuation.view.ClaimantView' ,{
    extend: 'Ext.grid.Panel',
    alias: 'widget.claimantView',

    title: 'Claimant',

    store: 'ServiceLineStore',
    
    initComponent: function() {

        this.columns = [
            {header: 'Description',  dataIndex: 'description',  flex: 1},
            {header: 'Category',  dataIndex: 'categoryName',  flex: 1},
        ];

        this.callParent(arguments);
    }
});