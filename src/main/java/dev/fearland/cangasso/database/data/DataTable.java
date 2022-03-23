package dev.fearland.cangasso.database.data;

import dev.fearland.cangasso.database.data.interfaces.DataTableInfo;
import dev.fearland.cangasso.database.Database;
import dev.fearland.cangasso.database.tables.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public abstract class DataTable {

  public abstract void init(Database database);

  public abstract Map<String, DataContainer> getDefaultValues();

  public DataTableInfo getInfo() {
    return this.getClass().getAnnotation(DataTableInfo.class);
  }

  private static final List<DataTable> TABLES = new ArrayList<>();

  static {
    TABLES.add(new CoreTable());
    TABLES.add(new SkyWarsTable());
    TABLES.add(new BedWarsTable());
    TABLES.add(new TheBridgeTable());
    TABLES.add(new MurderTable());
    TABLES.add(new BuildBattleTable());
  }

  public static void registerTable(DataTable table) {
    TABLES.add(table);
  }

  public static Collection<DataTable> listTables() {
    return TABLES;
  }
}
