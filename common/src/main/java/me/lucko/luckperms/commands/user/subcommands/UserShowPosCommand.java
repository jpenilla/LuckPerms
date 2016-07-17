package me.lucko.luckperms.commands.user.subcommands;

import me.lucko.luckperms.LuckPermsPlugin;
import me.lucko.luckperms.commands.Sender;
import me.lucko.luckperms.commands.Util;
import me.lucko.luckperms.commands.user.UserSubCommand;
import me.lucko.luckperms.constants.Message;
import me.lucko.luckperms.constants.Permission;
import me.lucko.luckperms.tracks.Track;
import me.lucko.luckperms.users.User;

import java.util.List;

public class UserShowPosCommand extends UserSubCommand {
    public UserShowPosCommand() {
        super("showpos", "Shows a users position on a track", "/%s user <user> showpos <track>", Permission.USER_SHOWPOS);
    }

    @Override
    protected void execute(LuckPermsPlugin plugin, Sender sender, User user, List<String> args, String label) {
        final String trackName = args.get(0).toLowerCase();

        plugin.getDatastore().loadTrack(trackName, success -> {
            if (!success) {
                Message.TRACK_DOES_NOT_EXIST.send(sender);
            } else {
                Track track = plugin.getTrackManager().getTrack(trackName);
                if (track == null) {
                    Message.TRACK_DOES_NOT_EXIST.send(sender);
                    return;
                }

                if (track.getSize() <= 1) {
                    Message.TRACK_EMPTY.send(sender);
                    return;
                }

                if (!track.containsGroup(user.getPrimaryGroup())) {
                    Message.TRACK_DOES_NOT_CONTAIN.send(sender, track.getName(), user.getPrimaryGroup());
                    return;
                }

                Message.USER_SHOWPOS.send(sender, user.getName(), track.getName(), Util.listToArrowSep(track.getGroups(), user.getPrimaryGroup()));
            }
        });
    }

    @Override
    public List<String> onTabComplete(Sender sender, List<String> args, LuckPermsPlugin plugin) {
        return getTrackTabComplete(args, plugin);
    }

    @Override
    public boolean isArgLengthInvalid(int argLength) {
        return argLength != 1;
    }
}
