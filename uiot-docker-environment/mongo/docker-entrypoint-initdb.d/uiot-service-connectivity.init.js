db = db.getSiblingDB("uiot-service-connectivity");

db.createCollection("connectivity");

db.runCommand({
    createIndexes: "connectivity",
    indexes: [{
        "name": "roomUid_thingUid_unique",
        "key": {
            "roomUid": -1,
            "thingUid": -1
        },
        "unique": true
    }]
});
