package com.umbr3114.common;

import com.umbr3114.errors.DatabaseConnectionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Properties;

/**
 * Responsible for loading database host/credentials from either JAR or Environment variables
 */
public class DatabaseConnectionDetails {
    private String dbUsername;
    private String dbPassword;
    private String dbHost;
    public static final String DEFAULT_HOST = "cluster0-urisd.gcp.mongodb.net";
    private Logger log;

    public DatabaseConnectionDetails() {
        log = LoggerFactory.getLogger("DatabaseConnectionInit");
        // attempt from JAR first, then attempt from environment
        try {
            fromJarProperties();
        } catch(IOException e) {
            log.warn("Could not load credentials from Jar file, attempting environment variables");
            fromEnvironment();
        }
        credentialSanityCheck();
    }

    private void fromEnvironment() {
        this.dbUsername = System.getenv("MONGO_USER");
        this.dbPassword = System.getenv("MONGO_PASS");
        this.dbHost = System.getenv("MONGO_HOST");
    }

    private void fromJarProperties() throws IOException {
        Properties dbProperties = getPropertiesFromJar();
        String dbUserKey = "mongo.user";
        String dbPassKey = "mongo.password";
        String dbHostKey = "mongo.host";

        // set everything
        this.dbUsername = dbProperties.getProperty(dbUserKey);
        this.dbPassword = dbProperties.getProperty(dbPassKey);
        this.dbHost = dbProperties.getProperty(dbHostKey);
    }

    private Properties getPropertiesFromJar() throws IOException {
        Properties jarProps;
        jarProps = new Properties();
        try {
            jarProps.load(getClass().getClassLoader().getResourceAsStream("dbconnection.properties"));
        } catch (NullPointerException e) {
            throw new IOException();
        }
        return jarProps;
    }

    private void credentialSanityCheck() {
        if (dbHost == null || dbHost.isEmpty() || dbHost.equals("<MONGOHOST>")) {
            log.warn("Configured with bad host, attempting default");
            dbHost = DEFAULT_HOST;
        }

        if (
                (dbUsername == null || dbUsername.isEmpty() || dbUsername.equals("<MONGOUSER>"))
                        && (dbPassword == null || dbPassword.isEmpty() || dbPassword.equals("<MONGOPASS>"))
        ) {
            log.error("Application configured with bad database credentials");
            throw new DatabaseConnectionException("Application configured with bad credentials");
        }
    }

    public String getUsername() {
        return dbUsername;
    }

    public String getPassword() {
        return dbPassword;
    }

    public String getHost() {
        return dbHost;
    }
}
