package dev.fearland.cangasso.database.tables;

import dev.fearland.cangasso.database.Database;
import dev.fearland.cangasso.database.MySQLDatabase;
import dev.fearland.cangasso.database.data.DataContainer;
import dev.fearland.cangasso.database.data.DataTable;
import dev.fearland.cangasso.database.data.interfaces.DataTableInfo;
import dev.fearland.cangasso.database.HikariDatabase;

import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.Map;

@DataTableInfo(
        name = "kCoreSkyWars",
        create = "CREATE TABLE IF NOT EXISTS `kCoreSkyWars` (`name` VARCHAR(32), `1v1kills` LONG, `1v1deaths` LONG, `1v1assists` LONG, `1v1games` LONG, `1v1wins` LONG, `2v2kills` LONG, `2v2deaths` LONG, `2v2assists` LONG, `2v2games` LONG, `2v2wins` LONG, `rankedkills` LONG, `rankeddeaths` LONG, `rankedassists` LONG, `rankedgames` LONG, `rankedwins` LONG, `rankedpoints` LONG, `monthlykills` LONG, `monthlydeaths` LONG, `monthlypoints` LONG, `monthlyassists` LONG, `monthlywins` LONG, `monthlygames` LONG, `month` TEXT, `coins` DOUBLE, `souls` DOUBLE, `maxsouls` DOUBLE, `winsouls` DOUBLE, `wellsouls` INT, `lastmap` LONG, `cosmetics` TEXT, `selected` TEXT, `kitconfig` TEXT, PRIMARY KEY(`name`)) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_bin;",
        select = "SELECT * FROM `kCoreSkyWars` WHERE LOWER(`name`) = ?",
        insert = "INSERT INTO `kCoreSkyWars` VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
        update = "UPDATE `kCoreSkyWars` SET `1v1kills` = ?, `1v1deaths` = ?, `1v1assists` = ?, `1v1games` = ?, `1v1wins` = ?, `2v2kills` = ?, `2v2deaths` = ?, `2v2assists` = ?, `2v2games` = ?, `2v2wins` = ?, `rankedkills` = ?, `rankeddeaths` = ?, `rankedassists` = ?, `rankedgames` = ?, `rankedwins` = ?, `rankedpoints` = ?, `monthlykills` = ?, `montlhydeaths` = ?, `monthlypoints` = ?, `monthlyassists` = ?, `monthlywins` = ?, `monthlygames` = ?, `month` = ?, `coins` = ?, `souls` = ?, `maxsouls` = ?, `winsouls` = ?, `wellsouls` = ?, `lastmap` = ?, `cosmetics` = ?, `selected` = ?, `kitconfig` = ? WHERE LOWER(`name`) = ?"
)
public class SkyWarsTable extends DataTable {

  @Override
  public void init(Database database) {
    if (database instanceof MySQLDatabase) {
      if (((MySQLDatabase) database).query("SHOW COLUMNS FROM `kCoreSkyWars` LIKE 'lastmap'") == null) {
        ((MySQLDatabase) database).execute(
                "ALTER TABLE `kCoreSkyWars` ADD `lastmap` LONG DEFAULT 0 AFTER `coins`, ADD `kitconfig` TEXT AFTER `selected`");
      }
    } else if (database instanceof HikariDatabase) {
      if (((HikariDatabase) database).query("SHOW COLUMNS FROM `kCoreSkyWars` LIKE 'lastmap'") == null) {
        ((HikariDatabase) database).execute(
                "ALTER TABLE `kCoreSkyWars` ADD `lastmap` LONG DEFAULT 0 AFTER `coins`, ADD `kitconfig` TEXT AFTER `selected`");
      }
    }
  }

  public Map<String, DataContainer> getDefaultValues() {
    Map<String, DataContainer> defaultValues = new LinkedHashMap<>();
    defaultValues.put("1v1kills", new DataContainer(0L));
    defaultValues.put("1v1deaths", new DataContainer(0L));
    defaultValues.put("1v1assists", new DataContainer(0L));
    defaultValues.put("1v1games", new DataContainer(0L));
    defaultValues.put("1v1wins", new DataContainer(0L));
    defaultValues.put("2v2kills", new DataContainer(0L));
    defaultValues.put("2v2deaths", new DataContainer(0L));
    defaultValues.put("2v2assists", new DataContainer(0L));
    defaultValues.put("2v2games", new DataContainer(0L));
    defaultValues.put("2v2wins", new DataContainer(0L));
    defaultValues.put("rankedkills", new DataContainer(0L));
    defaultValues.put("rankeddeaths", new DataContainer(0L));
    defaultValues.put("rankedassists", new DataContainer(0L));
    defaultValues.put("rankedgames", new DataContainer(0L));
    defaultValues.put("rankedwins", new DataContainer(0L));
    defaultValues.put("rankedpoints", new DataContainer(0L));
    if (Database.getInstance() instanceof MySQLDatabase || Database.getInstance() instanceof HikariDatabase) {
      String assists = "assists";
      for (String stats : new String[] {"kills", "deaths", "points", assists, "wins", "games"}) {
        defaultValues.put("monthly" + stats, new DataContainer(0L));
      }
      defaultValues.put("month", new DataContainer((Calendar.getInstance().get(Calendar.MONTH) + 1) + "/" + Calendar.getInstance().get(Calendar.YEAR)));
    }
    defaultValues.put("coins", new DataContainer(0L));
    defaultValues.put("souls", new DataContainer(0L));
    defaultValues.put("maxsouls", new DataContainer(100L));
    defaultValues.put("winsouls", new DataContainer(0L));
    defaultValues.put("wellsouls", new DataContainer(1));
    defaultValues.put("lastmap", new DataContainer(0L));
    defaultValues.put("cosmetics", new DataContainer("{}"));
    defaultValues.put("selected", new DataContainer("{}"));
    defaultValues.put("kitconfig", new DataContainer("{}"));
    return defaultValues;
  }
}
