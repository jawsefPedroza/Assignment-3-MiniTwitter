// MiniTwitter.java
import javax.swing.*;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.event.*;

// Main class representing the Mini Twitter application
public class MiniTwitter {
    private UserGroup rootGroup;
    private DefaultMutableTreeNode rootNode;
    private DefaultTreeModel treeModel;

    public MiniTwitter() {
        this.rootGroup = new UserGroup("Root");
        this.rootNode = new DefaultMutableTreeNode(rootGroup);
        this.treeModel = new DefaultTreeModel(rootNode);
    }

    public void addUser(User user, UserGroup group) {
        group.addUser(user);
        DefaultMutableTreeNode userNode = new DefaultMutableTreeNode(user);
        DefaultMutableTreeNode parentNode = findNode(rootNode, group);
        if (parentNode != null) {
            parentNode.add(userNode);
            treeModel.reload(parentNode); // Update the parent node in the tree model
        }
    }
    
    public void addGroup(UserGroup group, UserGroup parentGroup) {
        if (isNameUsed(group.getId())) {
            JOptionPane.showMessageDialog(null, "Group name is already used!");
            return;
        }
    
        parentGroup.addGroup(group);
        DefaultMutableTreeNode parentNode = findNode(rootNode, parentGroup);
        if (parentNode != null) {
            DefaultMutableTreeNode groupNode = new DefaultMutableTreeNode(group, true);
            parentNode.add(groupNode);
            treeModel.reload(parentNode); // Update the parent node in the tree model
        }
    }
    
    private boolean isNameUsed(String name) {
        if (findUser(name) != null || findGroup(name) != null) {
            return true;
        }
        return false;
    }

    public User findUser(String userId) {
        return findUser(userId, rootGroup);
    }
    
    private DefaultMutableTreeNode findNode(DefaultMutableTreeNode parentNode, UserGroup group) {
        // Recursively search for the tree node representing the given group
        if (parentNode.getUserObject().equals(group)) {
            return parentNode;
        } else {
            int childCount = parentNode.getChildCount();
            for (int i = 0; i < childCount; i++) {
                DefaultMutableTreeNode childNode = (DefaultMutableTreeNode) parentNode.getChildAt(i);
                DefaultMutableTreeNode result = findNode(childNode, group);
                if (result != null) {
                    return result;
                }
            }
            return null;
        }
    }

