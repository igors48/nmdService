Ext.define('RepairValuation.model.Claimant', {
    extend:'Ext.data.Model',
    fields:[
        { name: 'name', type: 'string' }/*, 'phone', 'mobile', 'email', 'address1', 'address2', 'postalCode', 'city'*/
    ],
    
    proxy:{
        type:'rest',
        //TODO(igu) remove url construction from here
        url:'http://localhost:8080/feed/f.xml',
        reader:{
            type:'json',
            root:'claimant'
        }
    }

});
