db = db.getSiblingDB("uiot-service-history");

db.createCollection("thing_events");

db.runCommand({
    createIndexes: "thing_events",
    indexes: [{
        "name": "uuid_unique",
        "key": {
            "uuid": -1
        },
        "unique": true
    }, {
        "name": "roomUid",
        "key": {
            "roomUid": -1
        }
    }, {
        "name": "roomUid_thingUid",
        "key": {
            "roomUid": -1,
            "thingUid": -1
        }
    }, {
        "name": "roomUid_thingUid_time",
        "key": {
            "roomUid": -1,
            "thingUid": -1,
            "time": -1
        }
    }, {
        "name": "roomUid_thingUid_arrivalTime",
        "key": {
            "roomUid": -1,
            "thingUid": -1,
            "arrivalTime": -1
        }
    }]
});
