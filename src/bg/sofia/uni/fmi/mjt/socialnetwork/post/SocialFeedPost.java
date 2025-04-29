package bg.sofia.uni.fmi.mjt.socialnetwork.post;

import bg.sofia.uni.fmi.mjt.socialnetwork.profile.UserProfile;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class SocialFeedPost implements Post {
    private UserProfile author;
    private String uniqueId;
    private LocalDateTime publishedOn;
    private String content;
    private Map<UserProfile, ReactionType> reactions;

    public SocialFeedPost(UserProfile author, String uniqueId) {
        this.author = author;
        this.uniqueId = uniqueId;
        this.publishedOn = LocalDateTime.now();
        this.content = "";
        this.reactions = new HashMap<>();
    }

    @Override
    public UserProfile getAuthor() {
        return author;
    }

    @Override
    public String getUniqueId() {
        return uniqueId;
    }

    @Override
    public LocalDateTime getPublishedOn() {
        return publishedOn;
    }

    @Override
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public boolean addReaction(UserProfile userProfile, ReactionType reactionType) {
        if (userProfile == null) {
            throw new IllegalArgumentException("The user reacting can't be null.");
        }

        if (reactionType == null) {
            throw new IllegalArgumentException("The reaction is invalid");
        }

        if (this.reactions.containsKey(userProfile)) {
            this.reactions.replace(userProfile, reactionType);
            return false;
        } else {
            this.reactions.put(userProfile, reactionType);
            return true;
        }
    }

    @Override
    public boolean removeReaction(UserProfile userProfile) {
        if (userProfile == null) {
            throw new IllegalArgumentException("The user can't be null.");
        }

        if (this.reactions.containsKey(userProfile)) {
            this.reactions.remove(userProfile);
            return true;
        }
        return false;
    }

    @Override
    public Map<ReactionType, Set<UserProfile>> getAllReactions() {
        Map<ReactionType, Set<UserProfile>> allReactions = new HashMap<>();

        for (ReactionType reaction : ReactionType.values()) {
            allReactions.put(reaction, new HashSet<>());
        }

        for (Map.Entry<UserProfile, ReactionType> entry : this.reactions.entrySet()) {
            allReactions.get(entry.getValue()).add(entry.getKey());
        }

        for (ReactionType reaction : ReactionType.values()) {
            allReactions.put(reaction, Collections.unmodifiableSet(allReactions.get(reaction)));
        }

        return Collections.unmodifiableMap(allReactions);
    }

    @Override
    public int getReactionCount(ReactionType reactionType) {
        Collection<ReactionType> reactionTypes = this.reactions.values();
        return Collections.frequency(reactionTypes, reactionType);
    }

    @Override
    public int totalReactionsCount() {
        ReactionType[] reactionTypes = this.reactions.values().toArray(new ReactionType[0]);
        return reactionTypes.length;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SocialFeedPost that = (SocialFeedPost) o;
        return Objects.equals(author, that.author) && Objects.equals(uniqueId, that.uniqueId) &&
            Objects.equals(publishedOn, that.publishedOn) && Objects.equals(content, that.content) &&
            Objects.equals(reactions, that.reactions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(author, uniqueId, publishedOn, content, reactions);
    }
}
