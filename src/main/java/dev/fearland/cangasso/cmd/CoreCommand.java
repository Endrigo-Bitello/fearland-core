package dev.fearland.cangasso.cmd;

import dev.fearland.cangasso.database.Database;
import dev.fearland.cangasso.utils.SlickUpdater;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import dev.fearland.cangasso.Core;

public class CoreCommand extends Commands {

  public CoreCommand() {
    super("kcore", "kc");
  }

  @Override
  public void perform(CommandSender sender, String label, String[] args) {
    if (sender instanceof Player) {
      Player player = (Player) sender;
      if (!player.hasPermission("fearland.admin")) {
        player.sendMessage("§cComando desconhecido");
        return;
      }

      if (args.length == 0) {
        player.sendMessage("\n §b§lFearLand §b- Opções " +
                "\n \n§e/kc atualizar §f- §7Atualizar o FearCore." +
                "\n§e/kc converter §f- §7Converter seu Banco de Dados.\n ");
        return;
      }

      String action = args[0];
      if (action.equalsIgnoreCase("atualizar")) {
        if (SlickUpdater.UPDATER != null) {
          if (!SlickUpdater.UPDATER.canDownload) {
            player.sendMessage(
              " \n§6§l[FEARLAND]\n \n§aA atualização já está baixada, ela será aplicada na próxima reinicialização do servidor. Caso deseje aplicá-la agora, utilize o comando /stop.\n ");
            return;
          }
          SlickUpdater.UPDATER.canDownload = false;
          SlickUpdater.UPDATER.downloadUpdate(player);
        } else {
          player.sendMessage("§aO plugin já se encontra em sua última versão.");
        }
      } else if (action.equalsIgnoreCase("converter")) {
        player.sendMessage("§fBanco de Dados: " + Database.getInstance().getClass().getSimpleName().replace("Database", ""));
        Database.getInstance().convertDatabase(player);
      } else {
        player.sendMessage("\n §b§lFearLand §b- Opções " +
                "\n \n§e/kc atualizar §f- §7Atualizar o FearCore." +
                "\n§e/kc converter §f- §7Converter seu Banco de Dados.\n ");
      }
    } else {
      sender.sendMessage("§cApenas jogadores podem utilizar este comando.");
    }
  }
}