    public void displayAdminControlPanel() {
        JFrame frame = new JFrame("Admin Control Panel");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create UI components
        JButton createUserButton = new JButton("Create User");
        JTextField userIdTextField = new JTextField(15);
        JButton createGroupButton = new JButton("Create Group");
        JTextField groupIdTextField = new JTextField(15);
        JButton showTotalUsersButton = new JButton("Total Users");
        JButton showTotalGroupsButton = new JButton("Total Groups");
        JButton showTotalTweetsButton = new JButton("Total Tweets");
        JButton showPositiveTweetsButton = new JButton("Positive Tweets");
        JButton openUserViewButton = new JButton("Open User View");
        JButton lastUpdateUserButton = new JButton("Last Update User");

        
        JTree userTree = new JTree(treeModel);
        userTree.setCellRenderer(new FolderTreeCellRenderer());
        JScrollPane treeScrollPane = new JScrollPane(userTree);

        // Configure layout
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(5, 5, 5, 5);

        panel.add(new JLabel("Create User: "), constraints);
        constraints.gridx++;
        panel.add(userIdTextField, constraints);
        constraints.gridx++;
        panel.add(createUserButton, constraints);

        constraints.gridx = 0;
        constraints.gridy++;
        panel.add(new JLabel("Create Group: "), constraints);
        constraints.gridx++;
        panel.add(groupIdTextField, constraints);
        constraints.gridx++;
        panel.add(createGroupButton, constraints);

        constraints.gridx = 0;
        constraints.gridy++;
        panel.add(showTotalUsersButton, constraints);

        constraints.gridx++;
        panel.add(showTotalGroupsButton, constraints);

        constraints.gridx = 0;
        constraints.gridy++;
        panel.add(showTotalTweetsButton, constraints);

        constraints.gridx++;
        panel.add(showPositiveTweetsButton, constraints);

        constraints.gridx++;
        panel.add(lastUpdateUserButton, constraints);

        constraints.gridx = 0;
        constraints.gridy++;
        constraints.gridwidth = 3;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weightx = 1.0;
        constraints.weighty = 1.0;
        panel.add(treeScrollPane, constraints);

        constraints.gridx = 0;
        constraints.gridy++;
        panel.add(openUserViewButton, constraints);

        // Add event listeners
        createUserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String userId = userIdTextField.getText().trim();
                if (!userId.isEmpty()) {
                    // Check if the user already exists
                    if (findUser(userId) != null) {
                        JOptionPane.showMessageDialog(frame, "User already exists!");
                    } else if (userId.contains(" ")) {
                        JOptionPane.showMessageDialog(frame, "User ID cannot contain spaces!");
                    } else {
                        User newUser = new User(userId);
                        addUser(newUser, rootGroup);
                        userIdTextField.setText("");
                    }
                }
            }
        });
        // Make a group
        createGroupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String groupId = groupIdTextField.getText().trim();
                if (!groupId.isEmpty()) {
                    // Check if the group already exists
                    if (findUser(groupId) != null || findGroup(groupId) != null) {
                        JOptionPane.showMessageDialog(frame, "Group already exists!");
                    } else if (groupId.contains(" ")) {
                        JOptionPane.showMessageDialog(frame, "Group ID cannot contain spaces!");
                    } else {
                        UserGroup newGroup = new UserGroup(groupId);
                        addGroup(newGroup, rootGroup);
                        groupIdTextField.setText("");
                    }
                }
            }
        });

        showTotalUsersButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int totalUsers = countTotalUsers(rootGroup);
                JOptionPane.showMessageDialog(frame, "Total Users: " + totalUsers);
            }
        });

        showTotalGroupsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int totalGroups = countTotalGroups(rootGroup);
                JOptionPane.showMessageDialog(frame, "Total Groups: " + totalGroups);
            }
        });

        showTotalTweetsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int totalTweets = countTotalTweets(rootGroup);
                JOptionPane.showMessageDialog(frame, "Total Tweets: " + totalTweets);
            }
        });

        showPositiveTweetsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int totalTweets = countTotalTweets(rootGroup);
                int positiveTweets = countPositiveTweets(rootGroup);
                double percentage = (double) positiveTweets / totalTweets * 100;
                JOptionPane.showMessageDialog(frame, "Positive Tweets: " + String.format("%.2f", percentage) + "%");
            }
        });

        lastUpdateUserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String lastUpdateUserId = findLastUpdateUser(rootGroup);
                JOptionPane.showMessageDialog(frame, "Last Update User ID: " + lastUpdateUserId);
            }
        });

        openUserViewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) userTree.getLastSelectedPathComponent();
                if (selectedNode != null && selectedNode.getUserObject() instanceof User) {
                    User selectedUser = (User) selectedNode.getUserObject();
                    displayUserView(selectedUser);
                }
            }
        });

        frame.add(panel);
        frame.pack();
        frame.setVisible(true);
    }

private int countTotalUsers(UserGroup group) {
        int count = group.getUsers().size();
        for (UserGroup subGroup : group.getSubGroups()) {
            count += countTotalUsers(subGroup);
        }
        return count;
    }

 private int countTotalGroups(UserGroup group) {
    int count = group.getSubGroups().size();
    for (UserGroup subGroup : group.getSubGroups()) {
        count += countTotalGroups(subGroup);
    }
    return count;
}

private int countTotalTweets(UserGroup group) {
    int count = 0;
    for (User user : group.getUsers()) {
        count += user.getTweetCount();
    }
    for (UserGroup subGroup : group.getSubGroups()) {
        count += countTotalTweets(subGroup);
    }
    return count;
}

