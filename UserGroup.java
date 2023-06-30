// UserGroup.java
import java.util.ArrayList;
import java.util.List;

// UserGroup class representing a user group in Mini Twitter
class UserGroup {
    private String id; // ID of the user group
    private List<User> users;  // List of users in the group
    private List<UserGroup> subGroups;  // List of subgroups under this group
    private long creationTime; // Set the creation time to the current system time

    public UserGroup(String id) {
        this.id = id;
        this.users = new ArrayList<>();
        this.subGroups = new ArrayList<>();
        this.creationTime = System.currentTimeMillis();
    }

    public String getId() {
        return id;
    }

    public List<User> getUsers() {
        return users;
    }

    public List<UserGroup> getSubGroups() {
        return subGroups;
    }

    public void addUser(User user) {
        users.add(user); // Add a user to the user group
    }
    
    public void addGroup(UserGroup group) {
        subGroups.add(group); // Add a subgroup to the user group
    }
    
    public long getCreationTime() {
        return creationTime;
    }
 
    @Override
    public String toString() {
        return id; // Return the group's ID as the string representation
    }
}//end of UserGroup
