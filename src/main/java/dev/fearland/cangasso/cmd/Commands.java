package dev.fearland.cangasso.cmd;

import dev.fearland.cangasso.cash.CashManager;
import dev.fearland.cangasso.player.fake.FakeManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.SimpleCommandMap;
import dev.fearland.cangasso.Core;

import java.util.Arrays;
import java.util.logging.Level;

public abstract class Commands extends Command {

  public Commands(String name, String... aliases) {
    super(name);
    this.setAliases(Arrays.asList(aliases));

    try {
      SimpleCommandMap simpleCommandMap = (SimpleCommandMap) Bukkit.getServer().getClass().getDeclaredMethod("getCommandMap").invoke(Bukkit.getServer());
      simpleCommandMap.register(this.getName(), "kcore", this);
    } catch (ReflectiveOperationException ex) {
      Core.getInstance().getLogger().log(Level.SEVERE, "Cannot register command: ", ex);
    }
  }

  public abstract void perform(CommandSender sender, String label, String[] args);

  @Override
  public boolean execute(CommandSender sender, String commandLabel, String[] args) {
    this.perform(sender, commandLabel, args);
    return true;
  }

  public static void setupCommands() {
    new CoreCommand();
    new CoinsCommand();
    if (CashManager.CASH) {
      new CashCommand();
    }
    if (!FakeManager.isBungeeSide()) {
      new FakeCommand();
      new PartyCommand();
    }
  }
}
