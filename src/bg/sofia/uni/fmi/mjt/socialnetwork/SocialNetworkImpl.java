package bg.sofia.uni.fmi.mjt.socialnetwork;

import bg.sofia.uni.fmi.mjt.socialnetwork.exception.UserRegistrationException;
import bg.sofia.uni.fmi.mjt.socialnetwork.post.Post;
import bg.sofia.uni.fmi.mjt.socialnetwork.post.SocialFeedPost;
import bg.sofia.uni.fmi.mjt.socialnetwork.profile.Interest;
import bg.sofia.uni.fmi.mjt.socialnetwork.profile.SortByNumberOfFriendsComparator;
import bg.sofia.uni.fmi.mjt.socialnetwork.profile.UserProfile;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

public class SocialNetworkImpl implements SocialNetwork {
    private Set<UserProfile> users;
    private List<Post> posts;

    public SocialNetworkImpl() {
        this.users = new HashSet<>();
        this.posts = new LinkedList<>();
    }

    @Override
    public void registerUser(UserProfile userProfile) throws UserRegistrationException {
        if (userProfile == null) {
            throw new IllegalArgumentException("You can't add an user that is null.");
        }

        if (this.users.contains(userProfile)) {
            throw new UserRegistrationException("User %s is already registered.".formatted(userProfile.getUsername()));
        }

        this.users.add(userProfile);
    }

    @Override
    public Set<UserProfile> getAllUsers() {
        return Set.copyOf(this.users);
    }

    @Override
    public Post post(UserProfile userProfile, String content) throws UserRegistrationException {
        if (userProfile == null) {
            throw new IllegalArgumentException("User which is null cannot post.");
        }

        if (content == null || content.isBlank()) {
            throw new IllegalArgumentException("The content of the post should contain info.");
        }

        if (!this.users.contains(userProfile)) {
            throw new UserRegistrationException("User %s is not yet registered.".formatted(userProfile.getUsername()));
        }

        String uniqueId = java.util.UUID.randomUUID().toString();
        SocialFeedPost post = new SocialFeedPost(userProfile, uniqueId);
        post.setContent(content);
        this.posts.add(post);

        return post;
    }

    @Override
    public Collection<Post> getPosts() {
        return List.copyOf(this.posts);
    }

    @Override
    public Set<UserProfile> getReachedUsers(Post post) {
        if (post == null) {
            throw new IllegalArgumentException("Post can't be null to be searched for.");
        }

        UserProfile author = post.getAuthor();
        Set<UserProfile> reachedUsers = new HashSet<>();

        for (UserProfile userNow : users) {
            Set<Interest> commonInterests = new HashSet<>(userNow.getInterests());
            commonInterests.retainAll(author.getInterests());

            if (!commonInterests.isEmpty()) {
                if (userNow.isFriend(author) || isInSameNetwork(userNow, author, new HashSet<>())) {
                    reachedUsers.add(userNow);
                }
            }
        }

        return reachedUsers;
    }

    private boolean isInSameNetwork(UserProfile start, UserProfile target, Set<UserProfile> visited) {
        visited.add(start);

        if (start.isFriend(target)) {
            return true;
        }

        for (UserProfile friend : start.getFriends()) {
            if (visited.contains(friend)) {
                continue;
            }
            if (isInSameNetwork(friend, target, visited)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public Set<UserProfile> getMutualFriends(UserProfile userProfile1, UserProfile userProfile2)
        throws UserRegistrationException {
        if (userProfile1 == null || userProfile2 == null) {
            throw new IllegalArgumentException("Users can't be null.");
        }

        if (!this.users.contains(userProfile1)) {
            throw new IllegalArgumentException("User %s is not registered yet.".formatted(userProfile1.getUsername()));
        }

        if (!this.users.contains(userProfile2)) {
            throw new IllegalArgumentException("User %s is not registered yet.".formatted(userProfile2.getUsername()));
        }

        Set<UserProfile> mutualFriends = new HashSet<>(userProfile1.getFriends());
        mutualFriends.retainAll(userProfile2.getFriends());
        return mutualFriends;
    }

    @Override
    public SortedSet<UserProfile> getAllProfilesSortedByFriendsCount() {
        SortedSet<UserProfile> sorted = new TreeSet<>(new SortByNumberOfFriendsComparator());
        sorted.addAll(users);
        return sorted;
    }

}
