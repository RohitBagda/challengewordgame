package com.rohitbagda.challenge

object UsernameGenerator {
    private val usernames = setOf(
        "Bashful Bat", "Bashful Beetle", "Bouncy Bobcat", "Bouncy Bunny", "Breezy Beagle",
        "Breezy Bluejay", "Bubbly Badger", "Bubbly Bison", "Bumbling Bison", "Buzzing Bee",
        "Charming Chipmunk", "Chatty Cheetah", "Cheeky Chimp", "Chilled Chameleon", "Chipper Cicada",
        "Chubby Chicken", "Chubby Cheetah", "Clever Corgi", "Cozy Cougar", "Cozy Coyote",
        "Crazy Crab", "Curious Cat", "Dandy Dingo", "Dapper Dove", "Dapper Duck",
        "Daring Dodo", "Dashing Dalmatian", "Dizzy Dingo", "Dizzy Dolphin", "Dizzy Dragonfly",
        "Fancy Ferret", "Fancy Finch", "Fluttering Flamingo", "Frolicsome Frog", "Funky Fox",
        "Fuzzy Falcon", "Fuzzy Ferret", "Giddy Gazelle", "Giddy Gorilla", "Giggly Giraffe",
        "Giggly Goldfish", "Gleaming Gorilla", "Gleeful Guppy", "Grumpy Goose", "Groovy Grasshopper",
        "Happy Hamster", "Happy Hedgehog", "Jazzy Jaguar", "Jolly Jellyfish", "Jovial Jackal",
        "Jumpy Jay", "Lively Llama", "Lucky Ladybug", "Lucky Lion", "Lush Lynx",
        "Mellow Mongoose", "Melodic Magpie", "Merry Mink", "Merry Minnow", "Merry Moose",
        "Nutty Newt", "Plucky Pigeon", "Posh Panda", "Puffy Pug", "Purring Penguin",
        "Quirky Quokka", "Radiant Raccoon", "Silent Sloth", "Silent Sparrow", "Silent Snake",
        "Silent Starling", "Silly Sloth", "Silly Starfish", "Sneaky Snake", "Snuggly Sheep",
        "Snuggly Skunk", "Snazzy Sloth", "Snazzy Starling", "Snappy Sparrow", "Sneaky Snake",
        "Spicy Squirrel", "Speedy Snail", "Sprightly Swan", "Spry Squirrel", "Sassy Seal",
        "Slinky Squid", "Swift Sardine", "Vibrant Vulture", "Wacky Walrus", "Whimsical Whippet",
        "Whistling Wolf", "Wiggly Wren", "Witty Whale", "Zany Zebu", "Zany Zookeeper",
        "Zesty Zebra", "Zesty Zonkey", "Zippy Zebra", "Zippy Zebrafish"
    )

    fun generate() = usernames.random()
}