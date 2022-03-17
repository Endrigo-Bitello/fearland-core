package dev.fearland.cangasso.bungee.cmd;

import dev.fearland.cangasso.bungee.party.BungeePartyManager;
import dev.fearland.cangasso.utils.StringUtils;
import dev.fearland.cangasso.bungee.party.BungeeParty;
import dev.fearland.cangasso.player.role.Role;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class PartyChatCommand extends Commands {

  public PartyChatCommand() {
    super("pc");
  }

  @Override
  public void perform(CommandSender sender, String[] args) {
    if (!(sender instanceof ProxiedPlayer)) {
      sender.sendMessage(TextComponent.fromLegacyText("§cApenas jogadores podem utilizar este comando."));
      return;
    }

    ProxiedPlayer player = (ProxiedPlayer) sender;
    if (args.length == 0) {
      player.sendMessage(TextComponent.fromLegacyText("§cUtilize /pc <mensagem> para conversar com os membros da sua party."));
      return;
    }

    BungeeParty party = BungeePartyManager.getMemberParty(player.getName());
    if (party == null) {
      player.sendMessage(TextComponent.fromLegacyText("§cVocê não encontra-se em uma party."));
      return;
    }

    party.broadcast("§d[Party] " + Role.getPrefixed(player.getName()) + "§f: " + StringUtils.join(args, " "));
  }
}
