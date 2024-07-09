package qc.novaclient;

import fr.flowarg.flowupdater.FlowUpdater;
import fr.flowarg.flowupdater.utils.ModFileDeleter;
import fr.flowarg.flowupdater.utils.UpdaterOptions;
import fr.flowarg.flowupdater.versions.AbstractForgeVersion;
import fr.flowarg.flowupdater.versions.ForgeVersionBuilder;
import fr.flowarg.flowupdater.versions.VanillaVersion;
import fr.flowarg.openlauncherlib.NoFramework;
import fr.litarvan.openauth.microsoft.MicrosoftAuthResult;
import fr.litarvan.openauth.microsoft.MicrosoftAuthenticator;
import fr.theshark34.openlauncherlib.minecraft.*;
import qc.novaclient.utils.ConfigReader;

import java.io.File;
import java.nio.file.Path;

public class Launcher {
    private final static GameInfos gameInfos = new GameInfos("NovaAlcyone",
            new GameVersion("1.12.2", GameType.V1_13_HIGHER_FORGE), new GameTweak[] { GameTweak.FORGE });
    private final static Path path = gameInfos.getGameDir();
    public static File crashFile = new File(String.valueOf(path), "crashes");
    private static AuthInfos authInfos;

    public static void update() throws Exception {

        VanillaVersion vanillaVersion = new VanillaVersion.VanillaVersionBuilder().withName("1.12.2").build();
        UpdaterOptions options = new UpdaterOptions.UpdaterOptionsBuilder().build();

        AbstractForgeVersion version = new ForgeVersionBuilder(ForgeVersionBuilder.ForgeVersionType.NEW)
                .withMods("https://novaalcyone.com/storage/lanceur/mods.php").withFileDeleter(new ModFileDeleter(true))
                .withForgeVersion("14.23.5.2859").build();

        FlowUpdater updater = new FlowUpdater.FlowUpdaterBuilder().withVanillaVersion(vanillaVersion)
                .withUpdaterOptions(options).withModLoaderVersion(version).build();
        updater.update(path);

    }

    public static void launch() throws Exception {
        MicrosoftAuthenticator microsoftAuthenticator = new MicrosoftAuthenticator();
        final String refresh_token = ConfigReader.getRefreshToken();
        MicrosoftAuthResult result = microsoftAuthenticator.loginWithRefreshToken(refresh_token);
        authInfos = new AuthInfos(result.getProfile().getName(), result.getAccessToken(), result.getProfile().getId());
        NoFramework noFramework = new NoFramework(path, authInfos, GameFolder.FLOW_UPDATER);
        noFramework.launch("1.12.2", "14.23.5.2859", NoFramework.ModLoader.FORGE);
    }

    public static Path getPath() {
        return path;
    }
}
