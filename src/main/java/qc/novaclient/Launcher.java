package qc.novaclient;

import fr.flowarg.flowupdater.FlowUpdater;
import fr.flowarg.flowupdater.utils.ModFileDeleter;
import fr.flowarg.flowupdater.utils.UpdaterOptions;
import fr.flowarg.flowupdater.versions.AbstractForgeVersion;
import fr.flowarg.flowupdater.versions.ForgeVersionBuilder;
import fr.flowarg.flowupdater.versions.VanillaVersion;
import fr.flowarg.openlauncherlib.NoFramework;
import fr.litarvan.openauth.microsoft.MicrosoftAuthResult;
import fr.litarvan.openauth.microsoft.MicrosoftAuthenticationException;
import fr.litarvan.openauth.microsoft.MicrosoftAuthenticator;
import fr.theshark34.openlauncherlib.minecraft.*;

import java.io.File;
import java.nio.file.Path;
import java.util.Arrays;
import javax.swing.*;

public class Launcher {
    private final static GameInfos gameInfos = new GameInfos("novaclient", new GameVersion("1.19.4", GameType.V1_13_HIGHER_FORGE), new GameTweak[]{GameTweak.FORGE});
    private final static Path path = gameInfos.getGameDir();
    public static File crashFile = new File(String.valueOf(path), "crashes");
    private static AuthInfos authInfos;
    public static String playerUsername;  // Step 1: Class variable to store the username

    public static void auth() throws MicrosoftAuthenticationException {
        MicrosoftAuthenticator microsoftAuthenticator = new MicrosoftAuthenticator();
        final String refresh_token = qc.novaclient.Frame.getSaver().get("refresh_token");
        MicrosoftAuthResult result;
        if (refresh_token != null && !refresh_token.isEmpty()) {
            result = microsoftAuthenticator.loginWithRefreshToken(refresh_token);
            playerUsername = qc.novaclient.Frame.getSaver().get("player_username"); // Step 3: Retrieve and store username
            authInfos = new AuthInfos(result.getProfile().getName(), result.getAccessToken(), result.getProfile().getId());
        } else {
            result = microsoftAuthenticator.loginWithWebview();
            qc.novaclient.Frame.getSaver().set("refresh_token", result.getRefreshToken());
            authInfos = new AuthInfos(result.getProfile().getName(), result.getAccessToken(), result.getProfile().getId());
            if (result.getProfile() != null) {
                playerUsername = result.getProfile().getName();
                qc.novaclient.Frame.getSaver().set("player_username", playerUsername);
                JOptionPane.showMessageDialog(
                        null,
                        "Bienvenue " + playerUsername + " sur Nova Antares!",
                        "Yay!",
                        JOptionPane.INFORMATION_MESSAGE
                );
            }
        }
    }


    public static void update() throws Exception {

        VanillaVersion vanillaVersion = new VanillaVersion.VanillaVersionBuilder().withName("1.19.4").build();
        UpdaterOptions options = new UpdaterOptions.UpdaterOptionsBuilder().build();

        AbstractForgeVersion version = new ForgeVersionBuilder(ForgeVersionBuilder.ForgeVersionType.NEW).withMods("https://novaalcyone.com/storage/lanceur/mods.php").withFileDeleter(new ModFileDeleter(true)).withForgeVersion("45.1.0").build();

        FlowUpdater updater = new FlowUpdater.FlowUpdaterBuilder().withVanillaVersion(vanillaVersion).withUpdaterOptions(options).withModLoaderVersion(version).build();
        updater.update(path);

    }

    public static void launch() throws Exception {
        MicrosoftAuthenticator microsoftAuthenticator = new MicrosoftAuthenticator();
        final String refresh_token = qc.novaclient.Frame.getSaver().get("refresh_token");
        MicrosoftAuthResult result = microsoftAuthenticator.loginWithRefreshToken(refresh_token);
        authInfos = new AuthInfos(result.getProfile().getName(), result.getAccessToken(), result.getProfile().getId());
        NoFramework noFramework = new NoFramework(path, authInfos, GameFolder.FLOW_UPDATER);
        noFramework.getAdditionalVmArgs().addAll(Arrays.asList(qc.novaclient.Frame.getInstance().getPanel().getRamSelector().getRamArguments()));
        noFramework.launch("1.19.4", "45.1.0", NoFramework.ModLoader.FORGE);
    }

    public static Path getPath() {
        return path;
    }

}