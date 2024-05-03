package com.rohitbagda.challenge

object UsernameGenerator {
    private val usernames = setOf(
        "BashfulBat", "BashfulBeetle", "BouncyBobcat", "BouncyBunny", "BreezyBeagle",
        "BreezyBluejay", "BubblyBadger", "BubblyBison", "BumblingBison", "BuzzingBee",
        "CharmingChipmunk", "ChattyCheetah", "CheekyChimp", "ChilledChameleon", "ChipperCicada",
        "ChubbyChicken", "ChubbyCheetah", "CleverCorgi", "CozyCougar", "CozyCoyote",
        "CrazyCrab", "CuriousCat", "DandyDingo", "DapperDove", "DapperDuck",
        "DaringDodo", "DashingDalmatian", "DizzyDingo", "DizzyDolphin", "DizzyDragonfly",
        "FancyFerret", "FancyFinch", "FlutteringFlamingo", "FrolicsomeFrog", "FunkyFox",
        "FuzzyFalcon", "FuzzyFerret", "GiddyGazelle", "GiddyGorilla", "GigglyGiraffe",
        "GigglyGoldfish", "GleamingGorilla", "GleefulGuppy", "GrumpyGoose", "GroovyGrasshopper",
        "HappyHamster", "HappyHedgehog", "JazzyJaguar", "JollyJellyfish", "JovialJackal",
        "JumpyJay", "LivelyLlama", "LuckyLadybug", "LuckyLion", "LushLynx",
        "MellowMongoose", "MelodicMagpie", "MerryMink", "MerryMinnow", "MerryMoose",
        "NuttyNewt", "PluckyPigeon", "PoshPanda", "PuffyPug", "PurringPenguin",
        "QuirkyQuokka", "RadiantRaccoon", "SilentSloth", "SilentSparrow", "SilentSnake",
        "SilentStarling", "SillySloth", "SillyStarfish", "SneakySnake", "SnugglySheep",
        "SnugglySkunk", "SnazzySloth", "SnazzyStarling", "SnappySparrow", "SneakySnake",
        "SpicySquirrel", "SpeedySnail", "SprightlySwan", "SprySquirrel", "SassySeal",
        "SlinkySquid", "SwiftSardine", "VibrantVulture", "WackyWalrus", "WhimsicalWhippet",
        "WhistlingWolf", "WigglyWren", "WittyWhale", "ZanyZebu", "ZanyZookeeper",
        "ZestyZebra", "ZestyZonkey", "ZippyZebra", "ZippyZebrafish"
    )

    fun generate() = usernames.random()
}