db = db.getSiblingDB("uiot-service-rule");

db.createCollection("rules");

db.runCommand({
    createIndexes: "rules",
    indexes: [{
        "name": "rule_unique",
        "key": {
            "roomUid": -1,
            "uid": -1
        },
        "unique": true
    }]
});
