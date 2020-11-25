package by.thmihnea.persistent.lang;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;

public enum Lang {
    MESSAGE_PREFIX("&e[Tower Defense] "),
    ARENA_NOT_FOUND("&cCouldn't find arena!"),
    USER_IS_NOT_ADMIN("&e(Tower Defense) &8| &fPlugin is running on version %version%."),
    INSUFFICIENT_ARGS_TD_1("&8&m-----------------------------"),
    INSUFFICIENT_ARGS_TD_2("&7Here's a list of available commands:"),
    INSUFFICIENT_ARGS_TD_3("&e/td arena &6- &fCreates and displays arena info"),
    INSUFFICIENT_ARGS_TD_4("&e/td setspawnpoint &6- &fSets spawnpoints for attackers/defenders/arena lobby"),
    INSUFFICIENT_ARGS_TD_5("&e/td sethub &6- &fSets game hub (world where players are gonna be sent after a game is done)"),
    INSUFFICIENT_ARGS_TD_6("&e/td remove &6- &fRemoves an arena"),
    INSUFFICIENT_ARGS_TD_7("&e/join &6- &fJoin an arena"),
    INSUFFICIENT_ARGS_TD_8("&e/leave &6- &fLeaves an arena"),
    INSUFFICIENT_ARGS_TD_LAST("&8&m-----------------------------"),
    INSUFFICIENT_ARGS_TD_ARENA("&e(Tower Defense) &8| &fInsufficient arguments &7&o(Specified %amount%)"),
    COMMAND_TD_ARENA_USAGE("&e(Tower Defense) &8| &fUsage: &e/td arena (create/info/edit/list) <name> <id> <world> <timeLimit>"),
    COMMAND_TD_ARENA_CREATE_WORLD_NOT_FOUND("&e(Tower Defense) &8| &fThe specified world couldn't be found. Creating a new one."),
    BUILD_MODE_ON("&e(Tower Defense) &8| &fYou can now break/place blocks anywhere!"),
    BUILD_MODE_OFF("&e(Tower Defense) &8| &fYou can no longer break/place blocks anywhere."),
    INSUFFICIENT_ARGS_TD_INFO("&e(Tower Defense) &8| &fInsufficient arguments. Usage: /td arena info <name>"),
    ENABLED_EDITOR("&6➢ &fSwitch editor mode ON. You are now editing arena: &e%arena%"),
    DISABLED_EDITOR("&6➢ &fYou are no longer editing an arena."),
    INCORRECT_USAGE_EDIT("&e(Tower Defense) &8| &fUsage: &e/td arena edit <name>"),
    NOT_EDITING_ARENA("&e(Tower Defense) &8| &fYou can't use the spawnpoint command as you're currently not editing any arena."),
    ATTACKER_SPAWNPOINT_SET("&e(Tower Defense) &8| &fYou have successfully set the attacker spawnpoint."),
    DEFENDER_SPAWNPOINT_SET("&e(Tower Defense) &8| &fYou have successfully set the defender spawnpoint."),
    LOBBY_SPAWNPOINT_SET("&e(Tower Defense) &8| &fYou have successfully set the lobby spawnpoint for this arena."),
    GAMEHUB_SET("&e(Tower Defense) &8| &fYou have successfully set the Game Hub world to %world%!"),
    GAMEHUB_SPAWNPOINT_SET("&e(Tower Defense) &8| &fYou have successfully set the Game Hub spawnpoint to your current location!"),
    WHICH_SPAWNPOINTS_ARENT_SET("&6➢ &fYou still have to set spawnpoints: &e%spawnpoints%"),
    WORLD_NOT_FOUND("&e(Tower Defense) &8| &fThis world couldn't be found."),
    CANT_SET_HUB_TO_ARENA_WORLD("&c&l(!) &fThe Game Hub can't be set to an Arena world!"),
    CANT_SET_ARENA_TO_HUB_WORLD("&c&l(!) &fThe Arena world can't be set to the Game Hub world!"),
    ALREADY_IN_GAME("&cYou must leave your current arena before joining another one!"),
    ALREADY_IN_GAME_EDIT("&cYou must leave your current arena before editing another one!"),
    COMMAND_TD_ARENA_REMOVE_USAGE("&e(Tower Defense) &8| &fUsage: &e/td arena remove <name>"),
    ARENA_SUCCESSFULLY_DELETED("&e(Tower Defense) &8| &fArena %arena% has been successfully deleted!"),
    COUNTDOWN_CANCELLED("&e(Tower Defense) &8| &fThe countdown has been cancelled due to the fact that a player has left the lobby."),
    MATCH_STARTING_IN_20("&e(Tower Defense) &8| &fThe game is starting in &e%time%&f!"),
    MATCH_STARTING_IN_10("&e(Tower Defense) &8| &fThe game is starting in &e%time%&f!"),
    MATCH_STARTING_IN_5("&e(Tower Defense) &8| &fThe game is starting in &e%time%&f!"),
    MATCH_STARTING_IN_3("&e(Tower Defense) &8| &fThe game is starting in &e%time%&f!"),
    MATCH_STARTING_IN_2("&e(Tower Defense) &8| &fThe game is starting in &e%time%&f!"),
    MATCH_STARTING_IN_1("&e(Tower Defense) &8| &fThe game is starting in &e%time%&f!"),
    SPECIFY_ARENA_TO_JOIN("&e(Tower Defense) &8| &fUsage: &e/join <arenaName>"),
    CANT_JOIN_IN_EDIT_MODE("&cYou can't join an arena while editing!"),
    CANT_LEAVE_IN_EDIT_MODE("&cYou can't leave an arena while editing!"),
    PLAYER_LEFT_YOUR_ARENA("&e(Tower Defense) &8| &f%player% left the arena. &e(%inArena%/5)"),
    LEFT_ARENA("&e(Tower Defense) &8| &fYou have left arena %arena%!"),
    ARENA_WIN_ATTACKERS("&e(Tower Defense) &8| &cThe Attackers &fhave successfully destroyed the castle. Better luck next time!"),
    ARENA_WIN_DEFENDERS("&e(Tower Defense) &8| &aThe Defenders &fhave successfully defended the castle. Good job!"),
    ARENA_WIN("&e(Tower Defense) &8| &e%winner% have won this game!"),
    ARENA_IS_FULL("&cThis arena is full, please join another one!"),
    GAME_STARTED("&e(Tower Defense) &8| &fThe game has started!"),
    ARENA_SHOP_NAME("&6➢ &eArena Shop"),
    JOINED_ARENA("&e(Tower Defense) &8| &fYou have joined arena %arena%!"),
    PLAYER_JOINED_YOUR_ARENA("&e(Tower Defense) &8| &f%player% has joined the arena. &e(%inArena%/5)"),
    MUST_BE_IN_GAME_TO_LEAVE("&cYou must be in an Arena in order to leave it!"),
    CANT_JOIN_ARENA("&e(Tower Defense) &8| &fThis arena can't be joined right now!"),
    MATCH_ENDED("&e(Tower Defense) &8| &fThe match has ended. The arena will close for cleanup in 10 seconds."),
    ARENA_SELECTOR_TITLE("&6➢ &eArena Selector"),
    ARENA_SELECTOR_NAME("&6• &eArena #%number%"),
    ARENA_KILL_MESSAGE("&6➢ &e%damaged% &7has been killed by &e%damager%&7."),
    SET_OFF_FIRE("&6➢ &fYou have used a Water Bucket! You're no longer on fire!"),
    SET_OFF_POISON("&6➢ &fYou have used a Milk Bucket! You're no longer being poisoned!"),
    MONEY_ON_KILL("&6➢ &7You have received &e%money% Coins &7by killing &e%player%&7."),
    CHAT_FORMAT_LOBBY("&7(Lobby) &8- &7%player%: &f%message%"),
    CHAT_FORMAT_ARENA("&e(Game) &8- &e%player%: &f%message%"),
    CHAT_NOT_IN_ARENA("&cYou can't use this command as you're currently not in a game or your current game hasn't started yet."),
    CHAT_ATTACKER("&c(Attackers) &8- &c%player%: &f%message%"),
    CHAT_DEFENDER("&a(Defenders) &8- &a%player%: &f%message%"),
    CANT_OPEN_SHOP_IN_COMBAT("&cYou can't open the shop while you are in combat. (%timeLeft% seconds)"),
    CANT_USE_COOLDOWN("&cYou can't use this due to the item being on cooldown. (%timeLeft% seconds)"),
    CHAT_SPECIFY_ARGS("&cYou need to specify a message in order to use this command."),
    SETUP_TRAP("&6➢ &fYou have successfully setup a trap at pointed location."),
    FALL_IN_TRAP("&cYou have fallen into a lava trap set by the Defenders!"),
    NOT_ENOUGH_MONEY("&cYou don't have enoughy money in your purse to purchase this item!"),
    DIE_DUE_TO_LAVA("&6➢ &e%player% &7has burned to death."),
    DIE_DUE_TO_SUICIDE("&6➢ &e%player% &7has committed suicide."),
    DIE_DUE_TO_DROWNING("&6➢ &e%player% &7has drowned to death."),
    DIE_DUE_TO_ZOMBIE("&6➢ &e%player% &7has been slain by a Zombie."),
    DIE_DUE_TO_SKELETON("&6➢ &e%player% &7has been slain by a Skeleton."),
    DIE_DUE_TO_WOLF("&6➢ &e%player% &7has been slain by a Wolf."),
    DIE_DUE_TO_WITCH("&6➢ &e%player% &7has been slain by a Witch."),
    DIE_DUE_TO_GOLEM("&6➢ &e%player% &7has been slain by a Golem."),
    ARENA_WIN_DUE_TO_LEAVE("&e(Tower Defense) &8| &fThis game has ended due to %player% leaving the arena. Starting postgame."),
    SETUP_HUB_BEFORE_SPAWNPOINT("&e(Tower Defense) &8| &fYou have to first setup the game hub before setting its spawnpoint!"),
    COMMAND_TD_ARENA_NOT_NUMBER("&e(Tower Defense) &8| &fArgument `%arg%` is not a number. Please use the command correctly!");


    private String path;
    private String def;
    private static YamlConfiguration CONFIG;

    Lang(String path, String start) {
        this.path = path;
        this.def = start;
    }

    Lang(String start) {
        this.path = this.name().toLowerCase();
        this.def = start;
    }

    public static void setFile(YamlConfiguration config) {
        CONFIG = config;
    }

    @Override
    public String toString() {
        return ChatColor.translateAlternateColorCodes('&', CONFIG.getString(this.path, def));
    }

    public String getDefault() {
        return this.def;
    }

    public String getPath() {
        return this.path;
    }
}
