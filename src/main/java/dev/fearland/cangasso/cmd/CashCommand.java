package dev.fearland.cangasso.cmd;

import dev.fearland.cangasso.cash.CashException;
import dev.fearland.cangasso.cash.CashManager;
import dev.fearland.cangasso.player.role.Role;
import dev.fearland.cangasso.utils.StringUtils;
import dev.fearland.cangasso.utils.enums.EnumSound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CashCommand extends Commands {

  public CashCommand() {
    super("cash"); }

  @Override
  public void perform(CommandSender sender, String label, String[] args) {
    if (args.length == 0) {
      if (sender instanceof Player) {

        sender.sendMessage("§eCash: §b" + StringUtils.formatNumber(CashManager.getCash(sender.getName())));
        return;
      }

      sender.sendMessage(
        "\n§b/cash set <jogador> (cash) §f- §eDefina ou altere a quantidade de cash do jogador." +
           "\n§b/cash add <jogador> (cash) §f- §eAdicione cash a conta do jogador." +
           "\n§b/cash remove <jogador> (cash) §f- §eRemova cash da conta do jogador.\n ");
      return;
    }

    if (!sender.hasPermission("core.cmd.cash")) {
      sender.sendMessage("§eCash: §b" + StringUtils.formatNumber(CashManager.getCash(sender.getName())));
      return;
    }

    String action = args[0];
    if (!action.equalsIgnoreCase("set") && !action.equalsIgnoreCase("add") && !action.equalsIgnoreCase("remove")) {
      sender.sendMessage(
              "\n§b/cash set <jogador> (cash) §f- §eDefina ou altere a quantidade de cash do jogador." +
                      "\n§b/cash add <jogador> (cash) §f- §eAdicione cash a conta do jogador." +
                      "\n§b/cash remove <jogador> (cash) §f- §eRemova cash da conta do jogador.\n ");
      return;
    }

    if (args.length <= 2) {
      sender.sendMessage("§cUtilize /cash " + action + " <jogador> (cash)");
      return;
    }

    long amount = 0L;
    try {
      if (args[2].startsWith("-")) {
        throw new NumberFormatException();
      }

      amount = Long.parseLong(args[2]);
    } catch (NumberFormatException ex) {
      sender.sendMessage("§cUtilize números válidos e positivos.");
      return;
    }

    try {
      switch (action.toLowerCase()) {
        case "set":
          CashManager.setCash(args[1], amount);
          sender.sendMessage("§aVocê definiu o cash de " + Role.getColored(args[1]) + " §apara §b" + StringUtils.formatNumber(amount) + "§a.");
          break;
        case "add":
          CashManager.addCash(args[1], amount);
          sender.sendMessage("§aVocê adicionou §b" + StringUtils.formatNumber(amount) + " §aao saldo de cash de " + Role.getColored(args[1]) + "§a.");
          break;
        case "remove":
          CashManager.removeCash(args[1], amount);
          sender.sendMessage("§aVocê removeu §b" + StringUtils.formatNumber(amount) + " §ado saldo de cash de " + Role.getColored(args[1]) + "§a.");
      }
    } catch (CashException ex) {
      sender.sendMessage("§cO usuário precisa estar conectado.");
    }
  }
}
