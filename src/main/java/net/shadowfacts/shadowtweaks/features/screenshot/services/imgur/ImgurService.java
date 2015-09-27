package net.shadowfacts.shadowtweaks.features.screenshot.services.imgur;

import net.minecraft.client.Minecraft;
import net.minecraft.event.ClickEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.shadowfacts.shadowtweaks.ShadowTweaks;
import net.shadowfacts.shadowtweaks.features.screenshot.ScreenShotHelper;
import net.shadowfacts.shadowtweaks.features.screenshot.services.Service;
import org.apache.commons.io.IOUtils;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedFile;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.File;
import java.lang.reflect.Field;
import java.util.Date;

/**
 * @author shadowfacts
 */
public class ImgurService implements Service {

	private String clientId;
	private boolean copyLink;

	@Override
	public String getName() {
		return "imgur";
	}

	@Override
	public void configure(Configuration config, String category) {
		clientId = config.getString("clientId", category, "", "The client id for the Imgur API.\nGo to https://api.imgur.com/oauth2/addclient to add an application then fill out these values.");

		copyLink = config.getBoolean("copyLink", category, true, "Copy the Imgur link on successful upload");

		config.getCategory(category).setComment("Configuration for the imgur auto-uploader for screenshots");
	}


	@Override
	public void accept(File screenshot) {
		((Runnable) () -> {
			RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(ImgurAPI.server).build();

			restAdapter.create(ImgurAPI.class).postImage(
					"Client-ID " + clientId,
					String.format("Minecraft %s screenshot from %s", MinecraftForge.MC_VERSION, ScreenShotHelper.dateFormat.format(new Date())),
					new TypedFile("image/png", screenshot),
					new Callback<ImgurResponse>() {
						@Override
						public void success(ImgurResponse imgurResponse, Response response) {
							if (response == null) {
								ShadowTweaks.log.error("There was a problem uploading a screenshot to Imgur");
								return;
							}

							((Runnable) () -> {
								ChatComponentText url = new ChatComponentText(imgurResponse.getData().getLink());
								url.getChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, imgurResponse.getData().getLink()));
								url.getChatStyle().setUnderlined(true);

								Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentTranslation("shadowtweaks.service.imgur.success", url));
							}).run();

							if (copyLink) {
								Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
								clipboard.setContents(new StringSelection(imgurResponse.getData().getLink()), null);
							}
						}

						@Override
						public void failure(RetrofitError error) {
							ShadowTweaks.log.error("There was a problem uploading a screenshot to Imgur:");

							try {
								ShadowTweaks.log.error(IOUtils.toString(error.getResponse().getBody().in()));
							} catch (Exception ignored) {}

							((Runnable) () -> Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentTranslation("shadowtweaks.service.imgur.failure"))).run();
						}
					}
			);

		}).run();
	}
}
