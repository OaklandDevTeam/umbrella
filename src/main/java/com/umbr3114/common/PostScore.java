package com.umbr3114.common;

/*
This class is responsible for calculating a decaying score for posts.
This implementation is highly influenced by this blog post written by Jules Jacobs
http://julesjacobs.github.io/2015/05/06/exponentially-decaying-likes.html
 */

import com.mongodb.MongoWriteException;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.umbr3114.ServiceLocator;
import com.umbr3114.data.CollectionFactory;
import com.umbr3114.models.PostModel;
import org.bson.types.ObjectId;
import org.mongojack.JacksonMongoCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PostScore {
    public static final double DECAY_HOUR = 1/3.6e6; // 1 hour in an hour
    public static final double DECAY_DAY = 24/3.6e6; // 24 hours in a day
    public static final double DECAY_THREEDAY = 72/3.6e6; // 72 hours in 3 days
    public static final double DECAY_WEEK = 168/3.6e6; // 168 hours in a week

    /**
     * This function calculates a score that decays over time based on the time of when an event happens (a post comment, for instance)
     * @param decay the rate of decay for a specific score (decays over an hour, day, week, etc)
     * @param initial the initial score
     * @param timeMillis
     * @return
     */
    public static double calculateScore(double decay, double initial, long timeMillis) {
        double score;
        double max;
        double min;

        max = Math.max(initial, decay*timeMillis);
        min = Math.min(initial, decay*timeMillis);
        score = max + Math.log1p(Math.exp(min - max)); // log1p is used to avoid overflows. The exponential of min - max can get very large very fast.
        return score;
    }

    public static void calculateScoreForPostModel(PostModel model, long time) {
        model.scoreHour = calculateScore(DECAY_HOUR, model.scoreHour, time);
        model.scoreDay = calculateScore(DECAY_DAY, model.scoreDay, time);
        model.scoreThreeDay = calculateScore(DECAY_THREEDAY, model.scoreThreeDay, time);
        model.scoreWeek = calculateScore(DECAY_WEEK, model.scoreWeek, time);
    }

    /**
     * This method will execute scorePostFromIdAndUpdate in a background thread.
     * @param postId Post ID to be scored
     * @param timeMillis The time that the event happened
     */
    public static void asyncScorePostFromIdAndUpdate(String postId, long timeMillis) {
        AsyncPostScoreRunnable runnable = new AsyncPostScoreRunnable(postId, timeMillis);
        ServiceLocator.getService().getBackgroundWorkerPool().execute(runnable);
    }

    /**
     * This will score a post and update it, with the provided id and event time.
     * It does this by creating a MongoCollection and looking-up the post
     * @param postId String ID of post to score
     * @param timeMillis Event time
     * @return true/fale if successful
     */
    public static boolean scorePostFromIdAndUpdate(String postId, long timeMillis) {
        JacksonMongoCollection<PostModel> mongoCollection = new CollectionFactory<PostModel>(
                ServiceLocator.getService().dbService(),
                PostModel.class
        ).getCollection();

        return scorePostFromIdAndUpdate(mongoCollection, postId, timeMillis);
    }

    /**
     * This will score a post and updated it, with the given mongoCollection, postId and event time
     * @param mongoCollection JacksonMongoCollection<PostModel> used to look up PostModel
     * @param postId String ID of post to look up
     * @param timeMillis time of event
     * @return true/false if successful
     */
    public static boolean scorePostFromIdAndUpdate(JacksonMongoCollection<PostModel> mongoCollection, String postId, long timeMillis) {
        PostModel model;
        if (mongoCollection == null)
            return false;
        model = mongoCollection.findOne(Filters.eq("_id", new ObjectId(postId)));

        if (model == null)
            return false;
        calculateScoreForPostModel(model, timeMillis);

        return updateScoredPostRecord(mongoCollection, model);
    }

    /**
     * This performs the logic to update a PostModel record that HAS ALREADY BEEN SCORED.
     * This does not call the scoring logic, the PostModel must be scored before it is passed to this method
     * @param mongoCollection collection object used to write the update
     * @param model model that IS ALREADY SCORED
     * @return true/false if successful
     */
    public static boolean updateScoredPostRecord(JacksonMongoCollection<PostModel> mongoCollection, PostModel model) {
        Logger log = LoggerFactory.getLogger("PostScoreUpdateLogic");

        if (mongoCollection == null || model == null) {
            return false;
        }

        try {
            mongoCollection.findOneAndUpdate(
                    Filters.eq("_id", model._id),
                    Updates.combine(
                            Updates.set("scoreHour", model.scoreHour),
                            Updates.set("scoreDay", model.scoreDay),
                            Updates.set("scoreThreeDay", model.scoreThreeDay),
                            Updates.set("scoreWeek", model.scoreWeek)
                    )
            );
        } catch (MongoWriteException e) {
            log.warn("Failed to updated post", e);
            return false;
        }
        return true;
    }

    static class AsyncPostScoreRunnable implements Runnable {
        String postId;
        long evenTimeMillis;

        public AsyncPostScoreRunnable(String postId, long eventTime) {
            this.postId = postId;
            this.evenTimeMillis = eventTime;
        }

        @Override
        public void run() {
            scorePostFromIdAndUpdate(postId, evenTimeMillis);
        }
    }
}
