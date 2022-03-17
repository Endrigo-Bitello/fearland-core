package dev.fearland.cangasso.bungee.cmd;

import dev.fearland.cangasso.party.PartyRole;
import dev.fearland.cangasso.player.Profile;
import dev.fearland.cangasso.player.enums.PartyRequest;
import dev.fearland.cangasso.utils.StringUtils;
import dev.fearland.cangasso.bungee.party.BungeeParty;
import dev.fearland.cangasso.bungee.party.BungeePartyManager;
import dev.fearland.cangasso.player.role.Role;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import dev.fearland.cangasso.Manager;
import org.bukkit.Bukkit;

import javax.xml.soap.Text;
import java.util.List;
import java.util.stream.Collectors;

public class PartyCommand extends Commands {

  public PartyCommand() {
    super("party");
  }

  @Override
  public void perform(CommandSender sender, String[] args) {
    if (!(sender instanceof ProxiedPlayer)) {
      sender.sendMessage(TextComponent.fromLegacyText("§cApenas jogadores podem utilizar este comando."));
      return;
    }

    ProxiedPlayer player = (ProxiedPlayer) sender;
    if (args.length == 0) {
      player.sendMessage(TextComponent.fromLegacyText(
                      "\n§b/pc <mensagem> §f- §eComunicar-se com os membros." +
                      "\n§b/party abrir §f- §eTornar a party pública." +
                      "\n§b/party fechar §f- §eTornar a party privada." +
                      "\n§b/party entrar <jogador> §f- §eEntrar em uma party pública." +
                      "\n§b/party aceitar <jogador> §f- §eAceitar uma solicitação." +
                      "\n§b/party ajuda §f- §eMostrar essa mensagem de ajuda." +
                      "\n§b/party convidar <jogador> §f- §eConvidar um jogador." +
                      "\n§b/party deletar §f- §eDeletar a party." +
                      "\n§b/party expulsar <jogador> §f- §eExpulsar um membro." +
                      "\n§b/party info §f- §eInformações da sua Party." +
                      "\n§b/party negar <jogador> §f- §eRecuse o convite para party." +
                      "\n§b/party sair §f- §eSaia da party." +
                      "\n§b/party transferir <jogador> §f- §eTransfira a posse da party para um membro.\n "));
      return;
    }

    String action = args[0];
    if (action.equalsIgnoreCase("abrir") || action.equalsIgnoreCase("open")) {
      BungeeParty party = BungeePartyManager.getMemberParty(player.getName());
      if (party == null) {
        player.sendMessage(TextComponent.fromLegacyText("§cVocê não pertence a uma party."));
        return;
      }

      if (!party.isLeader(player.getName())) {
        player.sendMessage(TextComponent.fromLegacyText("§cVocê não é o Líder da party."));
        return;
      }

      if (party.isOpen()) {
        player.sendMessage(TextComponent.fromLegacyText("§cSua party já é pública."));
        return;
      }

      party.setIsOpen(true);
      player.sendMessage(TextComponent.fromLegacyText("§aVocê abriu a party para qualquer jogador."));
      TextComponent component = new TextComponent("");
      for (BaseComponent components : TextComponent.fromLegacyText("\n§r" + Role.getColored(player.getName() + " §eabriu sua party para todos os jogadores!"))) ;
      component.addExtra(component);

      Bukkit.broadcastMessage("\n"+ Role.getColored(player.getName()) + " §eabriu sua party para todos os jogadores!\nPara entrar, digite §b/party entrar " + player.getName() + "§e.\n ");

    } else if (action.equalsIgnoreCase("fechar") || action.equalsIgnoreCase("close")) {
      BungeeParty party = BungeePartyManager.getMemberParty(player.getName());
      if (party == null) {
        player.sendMessage(TextComponent.fromLegacyText("§cVocê não pertence a uma party."));
        return;
      }

      if (!party.isLeader(player.getName())) {
        player.sendMessage(TextComponent.fromLegacyText("§cVocê não é o Líder da party."));
        return;
      }

      if (!party.isOpen()) {
        player.sendMessage(TextComponent.fromLegacyText("§cSua party já é privada."));
        return;
      }

      party.setIsOpen(false);
      player.sendMessage(TextComponent.fromLegacyText("§cVocê fechou a party para apenas convidados."));
    } else if (action.equalsIgnoreCase("entrar") || action.equalsIgnoreCase("join")) {
      if (args.length == 1) {
        player.sendMessage(TextComponent.fromLegacyText("§cUtilize /party entrar <jogador>"));
        return;
      }

      String target = args[1];
      if (target.equalsIgnoreCase(player.getName())) {
        player.sendMessage(TextComponent.fromLegacyText("§cVocê não pode entrar na party de você mesmo."));
        return;
      }

      BungeeParty party = BungeePartyManager.getMemberParty(player.getName());
      if (party != null) {
        player.sendMessage(TextComponent.fromLegacyText("§cVocê já pertence a uma Party."));
        return;
      }

      party = BungeePartyManager.getLeaderParty(target);
      if (party == null) {
        player.sendMessage(TextComponent.fromLegacyText("§c" + Manager.getCurrent(target) + " não é o líder de party."));
        return;
      }

      target = party.getName(target);
      if (!party.isOpen()) {
        player.sendMessage(TextComponent.fromLegacyText("§cA party de " + Manager.getCurrent(target) + " é privada."));
        return;
      }

      if (!party.canJoin()) {
        player.sendMessage(TextComponent.fromLegacyText("§cA party de " + Manager.getCurrent(target) + " está lotada."));
        return;
      }

      party.join(player.getName());
      player.sendMessage(TextComponent.fromLegacyText("§eVocê juntou-se a party de " + Role.getColored(target) + "§e."));
    } else if (action.equalsIgnoreCase("aceitar") || action.equalsIgnoreCase("accept")) {
      if (args.length == 1) {
        player.sendMessage(TextComponent.fromLegacyText("§cUtilize /party aceitar <jogador>"));
        return;
      }

      String target = args[1];
      if (target.equalsIgnoreCase(player.getName())) {
        player.sendMessage(TextComponent.fromLegacyText("§cVocê não pode aceitar convites de si mesmo."));
        return;
      }

      BungeeParty party = BungeePartyManager.getMemberParty(player.getName());
      if (party != null) {
        player.sendMessage(TextComponent.fromLegacyText("§cVocê já pertence a uma party."));
        return;
      }

      party = BungeePartyManager.getLeaderParty(target);
      if (party == null) {
        player.sendMessage(TextComponent.fromLegacyText("§c" + Manager.getCurrent(target) + " não é o líder de party."));
        return;
      }

      target = party.getName(target);
      if (!party.isInvited(player.getName())) {
        player.sendMessage(TextComponent.fromLegacyText("§c" + Manager.getCurrent(target) + " não convidou você para party."));
        return;
      }

      if (!party.canJoin()) {
        player.sendMessage(TextComponent.fromLegacyText("§cA party de " + Manager.getCurrent(target) + " está lotada."));
        return;
      }

      party.join(player.getName());
      player.sendMessage(TextComponent.fromLegacyText("§aVocê entrou na party de " + Role.getPrefixed(target) + "§a."));
    } else if (action.equalsIgnoreCase("ajuda")) {
      player.sendMessage(TextComponent.fromLegacyText(
              "\n§b/pc <mensagem> §f- §eComunicar-se com os membros." +
                      "\n§b/party abrir §f- §eTornar a party pública." +
                      "\n§b/party fechar §f- §eTornar a party privada." +
                      "\n§b/party entrar <jogador> §f- §eEntrar em uma party pública." +
                      "\n§b/party aceitar <jogador> §f- §eAceitar uma solicitação." +
                      "\n§b/party ajuda §f- §eMostrar essa mensagem de ajuda." +
                      "\n§b/party convidar <jogador> §f- §eConvidar um jogador." +
                      "\n§b/party deletar §f- §eDeletar a party." +
                      "\n§b/party expulsar <jogador> §f- §eExpulsar um membro." +
                      "\n§b/party info §f- §eInformações da sua Party." +
                      "\n§b/party negar <jogador> §f- §eRecuse o convite para party." +
                      "\n§b/party sair §f- §eSaia da party." +
                      "\n§b/party transferir <jogador> §f- §eTransfira a posse da party para um membro.\n "));
    } else if (action.equalsIgnoreCase("puxar") || action.equalsIgnoreCase("push")) {
      BungeeParty party = BungeePartyManager.getMemberParty(player.getName());
      if (party == null) {
        player.sendMessage(TextComponent.fromLegacyText("§cVocê não pertence a uma party."));
        return;
      }

      if (!party.isLeader(player.getName())) {
        player.sendMessage(TextComponent.fromLegacyText("§cVocê não é o líder da party."));
        return;
      }

      party.listMembers().forEach(member -> ProxyServer.getInstance().getPlayer(member.getName()).connect(player.getServer().getInfo()));
      party.broadcast("§dParty> " + Role.getColored(player.getName() + " §etrouxe todos os membros para o seu servidor."));
    } else if (action.equalsIgnoreCase("deletar") || action.equalsIgnoreCase("delete")) {
      BungeeParty party = BungeePartyManager.getMemberParty(player.getName());
      if (party == null) {
        player.sendMessage(TextComponent.fromLegacyText("§cVocê não pertence a uma party."));
        return;
      }

      if (!party.isLeader(player.getName())) {
        player.sendMessage(TextComponent.fromLegacyText("§cVocê não é o líder da party."));
        return;
      }

      party.broadcast("§dParty> " + Role.getColored(player.getName()) + " §cdeletou a party.", true);
      party.delete();
      player.sendMessage(TextComponent.fromLegacyText("§aVocê deletou a party."));
    } else if (action.equalsIgnoreCase("expulsar") || action.equalsIgnoreCase("kick")) {
      if (args.length == 1) {
        player.sendMessage(TextComponent.fromLegacyText("§cUtilize /party expulsar <jogador>"));
        return;
      }

      BungeeParty party = BungeePartyManager.getLeaderParty(player.getName());
      if (party == null) {
        player.sendMessage(TextComponent.fromLegacyText("§cVocê não é um líder de party."));
        return;
      }

      String target = args[1];
      if (target.equalsIgnoreCase(player.getName())) {
        player.sendMessage(TextComponent.fromLegacyText("§cVocê não pode se expulsar."));
        return;
      }

      if (!party.isMember(target)) {
        player.sendMessage(TextComponent.fromLegacyText("§cEsse jogador não pertence a sua Party."));
        return;
      }

      target = party.getName(target);
      party.kick(target);
      party.broadcast("§eParty> " + Role.getColored(player.getName()) + " §eexpulsou " + Role.getColored(target) + " §eda party.");
    } else if (action.equalsIgnoreCase("info")) {
      BungeeParty party = BungeePartyManager.getMemberParty(player.getName());
      if (party == null) {
        player.sendMessage(TextComponent.fromLegacyText("§cVocê não pertence a uma party."));
        return;
      }

      List<String> members = party.listMembers().stream().filter(pp -> pp.getRole() != PartyRole.LEADER).map(pp -> (pp.isOnline() ? "§a" : "§c") + pp.getName()).collect(Collectors.toList());
      player.sendMessage(TextComponent.fromLegacyText(
        " \n§aDono: " + Role.getPrefixed(party.getLeader()) +
        "\n§aTipo: " + (party.isOpen() ? "§7Pública" : "§7Privada") +
        "\n§aLimite de membros: §7" + party.listMembers()
          .size() + "/" + party.getSlots() + "\n§aMembros: §7" + StringUtils.join(members, "§7, ") + "\n "));
    } else if (action.equalsIgnoreCase("negar") || action.equalsIgnoreCase("deny")) {
      if (args.length == 1) {
        player.sendMessage(TextComponent.fromLegacyText("§cUtilize /party negar <jogador>"));
        return;
      }

      String target = args[1];
      if (target.equalsIgnoreCase(player.getName())) {
        player.sendMessage(TextComponent.fromLegacyText("§cVocê não pode negar convites de si mesmo."));
        return;
      }

      BungeeParty party = BungeePartyManager.getMemberParty(player.getName());
      if (party != null) {
        player.sendMessage(TextComponent.fromLegacyText("§cVocê já pertence a uma party."));
        return;
      }

      party = BungeePartyManager.getLeaderParty(target);
      if (party == null) {
        player.sendMessage(TextComponent.fromLegacyText("§c" + Manager.getCurrent(target) + " não é o líder da party."));
        return;
      }

      target = party.getName(target);
      if (!party.isInvited(player.getName())) {
        player.sendMessage(TextComponent.fromLegacyText("§c" + Manager.getCurrent(target) + " não convidou você para party."));
        return;
      }

      party.reject(player.getName());
      player.sendMessage(TextComponent.fromLegacyText("§cVocê recusou o convite para a party de " + Role.getColored(target) + "§c."));
    } else if (action.equalsIgnoreCase("sair") || action.equalsIgnoreCase("leave")) {
      BungeeParty party = BungeePartyManager.getMemberParty(player.getName());
      if (party == null) {
        player.sendMessage(TextComponent.fromLegacyText("§cVocê não pertence a uma party."));
        return;
      }

      party.leave(player.getName());
      player.sendMessage(TextComponent.fromLegacyText("§aVocê saiu da party."));
    } else if (action.equalsIgnoreCase("transferir") || action.equalsIgnoreCase("transfer")) {
      if (args.length == 1) {
        player.sendMessage(TextComponent.fromLegacyText("§cUtilize /party transferir <jogador>"));
        return;
      }

      BungeeParty party = BungeePartyManager.getLeaderParty(player.getName());
      if (party == null) {
        player.sendMessage(TextComponent.fromLegacyText("§cVocê não é um líder de party."));
        return;
      }

      String target = args[1];
      if (target.equalsIgnoreCase(player.getName())) {
        player.sendMessage(TextComponent.fromLegacyText("§cVocê já tem a posse dessa party."));
        return;
      }

      if (!party.isMember(target)) {
        player.sendMessage(TextComponent.fromLegacyText("§cEsse jogador não pertence a sua party."));
        return;
      }

      target = party.getName(target);
      party.transfer(target);
      party.broadcast("§dParty> " + Role.getColored(player.getName()) + " §etransferiu a posse da party para " + Role.getColored(target) + "§e.");
    } else {
      if (action.equalsIgnoreCase("convidar") || action.equalsIgnoreCase("invite")) {
        if (args.length == 1) {
          player.sendMessage(TextComponent.fromLegacyText("§cUtilize /party convidar <jogador>"));
          return;
        }

        action = args[1];
      }

      ProxiedPlayer target = ProxyServer.getInstance().getPlayer(action);
      if (target == null) {
        player.sendMessage(TextComponent.fromLegacyText("§cUsuário não encontrado."));
        return;
      }

      action = target.getName();
      if (action.equalsIgnoreCase(player.getName())) {
        player.sendMessage(TextComponent.fromLegacyText("§cVocê não pode enviar convites para si mesmo."));
        return;
      }

      BungeeParty party = BungeePartyManager.getMemberParty(player.getName());
      if (party == null) {
        party = BungeePartyManager.createParty(player);
      }

      if (!party.isLeader(player.getName())) {
        player.sendMessage(TextComponent.fromLegacyText("§cApenas o líder da party pode enviar convites!"));
        return;
      }

      if (!party.canJoin()) {
        player.sendMessage(TextComponent.fromLegacyText("§cA sua party está lotada."));
        return;
      }

      if (party.isInvited(action)) {
        player.sendMessage(TextComponent.fromLegacyText("§cVocê já enviou um convite para " + Manager.getCurrent(action) + "."));
        return;
      }

      if (BungeePartyManager.getMemberParty(action) != null) {
        player.sendMessage(TextComponent.fromLegacyText("§c" + Manager.getCurrent(action) + " já pertence a uma Party."));
        return;
      }

      Profile profilet = Profile.getProfile(target.getName());
      if(profilet.getPreferencesContainer().getPartyRequest().equals(PartyRequest.DESATIVADO)) {
        player.sendMessage(TextComponent.fromLegacyText("§cEsse jogador não quer receber convites para party."));
        return;
      }

      party.invite(target);
      player.sendMessage(
        TextComponent.fromLegacyText(" \n" + Role.getColored(action) + " §efoi convidado para a party, ele tem 60 segundos para responder ao convite.\n "));
    }
  }
}
