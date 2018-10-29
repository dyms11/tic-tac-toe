package tictactoe.service;

import tictactoe.model.GameConfiguration;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;

import static tictactoe.model.GameConfiguration.NUMBER_OF_PLAYERS;

public class ConfigurationLoader {
    private static final String DEFAULT_CONFIGURATION_FILE = "application.properties";
    private static final String PLAYER_MARK_PREFIX = "player.mark.";
    private static final String PLAYGROUND_SIZE = "playground.size";
    private static final int PLAYGROUND_SIZE_MIN = 3;
    private static final int PLAYGROUND_SIZE_MAX = 10;

    public Optional<GameConfiguration> loadGameConfiguration() {
        final Properties properties = readResourcePropertiesFile(DEFAULT_CONFIGURATION_FILE);
        return loadGameConfigurationProperties(properties);
    }

    public Optional<GameConfiguration> loadGameConfiguration(final String configurationFile) {
        final Properties properties = getCustomProperties(configurationFile);
        return loadGameConfigurationProperties(properties);
    }

    private Optional<GameConfiguration> loadGameConfigurationProperties(final Properties properties) {
        if (properties != null) {
            final Integer playgroundSize = getIntegerPlaygroundSize(properties.getProperty(PLAYGROUND_SIZE));
            final Character[] playerMarks = getPlayerMarks(properties);

            if (playgroundSize != null && playerMarks.length == NUMBER_OF_PLAYERS) {
                return Optional.of(new GameConfiguration(playerMarks, playgroundSize));
            }
        }

        return Optional.empty();
    }

    private Character[] getPlayerMarks(final Properties properties) {
        final Character[] playerMarks = new Character[NUMBER_OF_PLAYERS];
        final Set<Character> playerMarkSet = new HashSet<>();

        for (int playerNo = 1; playerNo <= NUMBER_OF_PLAYERS; playerNo++) {
            final String playerMark = properties.getProperty(PLAYER_MARK_PREFIX + playerNo);

            if (playerMark == null || playerMark.trim().length() != 1) {
                System.out.println();
                System.err.printf("Configuration file doesn't contain 'player.mark.%d'%n", playerNo);
                return new Character[0];
            }

            playerMarks[playerNo - 1] = playerMark.charAt(0);
            playerMarkSet.add(playerMark.charAt(0));
        }

        if (playerMarkSet.size() != playerMarks.length) {
            System.out.println();
            System.err.println("Configuration file has duplicated characters for players");
            return new Character[0];
        }

        return playerMarks;
    }

    private Integer getIntegerPlaygroundSize(final String playgroundSize) {
        if (playgroundSize != null && playgroundSize.matches("\\d+")) {
            final Integer intPlaygroundSize = Integer.valueOf(playgroundSize);
            if (intPlaygroundSize >= PLAYGROUND_SIZE_MIN && intPlaygroundSize <= PLAYGROUND_SIZE_MAX) {
                return intPlaygroundSize;
            }
        }

        System.out.println();
        System.err.printf("Configuration file doesn't contain a valid playground.size between %d-%d.%n", PLAYGROUND_SIZE_MIN, PLAYGROUND_SIZE_MAX);
        return null;
    }

    private Properties getCustomProperties(final String configurationFile) {
        final Properties externalProperties = readExternalPropertiesFile(configurationFile);
        if (externalProperties != null) {
            return externalProperties;
        }

        final Properties resourceProperties = readResourcePropertiesFile(configurationFile);
        return resourceProperties != null ? resourceProperties
                                          : readResourcePropertiesFile(DEFAULT_CONFIGURATION_FILE);
    }

    private Properties readExternalPropertiesFile(final String configurationFile) {
        try (final InputStream inputStream = new FileInputStream(configurationFile)) {
            final Properties properties = new Properties();
            properties.load(inputStream);

            return properties;
        } catch (final IOException exception) {
            System.out.println();
            System.err.printf("An exception occurred while trying to read configuration file '%s'.%n", configurationFile);
        }

        return null;
    }

    private Properties readResourcePropertiesFile(final String configurationFile) {
        final ClassLoader classLoader = getClass().getClassLoader();
        try (final InputStream resourceAsStream = classLoader.getResourceAsStream(configurationFile)) {
            if (resourceAsStream != null) {
                final Properties properties = new Properties();
                properties.load(resourceAsStream);

                return properties;
            }
        } catch (final IOException exception) {
            System.out.println();
            System.err.printf("An exception occurred while trying to read configuration file '%s'.%n", configurationFile);
        }

        return null;
    }
}
