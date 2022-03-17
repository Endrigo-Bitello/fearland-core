package dev.fearland.cangasso.titles;

import com.mongodb.client.MongoCursor;
import dev.fearland.cangasso.player.Profile;
import dev.fearland.cangasso.utils.BukkitUtils;
import dev.fearland.cangasso.utils.StringUtils;
import dev.fearland.cangasso.database.Database;
import dev.fearland.cangasso.database.MongoDBDatabase;
import org.bson.Document;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Title {

  private String id;
  private String icon;
  private String title;

  public Title(String id, String title, String desc) {
    this.id = id;
    this.icon = "%material%:%durability% : 1 : esconder>tudo : nome>%name% : desc>&fTítulo: " + title + "\n \n" + desc + "\n \n%action%";
    this.title = title;
  }

  public String getId() {
    return this.id;
  }

  public String getTitle() {
    return this.title;
  }

  public void give(Profile profile) {
    if (!this.has(profile)) {
      profile.getTitlesContainer().add(this);
    }
  }

  public boolean has(Profile profile) {
    return profile.getTitlesContainer().has(this);
  }

  public ItemStack getIcon(Profile profile) {
    boolean has = this.has(profile);
    Title selected = profile.getSelectedContainer().getTitle();

    return BukkitUtils.deserializeItemStack(
      this.icon.replace("%material%", has ? (selected != null && selected.equals(this)) ? "MAP" : "EMPTY_MAP" : "STAINED_GLASS_PANE").replace("%durability%", has ? "0" : "14")
        .replace("%name%", (has ? "&a" : "&c") + StringUtils.stripColors(this.title))
        .replace("%action%", (has ? (selected != null && selected.equals(this)) ? "&eClique para deselecionar!" : "&eClique para selecionar!" : "&cVocê não possui este título.")));
  }

  private static final List<Title> TITLES = new ArrayList<>();

  public static void setupTitles() {
    TITLES.add(new Title("tbk", "§cSentinela da Ponte", "&8Pode ser desbloqueado através do\n&8Desafio \"Assassino das Pontes\"&8."));
    TITLES.add(new Title("tbw", "§6Líder da Ponte", "&8Pode ser desbloqueado através do\n&8Desafio \"Glorioso sobre Pontes\"&8."));
    TITLES.add(new Title("tbp", "§ePontuador Mestre", "&8Pode ser desbloqueado através do\n&8Desafio \"Maestria em Pontuação\"&8."));

    TITLES.add(new Title("swk", "§cAnjo da Morte", "&8Pode ser desbloqueado através do\n&8Desafio \"Traidor Celestial\"&8."));
    TITLES.add(new Title("sww", "§bRei Celestial", "&8Pode ser desbloqueado através do\n&8Desafio \"Destrono Celestial\"&8."));
    TITLES.add(new Title("swa", "§6Companheiro de Asas", "&8Pode ser desbloqueado através do\n&8Desafio \"Anjo Guardião\"&8."));

    TITLES.add(new Title("mmd", "§6Sherlock Holmes", "&8Pode ser desbloqueado através do\n&8Desafio \"Detetive\"&8."));
    TITLES.add(new Title("mmk", "§4Jef the Killer", "&8Pode ser desbloqueado através do\n&8Desafio \"Serial Killer\"&8."));

    TITLES.add(new Title("bwk", "§cDestruidor de Sonhos", "&8Pode ser desbloqueado através do\n&8Desafio \"Assasino a espreita\"&8."));
    TITLES.add(new Title("bww", "§6Anjo Sonolento", "&8Pode ser desbloqueado através do\n&8Desafio \"Protetor de Camas\"&8."));
    TITLES.add(new Title("bwp", "§4Pesadelo", "&8Pode ser desbloqueado através do\n&8Desafio \"Freddy Krueger\"&8."));

    if (Database.getInstance() instanceof MongoDBDatabase) {
      MongoDBDatabase database = ((MongoDBDatabase) Database.getInstance());

      MongoCursor<Document> titles = database.getDatabase().getCollection("kCoreTitles").find().cursor();
      while (titles.hasNext()) {
        Document title = titles.next();
        TITLES.add(new Title(title.getString("_id"), title.getString("name"), title.getString("description")));
      }

      titles.close();
    }
  }

  public static Title getById(String id) {
    return TITLES.stream().filter(title -> title.getId().equals(id)).findFirst().orElse(null);
  }

  public static Collection<Title> listTitles() {
    return TITLES;
  }
}