private int countPositiveTweets(UserGroup group) {
    int count = 0;
    for (User user : group.getUsers()) {
        for (String tweet : user.getNewsFeed()) {
            if (isPositiveTweet(tweet)) {
                count++;
            }
        }
    }
    for (UserGroup subGroup : group.getSubGroups()) {
        count += countPositiveTweets(subGroup);
    }
    return count;
}

private boolean isPositiveTweet(String tweet) {
    // Customize this method to define what constitutes a positive tweet
    String[] positiveWords = {"good", "great", "excellent"};
    tweet = tweet.toLowerCase(); // Convert tweet to lowercase for case-insensitive comparison
    for (String word : positiveWords) {
        if (tweet.contains(word)) {
            return true;
        }
    }
    return false;
}

    private UserGroup findGroup(String groupId, UserGroup group) {
        if (group.getId().equals(groupId)) {
            return group;
        } else {
            for (UserGroup subGroup : group.getSubGroups()) {
                UserGroup foundGroup = findGroup(groupId, subGroup);
                if (foundGroup != null) {
                    return foundGroup;
                }
            }
            return null;
        }
    }
    
    private UserGroup findGroup(String groupId) {
        return findGroup(groupId, rootGroup);
    }

    private String findLastUpdateUser(UserGroup group) {
        String lastUpdateUserId = null;
        long maxUpdateTime = Long.MIN_VALUE;
    
        for (User user : group.getUsers()) {
            long userUpdateTime = user.getLastUpdateTime();
            if (userUpdateTime > maxUpdateTime) {
                maxUpdateTime = userUpdateTime;
                lastUpdateUserId = user.getId();
            }
        }
    
        for (UserGroup subGroup : group.getSubGroups()) {
            String subGroupLastUpdateUser = findLastUpdateUser(subGroup);
            if (subGroupLastUpdateUser != null) {
                return subGroupLastUpdateUser;
            }
        }
    
        return lastUpdateUserId;
    }
   
    public void displayUserView(User user) {
        JFrame frame = new JFrame(user.getId() + " User View");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    
        // Create UI components
        DefaultListModel<String> followingListModel = new DefaultListModel<>();
        JList<String> followingList = new JList<>(followingListModel);
        JScrollPane followingScrollPane = new JScrollPane(followingList);
    
        DefaultListModel<String> newsFeedListModel = new DefaultListModel<>();
        JList<String> newsFeedList = new JList<>(newsFeedListModel);
        JScrollPane newsFeedScrollPane = new JScrollPane(newsFeedList);
    
        JTextField followUserIdTextField = new JTextField(15);
        JButton followUserButton = new JButton("Follow");
    
        JTextField tweetTextField = new JTextField(15);
        JButton postTweetButton = new JButton("Post Tweet");
    
        // Configure layout
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(5, 5, 5, 5);
    
        panel.add(new JLabel("Following:"), constraints);
    
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridheight = 4;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weightx = 1.0;
        constraints.weighty = 1.0;
        panel.add(followingScrollPane, constraints);
    
        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.gridheight = 1;
        constraints.gridwidth = 2;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        panel.add(new JLabel("Follow User:"), constraints);
    
        constraints.gridx = 1;
        constraints.gridy = 1;
        constraints.gridwidth = 1;
        panel.add(followUserIdTextField, constraints);
    
        constraints.gridx = 2;
        constraints.gridy = 1;
        panel.add(followUserButton, constraints);
    
        constraints.gridx = 1;
        constraints.gridy = 2;
        constraints.gridwidth = 2;
        constraints.fill = GridBagConstraints.BOTH;
        panel.add(new JLabel("News Feed:"), constraints);
    
        constraints.gridx = 1;
        constraints.gridy = 3;
        constraints.gridheight = 2;
        // Increase the width and height of the news feed scroll pane
        constraints.weightx = 1.0;
        constraints.weighty = 1.0;
        panel.add(newsFeedScrollPane, constraints);
    
        constraints.gridx = 3;
        constraints.gridy = 0;
        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        panel.add(new JLabel("Tweet:"), constraints);
    
        constraints.gridx = 3;
        constraints.gridy = 1;
        panel.add(tweetTextField, constraints);
    
        constraints.gridx = 3;
        constraints.gridy = 2;
        panel.add(postTweetButton, constraints);
    
        // Set preferred size of the news feed scroll pane
        Dimension preferredSize = new Dimension(400, 300); // Adjust the values as needed
        newsFeedScrollPane.setPreferredSize(preferredSize);
    
        // Add the lastUpdateTime label
        JLabel lastUpdateTimeLabel = new JLabel("Last Update Time: " + user.getLastUpdateTime());
        constraints.gridx = 4;
        constraints.gridy = 6;
        panel.add(lastUpdateTimeLabel, constraints);
    
        updateFollowingList(followingListModel, user);
        updateNewsFeed(newsFeedListModel, user);
    
        // Add the panel to the frame and display the window
        frame.getContentPane().add(panel);
        frame.pack();
        frame.setVisible(true);
    
        // Add event listeners
       followUserButton.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
        String userId = followUserIdTextField.getText().trim();
        if (!userId.isEmpty()) {
            User userToFollow = findUser(userId); // Assuming findUser is defined properly
            if (userToFollow != null && !userToFollow.equals(user)) {
                user.addFollowing(userToFollow);
                followingListModel.addElement("- " + userToFollow.getId()); // Add "- " before the user ID
                followUserIdTextField.setText("");
            } else {
                JOptionPane.showMessageDialog(frame, "User not found!");
            }
        }
    }
});
      
