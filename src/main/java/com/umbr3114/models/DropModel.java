package com.umbr3114.models;

import org.bson.types.ObjectId;
import org.mongojack.MongoCollection;

import java.util.List;
import java.util.Objects;

@MongoCollection(name = "drops")
public class DropModel {
    public String title;
    public String topic;
    public ObjectId _id;
    public String owner;
    public List<ModeratorModel> moderators;


    public static class ModeratorModel {
        public String modName;
        public String userId;

        public ModeratorModel() {}
        public ModeratorModel(String modName, String userId) {
            this.modName = modName;
            this.userId = userId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ModeratorModel that = (ModeratorModel) o;
            return Objects.equals(userId, that.userId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(userId);
        }
    }
}
