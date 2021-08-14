db = db.getSiblingDB("uiot-service-accounting");

db.createCollection("accounting");

db.runCommand({
    createIndexes: "rooms",
    indexes: [{
        "name": "uuid_unique",
        "key": {
            "uuid": -1
        },
        "unique": true
    }, {
        "name": "endTime",
        "key": {
            "endTime": -1
        }
    }, {
        "name": "roomUid_endTime",
        "key": {
            "roomUid": -1,
            "endTime": -1
        }
    }]
});
