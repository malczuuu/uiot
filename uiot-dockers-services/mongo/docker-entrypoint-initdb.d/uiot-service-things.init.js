db = db.getSiblingDB("uiot-service-things");

db.createCollection("things");

db.runCommand({
    createIndexes: "things",
    indexes: [{
        "name": "thing_unique",
        "key": {
            "roomUid": -1,
            "uid": -1
        },
        "unique": true
    }]
});
