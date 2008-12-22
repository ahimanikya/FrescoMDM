
/*
 * Scripts related to Tree in By Record screen
 */ 

function getByRecordData () {
    var domainSearchObj = {name:"Person", attributes:[{EUID:"000-000-000"}]};
    RelationshipHandler.searchDomainRelationshipsByRecord(domainSearchObj, getByRecordDataCB);
}

function getByRecordDataCB (data) {
    alert(data);
}