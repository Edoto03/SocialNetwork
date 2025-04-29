package bg.sofia.uni.fmi.mjt.socialnetwork.profile;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class DefaultUserProfile implements UserProfile {
    private String username;
    private Set<Interest> interests;
    private Set<UserProfile> friends;

    public DefaultUserProfile(String username) {
        this.username = username;
        this.interests = new HashSet<>();
        this.friends = new HashSet<>();
    }

    public DefaultUserProfile(UserProfile other) {
        this.username = other.getUsername();
        this.interests = new HashSet<>(other.getInterests());
        this.friends = new HashSet<>(other.getFriends());
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public Collection<Interest> getInterests() {
        return Set.copyOf(interests);
    }

    @Override
    public boolean addInterest(Interest interest) {
        if (interest == null) {
            throw new IllegalArgumentException("The interest you are trying to add cannot be null.");
        }

        return this.interests.add(interest);
    }

    @Override
    public boolean removeInterest(Interest interest) {
        if (interest == null) {
            throw new IllegalArgumentException("The interest you are trying to remove cannot be null.");
        }

        return this.interests.remove(interest);
    }

    @Override
    public Collection<UserProfile> getFriends() {
        return Set.copyOf(this.friends);
    }

    @Override
    public boolean addFriend(UserProfile userProfile) {
        if (userProfile == this) {
            throw new IllegalArgumentException("Can't add yourself as a friend.");
        }

        if (userProfile == null) {
            throw new IllegalArgumentException("The user you want be friends with is null.");
        }

        if (this.friends.contains(userProfile)) {
            return false;
        }

        boolean added = this.friends.add(userProfile);
        if (added && !userProfile.isFriend(this)) {
            userProfile.addFriend(this);
        }

        return added;
    }

    @Override
    public boolean unfriend(UserProfile userProfile) {
        if (userProfile == this) {
            throw new IllegalArgumentException("Can't unfriend yourself.");
        }

        if (userProfile == null) {
            throw new IllegalArgumentException("The user you want to unfriend is null.");
        }

        if (!this.friends.contains(userProfile)) {
            return false;
        }

        boolean removed = this.friends.remove(userProfile);
        if (removed && userProfile.isFriend(this)) {
            userProfile.unfriend(this);
        }

        return removed;
    }

    @Override
    public boolean isFriend(UserProfile userProfile) {
        if (userProfile == null) {
            throw new IllegalArgumentException("The user profile cannot be null.");
        }

        return this.friends.contains(userProfile);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserProfile)) return false;
        UserProfile that = (UserProfile) o;
        return this.getUsername().equals(that.getUsername());
    }

    @Override
    public int hashCode() {
        return this.getUsername().hashCode();
    }

}