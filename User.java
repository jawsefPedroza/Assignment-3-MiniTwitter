import java.util.ArrayList;
import java.util.List;
import java.text.SimpleDateFormat;


public class User {
    private String id;
    private List<User> following;  // List of users that this user is following
    private List<User> followers;  // List of users who are following this user
    private List<String> newsFeed; // List of tweets in the user's news feed
    private static List<User> allUsers = new ArrayList<>(); // List of all users
    private long creationTime; //timer
    private int tweetCount;
    private long lastUpdateTime; //update Timer


    public User(String id) {
        this.id = id;
        this.following = new ArrayList<>();
        this.followers = new ArrayList<>();
        this.newsFeed = new ArrayList<>();
        allUsers.add(this); // Add the user to the list of all users
        this.creationTime = System.currentTimeMillis(); // Set the creation time to the current system time
        tweetCount = 0;
        lastUpdateTime = 0; // Initialize the lastUpdateTime to 0
    }

    public String getId() {
        return id;
    }

    public List<User> getFollowing() {
        return following;
    }

    public List<String> getNewsFeed() {
        return newsFeed;
    }

    public void addFollowing(User user) {
        following.add(user);    // Add the given user to the list of users that this user is following
        user.addFollower(this); // Add this user as a follower of the given user
    }

    public void addFollower(User user) {
        followers.add(user);    // Add the given user to the list of followers of this user
    }

    public void postTweet(String tweet) {
        newsFeed.add(tweet); // Add the tweet to the user's news feed
        tweetCount++;

        // Add the tweet to the news feed of all followers 
        for (User follower : followers) {
            follower.getNewsFeed().add(tweet);
        }

        // Update the lastUpdateTime whenever a new tweet is posted
        lastUpdateTime = System.currentTimeMillis();
    }
    
    public long getCreationTime() {
        return creationTime;
    }
    
    public int getTweetCount() {
        return tweetCount;
    }

    public String getFormattedCreationTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(creationTime);
    }
    
    public long getLastUpdateTime() {
        return lastUpdateTime;
    }

    // Add this method to override toString()
    @Override
    public String toString() {
        return id;
    }
}//end of User
