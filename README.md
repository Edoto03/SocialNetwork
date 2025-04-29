# SocialNetwork

A Java simulation of a social network, developed for the Object-Oriented Programming course at FMI, Sofia University. Users can register, build friendships, create and react to posts, and see how content propagates through their interest network.

---

## Introduction

This project implements:

- **User profiles** with usernames, interests and bi-directional friendships  
- **Posting & reactions**: timestamped posts with support for multiple reaction types  
- **Interest-based reach**: determine which users see a given post based on shared interests and friendship paths  
- **Mutual friends** lookup and global ranking of users by friend count  

---

## Architecture Overview

### Core API  
- **`SocialNetwork`** (interface)  
  Defines the main operations: user registration, posting, reaction handling, reach calculation, mutual-friends and profile ranking.  
- **`SocialNetworkImpl`**  
  Uses in-memory collections (`HashSet` for users, `List` for posts), validates inputs, and implements reach via recursive network traversal.

### User Profiles  
- **`UserProfile`** (interface)  
  Abstracts a user’s identity, interests and friend list.  
- **`DefaultUserProfile`**  
  Concrete implementation, storing interests and friends in sets. Overrides `equals()`/`hashCode()` by username so that each user is treated uniquely in collections.

### Posts & Reactions  
- **`Post`** (interface)  
  Represents a social feed entry (author, content, timestamp, reactions).  
- **`SocialFeedPost`**  
  Stores reaction data in a map, enforces immutability on returned views, and supports changing or removing reactions.  
- **`ReactionType`** (enum)  
  Lists available reaction kinds (LIKE, LOVE, LAUGH, etc.).

### Supporting Types  
- **`Interest`** (enum)  
  Predefined categories (e.g. MUSIC, BOOKS, TRAVEL) used for matching users on shared interests.  
- **`SortByNumberOfFriendsComparator`**  
  Comparator to sort profiles descending by their number of friends.

---

## Project Structure

///
src/
└── bg/
    └── sofia/
        └── uni/
            └── fmi/
                └── mjt/
                    └── socialnetwork/
                        ├── exception/
                        │   └── UserRegistrationException.java
                        ├── post/
                        │   ├── Post.java
                        │   ├── ReactionType.java
                        │   └── SocialFeedPost.java
                        ├── profile/
                        │   ├── UserProfile.java
                        │   ├── DefaultUserProfile.java
                        │   ├── Interest.java
                        │   └── SortByNumberOfFriendsComparator.java
                        ├── SocialNetwork.java
                        ├── SocialNetworkImpl.java
                        └── Main.java
///
## Getting Started

1. Compile:  
   `javac -d out src/**/*.java`

2. Run the demo:  
   `java -cp out bg.sofia.uni.fmi.mjt.socialnetwork.Main`

3. In your IDE:  
   Import the `src/` directory as a Java project and run `Main.java`.
