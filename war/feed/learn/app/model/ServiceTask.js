//TODO(igu) move to its own package
Ext.define('RepairValuation.model.ServiceTask', {
    extend:'Ext.data.Model',
    
    fields: [
        {name: 'name', type: 'string', defaultValue: 'Unknown'}
    ],
    /*
    proxy:{
        type:'rest',
        url:'http://localhost:8080/feed/f.xml',
        reader:{
            type:'json',
            root:'task'
        }
    },
*/
    //hasMany: {model: 'ServiceLine', name: 'serviceLines'},
    //hasOne: {model: 'Claimant', name: 'claimant'}
    associations: [{ name: 'claimant', type: 'hasOne', model: 'RepairValuation.model.Claimant' }]
});
