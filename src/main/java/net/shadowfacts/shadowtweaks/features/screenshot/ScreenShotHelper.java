package net.shadowfacts.shadowtweaks.features.screenshot;

import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.event.ClickEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;
import net.shadowfacts.shadowtweaks.STConfig;
import net.shadowfacts.shadowtweaks.ShadowTweaks;
import net.shadowfacts.shadowtweaks.features.screenshot.services.Service;
import net.shadowfacts.shadowtweaks.features.screenshot.services.ServiceManager;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.IntBuffer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author shadowfacts
 */
public class ScreenShotHelper {

	public static final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss");

	private static IntBuffer buffer; // the pixel buffer
	private static int[] values; // the raw pixel values

	/**
	 * Called from {@link net.minecraft.util.ScreenShotHelper}.saveScreenshot
	 * Only called on the client side
	 * A large amount of this code is copied from the Vanilla implementation of {@link net.minecraft.util.ScreenShotHelper}
	 */
	public static IChatComponent saveScreenshot(File gameDir, String fileName, int requestedWidth, int requestedHeight, Framebuffer framebuffer) {
		File screenshotsDir = STConfig.screenshotDir.isEmpty() ? new File(gameDir, "screenshots") : new File(STConfig.screenshotDir);
		if (!screenshotsDir.exists()) screenshotsDir.mkdirs();

		if (OpenGlHelper.isFramebufferEnabled()) {
			requestedWidth = framebuffer.framebufferTextureWidth;
			requestedHeight = framebuffer.framebufferTextureHeight;
		}

		int total = requestedWidth * requestedHeight;

		if (buffer == null || buffer.capacity() < total) {
			buffer = BufferUtils.createIntBuffer(total);
			values = new int[total];
		}

		GL11.glPixelStorei(GL11.GL_PACK_ALIGNMENT, 1);
		GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);

		buffer.clear();

		if (OpenGlHelper.isFramebufferEnabled()) {
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, framebuffer.framebufferTexture);
			GL11.glGetTexImage(GL11.GL_TEXTURE_2D, 0, GL12.GL_BGRA, GL12.GL_UNSIGNED_INT_8_8_8_8_REV, buffer);
		} else {
			GL11.glReadPixels(0, 0, requestedWidth, requestedHeight, GL12.GL_BGRA, GL12.GL_UNSIGNED_INT_8_8_8_8_REV, buffer);
		}

		buffer.get(values);
		TextureUtil.func_147953_a(values, requestedWidth, requestedHeight);
		BufferedImage bufferedImage = null;

		if (OpenGlHelper.isFramebufferEnabled()) {
			bufferedImage = new BufferedImage(framebuffer.framebufferWidth, framebuffer.framebufferHeight, 1);

			int l = framebuffer.framebufferTextureHeight - framebuffer.framebufferHeight;

			for (int i = l; i < framebuffer.framebufferTextureHeight; ++i)
			{
				for (int x = 0; x < framebuffer.framebufferWidth; ++x)
				{
					bufferedImage.setRGB(x, i - l, values[i * framebuffer.framebufferTextureWidth + x]);
				}
			}
		} else {
			bufferedImage = new BufferedImage(requestedWidth, requestedHeight, 1);
			bufferedImage.setRGB(0, 0, requestedWidth, requestedHeight, values, 0, requestedWidth);
		}

		File screenshot = fileName != null ? new File(screenshotsDir, fileName) : getTimestampedPNGFileForDirectory(screenshotsDir);

		try {
			ImageIO.write(bufferedImage, "png", screenshot);

			applyService(screenshot);

			ChatComponentText file = new ChatComponentText(screenshot.getName());
			file.getChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.OPEN_FILE, screenshot.getAbsolutePath()));
			file.getChatStyle().setUnderlined(true);
			return new ChatComponentTranslation("screenshot.success", file);

		} catch (IOException e) {
			ShadowTweaks.log.error("There was a problem saving the screenshot");
			e.printStackTrace();

			return new ChatComponentTranslation("screenshot.failure", e.getMessage());

		}
	}

	private static void applyService(File screenshot) {
		Service s = ServiceManager.getActiveService();
		if (s != null) s.accept(screenshot);
	}

	private static File getTimestampedPNGFileForDirectory(File dir) {
		String s = dateFormat.format(new Date());
		int i = 1;

		while (true) {
			File f = new File(dir, s + (i == 1 ? "" : "_" + i) + ".png");

			if (!f.exists()) {
				return f;
			}

			++i;
		}

	}

}
