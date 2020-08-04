package cn.wode490390.nukkit.sanctioner;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.PluginIdentifiableCommand;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.lang.TranslationContainer;
import cn.nukkit.network.protocol.MovePlayerPacket;
import cn.nukkit.plugin.Plugin;
import cn.nukkit.utils.TextFormat;

public class CrashCommand extends Command implements PluginIdentifiableCommand {

    private final Plugin plugin;

    public CrashCommand(Plugin plugin) {
        super("crash", "플레이어의 클라이언트를 크래쉬 시킵니다.", "/crash <플레이어>");
        this.setPermission("sanctioner.crash");
        this.getCommandParameters().clear();
        this.addCommandParameters("default", new CommandParameter[]{
                new CommandParameter("player", CommandParamType.TARGET, false)
        });
        this.plugin = plugin;
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        if (!this.plugin.isEnabled() || !this.testPermission(sender)) {
            return false;
        }

        if (args.length > 0) {
            Player player = plugin.getServer().getPlayer(args[0]);
            if (player != null) {
                MovePlayerPacket pk = new MovePlayerPacket();
                pk.eid = player.getId();
                pk.x = pk.y = pk.z = Float.MAX_VALUE;
                pk.mode = MovePlayerPacket.MODE_TELEPORT;
                player.dataPacket(pk);

                Command.broadcastCommandMessage(sender, TextFormat.YELLOW + "대상 플레이어 클라이언트를 크래쉬 시켰습니다. " + args[0] + "'s client");
            } else {
                sender.sendMessage("알 수 없는 유저입니다.");
            }
        } else {
            sender.sendMessage(new TranslationContainer("commands.generic.usage", this.getUsage()));
        }

        return true;
    }

    @Override
    public Plugin getPlugin() {
        return this.plugin;
    }
}
