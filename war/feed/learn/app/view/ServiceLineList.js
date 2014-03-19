Ext.define('RepairValuation.view.ServiceLineList' ,{
    extend: 'Ext.grid.Panel',
    
    requires: [
        'Ext.selection.CellModel',
        'Ext.grid.*',
        'Ext.data.*',
        'Ext.util.*',
        'Ext.form.*'
    ],
    
    xtype: 'cell-editing',
    
    alias: 'widget.serviceLineList',

    title: 'Claim lines',

    store: 'ServiceLineStore',
    
    initComponent: function() {
        this.cellEditing = new Ext.grid.plugin.CellEditing({
            clicksToEdit: 1
        });
        
        this.plugins = [this.cellEditing];
        
        this.columns = [
            { header: 'Description', dataIndex: 'description', flex: 3 },
            { header: 'Category', dataIndex: 'categoryName', flex: 2 },
            { 
                header: 'Task',  
                dataIndex: 'task',  
                flex: 1, 
                editor: new Ext.form.field.ComboBox({
                    typeAhead: true,
                    triggerAction: 'all',
                    store: [
                        [ 'Repair', 'Repair' ],
                        [ 'Valuation', 'Valuation' ]
                    ]
                })
            },
            { 
                header: 'Service partner', 
                dataIndex: 'servicePartner', 
                flex: 1, 
                editor: new Ext.form.field.ComboBox({
                    typeAhead: true,
                    triggerAction: 'all',
                    store: [
                        [ 'aaa@aaa.com', 'aaa@aaa.com' ],
                        [ 'bbb@bbb.com', 'bbb@bbb.com' ]
                    ]
                })
            },
        ];

        this.selModel = {
            selType: 'cellmodel'
        },
        
        this.callParent(arguments);
    }
});