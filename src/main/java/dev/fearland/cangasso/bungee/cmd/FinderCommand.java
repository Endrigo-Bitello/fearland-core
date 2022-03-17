package dev.fearland.cangasso.bungee.cmd;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class FinderCommand extends Commands {

    public FinderCommand() {
        super("find");
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if(!sender.hasPermission("core.cmd.find")) {
            sender.sendMessage(new TextComponent(getPermissionMessage()));
            return;
        }
        if(args.length != 1) {
            sender.sendMessage(new TextComponent("§cUtilize /find <jogador>"));
            return;
        }
        ProxiedPlayer player = ProxyServer.getInstance().getPlayer(args[0]);
        if(player == null) {
            sender.sendMessage(new TextComponent("§cJogador não encontrado."));
            return;
        }
        TextComponent component = new TextComponent("§c" + player.getName() + " §efoi encontrado! §6§lCLIQUE AQUI §epara ir até o jogador.");
        component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText("§7Servidor: §9" + player.getServer().getInfo().getName())));
        component.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/go " + player.getName()));
        sender.sendMessage(component);
    } //player.getServer().getInfo().getName().toUpperCase()
}