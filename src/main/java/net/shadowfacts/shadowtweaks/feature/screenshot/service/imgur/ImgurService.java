package net.shadowfacts.shadowtweaks.feature.screenshot.service.imgur;

import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.shadowfacts.shadowlib.util.DesktopUtils;
import net.shadowfacts.shadowtweaks.ShadowTweaks;
import net.shadowfacts.shadowtweaks.feature.screenshot.service.Service;
import org.apache.commons.io.IOUtils;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedFile;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author shadowfacts
 */
public class ImgurService implements Service {

	private static final DateFormat format = new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss");

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

		config.getCategory(category).setComment("Configuration for the Imgur screenshot auto-uploader");
	}

	@Override
	public void accept(File screenshot) {
		RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(ImgurAPI.server).build();

		restAdapter.create(ImgurAPI.class).uploadImage(
				"Client-ID " + clientId,
				String.format("Minecraft %s screenshot from %s", MinecraftForge.MC_VERSION, format.format(new Date())),
				new TypedFile("image/png", screenshot),
				new Callback<ImgurResponse>() {
					@Override
					public void success(ImgurResponse imgurResponse, Response response) {
						if (response == null) {
							ShadowTweaks.log.error("There was a problem uploading a screenshot to Imgur");
							return;
						}

						TextComponentString url = new TextComponentString(imgurResponse.data.link);
						url.getStyle().setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, imgurResponse.data.link));
						url.getStyle().setUnderlined(true);

						Minecraft.getMinecraft().thePlayer.addChatComponentMessage(new TextComponentTranslation("shadowtweaks.service.imgur.success", url));

						if (copyLink) {
							DesktopUtils.copyToClipboard(imgurResponse.data.link);
						}
					}

					@Override
					public void failure(RetrofitError error) {
						ShadowTweaks.log.error("There was a problem uploading a screenshot to Imgur:");
						try {
							ShadowTweaks.log.error(IOUtils.toString(error.getResponse().getBody().in()));
						} catch (Exception ignored) {}

						Minecraft.getMinecraft().thePlayer.addChatComponentMessage(new TextComponentTranslation("shadowtweaks.service.imgur.failure"));
					}
				}
		);
	}

}
