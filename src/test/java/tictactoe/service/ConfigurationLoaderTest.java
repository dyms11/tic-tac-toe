package tictactoe.service;


import org.junit.Before;
import org.junit.Test;
import tictactoe.model.GameConfiguration;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.beans.SamePropertyValuesAs.samePropertyValuesAs;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ConfigurationLoaderTest {

    private ConfigurationLoader configurationLoader;
    private ByteArrayOutputStream outContent;

    @Before
    public void setup() {
        outContent = new ByteArrayOutputStream();
        System.setErr(new PrintStream(outContent));
        configurationLoader = new ConfigurationLoader();
    }


    @Test
    public void loadGameConfigurationDefault() {
        final GameConfiguration expectedConfiguration = new GameConfiguration(new Character[] {'O', 'X', 'A'}, 4);
        final Optional<GameConfiguration> configurationOptional = configurationLoader.loadGameConfiguration();

        assertTrue(configurationOptional.isPresent());
        configurationOptional.ifPresent(actualConfiguration -> assertThat(actualConfiguration, samePropertyValuesAs(expectedConfiguration)));
    }

    @Test
    public void loadGameConfigurationCustom() {
        final GameConfiguration expectedConfiguration = new GameConfiguration(new Character[] {'A', 'B', 'C'}, 10);
        final Optional<GameConfiguration> configurationOptional = configurationLoader.loadGameConfiguration("test-success.properties");

        assertTrue(configurationOptional.isPresent());
        configurationOptional.ifPresent(actualConfiguration -> assertThat(actualConfiguration, samePropertyValuesAs(expectedConfiguration)));
    }

    @Test
    public void loadGameConfigurationCustomNonExistentReturnsDefault() {
        final GameConfiguration expectedConfiguration = new GameConfiguration(new Character[] {'O', 'X', 'A'}, 4);
        final Optional<GameConfiguration> configurationOptional = configurationLoader.loadGameConfiguration("test-non-existent.properties");

        assertTrue(configurationOptional.isPresent());
        configurationOptional.ifPresent(actualConfiguration -> assertThat(actualConfiguration, samePropertyValuesAs(expectedConfiguration)));
    }

    @Test
    public void loadGameConfigurationCustomInvalidMinSize() {
        final Optional<GameConfiguration> configurationOptional = configurationLoader.loadGameConfiguration("test-min-size.properties");

        assertFalse(configurationOptional.isPresent());
        assertThat(outContent.toString(), containsString("Configuration file doesn't contain a valid playground.size between 3-10.\n"));
    }

    @Test
    public void loadGameConfigurationCustomInvalidMaxSize() {
        final Optional<GameConfiguration> configurationOptional = configurationLoader.loadGameConfiguration("test-max-size.properties");

        assertFalse(configurationOptional.isPresent());
        assertThat(outContent.toString(), containsString("Configuration file doesn't contain a valid playground.size between 3-10.\n"));
    }

    @Test
    public void loadGameConfigurationCustomMissingMarker() {
        final Optional<GameConfiguration> configurationOptional = configurationLoader.loadGameConfiguration("test-missing-marker.properties");

        assertFalse(configurationOptional.isPresent());
        assertThat(outContent.toString(), containsString("Configuration file doesn't contain 'player.mark.2'\n"));
    }

    @Test
    public void loadGameConfigurationCustomDuplicatedMarker() {
        final Optional<GameConfiguration> configurationOptional = configurationLoader.loadGameConfiguration("test-duplicated-marker.properties");

        assertFalse(configurationOptional.isPresent());
        assertThat(outContent.toString(), containsString("Configuration file has duplicated characters for players\n"));
    }
}
