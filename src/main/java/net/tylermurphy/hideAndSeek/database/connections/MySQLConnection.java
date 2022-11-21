/*
 * This file is part of Kenshins Hide and Seek
 *
 * Copyright (c) 2022 Tyler Murphy.
 *
 * Kenshins Hide and Seek free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * he Free Software Foundation version 3.
 *
 * Kenshins Hide and Seek is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 *
 */

package net.tylermurphy.hideAndSeek.database.connections;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import net.tylermurphy.hideAndSeek.Main;

import java.sql.Connection;
import java.sql.SQLException;

import static net.tylermurphy.hideAndSeek.configuration.Config.*;

public class MySQLConnection implements DatabaseConnection {

    private final HikariDataSource ds;

    public MySQLConnection(){

        HikariConfig config = new HikariConfig();

        Main.getInstance().getLogger().info("Database host: " + databaseHost);
        Main.getInstance().getLogger().info("Database port: " + databasePort);
        Main.getInstance().getLogger().info("Database user: " + databaseUser);
        Main.getInstance().getLogger().info("Database pass: xxxxxxxxxxx");
        Main.getInstance().getLogger().info("Database name: " + databaseName);


        config.setJdbcUrl("jdbc:mariadb://"+databaseHost+":"+databasePort+"/"+databaseName);
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        config.addDataSourceProperty("user", databaseUser);
        config.addDataSourceProperty("password",databasePass);
        config.addDataSourceProperty("autoCommit", "true");
        config.setAutoCommit(true);
        config.setMaximumPoolSize(20);

        ds = new HikariDataSource(config);

    }

    @Override
    public Connection connect() throws SQLException {
        return ds.getConnection();
    }

}
