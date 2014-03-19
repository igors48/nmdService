Ext.define('RepairValuation.model.ServiceLine', {
    extend:'Ext.data.Model',
    //TODO(igu) add types and other options
    fields:[
        { name: 'description' }, 
        { name: 'categoryName' },
        { name: 'task', defaultValue: 'Repair' },
        { name: 'servicePartner', defaultValue: 'aaa@aaa.com' }
    ],
    
    proxy:{
        type:'rest',
        //TODO(igu) remove url construction from here
        url:'http://localhost:8080/feed/f.xml',
        reader:{
            type:'json',
            root:'serviceLines'
        }
    }

});