postTweetButton.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
        // Retrieve the tweet from the text field
        String tweet = tweetTextField.getText().trim();
        if (!tweet.isEmpty()) {
            user.postTweet(" - " + user.getId() + " : " + tweet); // Add a dash before the user ID
            tweetTextField.setText("");
            updateNewsFeed(newsFeedListModel, user);
            // Update the last update time label
            lastUpdateTimeLabel.setText("Last Update Time: " + user.getLastUpdateTime());
        }
    }
});
            // Set up the initial view
       // Set up the initial view
       updateFollowingList(followingListModel, user);
       updateNewsFeed(newsFeedListModel, user);

// Add the panel to the frame and display the window
frame.getContentPane().add(panel);
frame.pack();
frame.setVisible(true);

JButton creationTimeButton = new JButton("Creation Time: " + user.getFormattedCreationTime());
    creationTimeButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            String message = "User creation time: " + user.getFormattedCreationTime();
            JOptionPane.showMessageDialog(frame, message);
        }
    });
    creationTimeButton.setEnabled(false);
    constraints.gridx = 0;
    constraints.gridy = 6;
    panel.add(creationTimeButton, constraints);
}

// Method to update the following list with the user's following users
private void updateFollowingList(DefaultListModel<String> followingListModel, User user) {
    followingListModel.clear();
    for (User followingUser : user.getFollowing()) {
        followingListModel.addElement("- " + followingUser.getId());
    }
}

// Method to update the news feed list with the user's tweets
private void updateNewsFeed(DefaultListModel<String> newsFeedListModel, User user) {
    newsFeedListModel.clear();
    for (String tweet : user.getNewsFeed()) {
        newsFeedListModel.addElement(tweet);
    }
}



    private User findUser(String userId, UserGroup group) {
        for (User user : group.getUsers()) {
            if (user.getId().equals(userId)) {
                return user;
            }
        }
        for (UserGroup subGroup : group.getSubGroups()) {
            User user = findUser(userId, subGroup);
            if (user != null) {
                return user;
            }
        }
        return null;
    }

    public class CreationTimeActionListener implements ActionListener {
        private User user;
    
        public CreationTimeActionListener(User user) {
            this.user = user;
        }
    
        @Override
        public void actionPerformed(ActionEvent e) {
            JOptionPane.showMessageDialog(null, "Creation Time: " + user.getCreationTime());
        }
    }

}//end of the MiniTwitter