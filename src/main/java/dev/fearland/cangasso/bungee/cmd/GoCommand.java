package dev.fearland.cangasso.bungee.cmd;

import dev.fearland.cangasso.bungee.Bungee;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.concurrent.TimeUnit;

public class GoCommand extends Commands {

    public GoCommand() {
        super("go");
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if(!(sender instanceof ProxiedPlayer))
            return;
        if(!sender.hasPermission("core.cmd.go")) {
            sender.sendMessage(new TextComponent(getPermissionMessage()));
            return;
        }
        if(args.length != 1) {
            sender.sendMessage(new TextComponent("§cUtilize /go <jogador>"));
            return;
        }
        ProxiedPlayer player = (ProxiedPlayer) sender, target = ProxyServer.getInstance().getPlayer(args[0]);
        if(target == null) {
            player.sendMessage(new TextComponent("§cEste jogador está offline."));
            return;
        }
        ServerInfo info;
        boolean b = player.getServer().getInfo().equals((info=target.getServer().getInfo()));
        if(!b)
            player.connect(info);
        ProxyServer.getInstance().getScheduler().schedule(Bungee.getInstance(), () -> Bungee.teleport(player, target), b?0:1, TimeUnit.SECONDS);
        player.sendMessage(new TextComponent("§eTeleportado para §c" + target.getName() + "§e."));
    }
}