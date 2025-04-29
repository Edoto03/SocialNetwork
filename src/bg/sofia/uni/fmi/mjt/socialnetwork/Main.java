package bg.sofia.uni.fmi.mjt.socialnetwork;

import bg.sofia.uni.fmi.mjt.socialnetwork.exception.UserRegistrationException;
import bg.sofia.uni.fmi.mjt.socialnetwork.post.Post;
import bg.sofia.uni.fmi.mjt.socialnetwork.post.ReactionType;
import bg.sofia.uni.fmi.mjt.socialnetwork.profile.DefaultUserProfile;
import bg.sofia.uni.fmi.mjt.socialnetwork.profile.Interest;
import bg.sofia.uni.fmi.mjt.socialnetwork.profile.UserProfile;

import java.util.Map;
import java.util.Set;
import java.util.SortedSet;

public class Main {
    public static void main(String[] args) {
        SocialNetwork socialNetwork = new SocialNetworkImpl();

        try {
            System.out.println("=== Registering users ===");
            UserProfile alice = new DefaultUserProfile("alice");
            UserProfile bob = new DefaultUserProfile("bob");
            UserProfile charlie = new DefaultUserProfile("charlie");
            UserProfile dave = new DefaultUserProfile("dave");
            UserProfile eve = new DefaultUserProfile("eve");

            socialNetwork.registerUser(alice);
            socialNetwork.registerUser(bob);
            socialNetwork.registerUser(charlie);
            socialNetwork.registerUser(dave);
            socialNetwork.registerUser(eve);

            try {
                socialNetwork.registerUser(alice);
            } catch (UserRegistrationException e) {
                System.out.println("Duplicate registration caught: " + e.getMessage());
            }

            System.out.println("\n=== Adding interests ===");
            alice.addInterest(Interest.MUSIC);
            alice.addInterest(Interest.BOOKS);
            alice.addInterest(Interest.TRAVEL);

            bob.addInterest(Interest.MUSIC);
            bob.addInterest(Interest.SPORTS);

            charlie.addInterest(Interest.TRAVEL);
            charlie.addInterest(Interest.FOOD);

            dave.addInterest(Interest.GAMES);
            dave.addInterest(Interest.MUSIC);

            eve.addInterest(Interest.BOOKS);
            eve.addInterest(Interest.TRAVEL);

            System.out.println("\n=== Adding friends ===");
            alice.addFriend(bob);
            alice.addFriend(charlie);
            bob.addFriend(dave);
            charlie.addFriend(eve);
            bob.addFriend(eve);

            System.out.println("\n=== Creating posts ===");
            Post post1 = socialNetwork.post(alice, "Just finished reading a great book about traveling!");
            Post post2 = socialNetwork.post(bob, "Going to the concert tonight!");
            Post post3 = socialNetwork.post(charlie, "Found an amazing restaurant during my travels!");
            Post post4 = socialNetwork.post(dave, "Anyone up for playing some games?");
            Post post5 = socialNetwork.post(eve, "Book recommendations for a long journey?");

            System.out.println("\n=== Adding reactions ===");
            post1.addReaction(bob, ReactionType.LIKE);
            post1.addReaction(charlie, ReactionType.LOVE);
            post1.addReaction(eve, ReactionType.LIKE);

            post2.addReaction(alice, ReactionType.LOVE);
            post2.addReaction(dave, ReactionType.LIKE);

            post3.addReaction(alice, ReactionType.LIKE);
            post3.addReaction(eve, ReactionType.LOVE);

            post4.addReaction(bob, ReactionType.LAUGH);

            post5.addReaction(alice, ReactionType.LIKE);
            post5.addReaction(charlie, ReactionType.LOVE);

            System.out.println("\n=== Changing reaction ===");
            post1.addReaction(bob, ReactionType.LOVE);

            System.out.println("\n=== Reactions for Alice's post ===");
            for (Map.Entry<ReactionType, Set<UserProfile>> entry : post1.getAllReactions().entrySet()) {
                if (!entry.getValue().isEmpty()) {
                    System.out.print(entry.getKey() + ": ");
                    entry.getValue().forEach(u -> System.out.print(u.getUsername() + " "));
                    System.out.println();
                }
            }

            System.out.println("Total reactions: " + post1.totalReactionsCount());

            System.out.println("\n=== Checking post reach ===");
            for (Post p : new Post[]{post1, post2, post3, post4, post5}) {
                Set<UserProfile> reached = socialNetwork.getReachedUsers(p);
                System.out.print(p.getAuthor().getUsername() + "'s post reaches: ");
                if (reached.isEmpty()) {
                    System.out.println("no one");
                } else {
                    reached.forEach(u -> System.out.print(u.getUsername() + " "));
                    System.out.println();
                }
            }

            System.out.println("\n=== Mutual friends ===");
            System.out.print("Alice & Bob: ");
            socialNetwork.getMutualFriends(alice, bob).forEach(u -> System.out.print(u.getUsername() + " "));
            System.out.println();

            System.out.print("Alice & Eve: ");
            socialNetwork.getMutualFriends(alice, eve).forEach(u -> System.out.print(u.getUsername() + " "));
            System.out.println();

            System.out.print("Bob & Eve: ");
            socialNetwork.getMutualFriends(bob, eve).forEach(u -> System.out.print(u.getUsername() + " "));
            System.out.println();

            System.out.println("\n=== Profiles sorted by friend count ===");
            SortedSet<UserProfile> sorted = socialNetwork.getAllProfilesSortedByFriendsCount();
            sorted.forEach(p -> System.out.println(p.getUsername() + ": " + p.getFriends().size() + " friends"));

            System.out.println("\n=== Error cases ===");
            try {
                socialNetwork.post(null, "Invalid");
            } catch (Exception e) {
                System.out.println("Caught null user post: " + e.getMessage());
            }

            try {
                socialNetwork.post(alice, "");
            } catch (Exception e) {
                System.out.println("Caught empty post: " + e.getMessage());
            }

            try {
                alice.addInterest(null);
            } catch (Exception e) {
                System.out.println("Caught null interest: " + e.getMessage());
            }

            try {
                alice.addFriend(alice);
            } catch (Exception e) {
                System.out.println("Caught self-friend: " + e.getMessage());
            }

            System.out.println("\n=== Demo complete ===");

        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
