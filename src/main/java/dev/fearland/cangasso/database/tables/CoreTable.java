package dev.fearland.cangasso.database.tables;

import dev.fearland.cangasso.database.data.interfaces.DataTableInfo;
import dev.fearland.cangasso.database.Database;
import dev.fearland.cangasso.database.HikariDatabase;
import dev.fearland.cangasso.database.MySQLDatabase;
import dev.fearland.cangasso.database.data.DataContainer;
import dev.fearland.cangasso.database.data.DataTable;

import java.util.LinkedHashMap;
import java.util.Map;

@DataTableInfo(
    name = "kCoreProfile",
    create = "CREATE TABLE IF NOT EXISTS `kCoreProfile` (`name` VARCHAR(32), `cash` LONG, `role` TEXT, `deliveries` TEXT, `preferences` TEXT, `titles` TEXT, `boosters` TEXT, `achievements` TEXT, `selected` TEXT, `created` LONG, `tag` TEXT, `lastlogin` LONG, PRIMARY KEY(`name`)) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_bin;",
    select = "SELECT * FROM `kCoreProfile` WHERE LOWER(`name`) = ?",
    insert = "INSERT INTO `kCoreProfile` VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", //13
    update = "UPDATE `kCoreProfile` SET `cash` = ?, `role` = ?, `deliveries` = ?, `preferences` " +
            "= ?, `titles` = ?, `boosters` = ?, `achievements` = ?, `selected` = ?, `created` = ?, `tag` = ?, `lastlogin` = ? WHERE LOWER(`name`) = ?")
public class CoreTable extends DataTable {

  @Override
  public void init(Database database) {
    if (database instanceof MySQLDatabase) {
      if (((MySQLDatabase) database).query("SHOW COLUMNS FROM `kCoreProfile` LIKE 'cash'") == null) {
        ((MySQLDatabase) database).execute("ALTER TABLE `kCoreProfile` ADD `cash` LONG AFTER `name`");
      }
    } else if (database instanceof HikariDatabase) {
      if (((HikariDatabase) database).query("SHOW COLUMNS FROM `kCoreProfile` LIKE 'cash'") == null) {
        ((HikariDatabase) database).execute("ALTER TABLE `kCoreProfile` ADD `cash` LONG AFTER `name`");
      }
    }
  }

  public Map<String, DataContainer> getDefaultValues() {
    Map<String, DataContainer> defaultValues = new LinkedHashMap<>();
    defaultValues.put("cash", new DataContainer(0L));
    defaultValues.put("role", new DataContainer("Membro"));
    defaultValues.put("deliveries", new DataContainer("{}"));
    defaultValues.put("preferences", new DataContainer("{\"pv\": 0, \"pm\": 0, \"bg\": 0, \"pl\": 0, \"fl\": 0, \"mt\": 0, \"ae\": 0, \"cr\": 0, \"fr\": 0}"));
    defaultValues.put("titles", new DataContainer("[]"));
    defaultValues.put("boosters", new DataContainer("{}"));
    defaultValues.put("achievements", new DataContainer("[]"));
    defaultValues.put("selected", new DataContainer("{\"title\": \"0\", \"icon\": \"0\"}"));
    defaultValues.put("created", new DataContainer(System.currentTimeMillis()));
    defaultValues.put("tag", new DataContainer(""));
    defaultValues.put("lastlogin", new DataContainer(System.currentTimeMillis()));
    return defaultValues;
  }
}
