package net.shadowfacts.shadowtweaks.features.screenshot.services.imgur;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author shadowfacts
 */
@AllArgsConstructor
@ToString
@Getter
@Setter
public class ImgurResponse {

	private boolean success;
	private int status;
	private UploadedImage data;

	@AllArgsConstructor
	@ToString
	@Getter
	@Setter
	public static class UploadedImage {
		private String id;
		private String title;
		private String description;
		private String type;
		private boolean animated;
		private int width;
		private int height;
		private int size;
		private int views;
		private int bandwidth;
		private String vote;
		private boolean favorite;
		private String accountUrl;
		private String deleteHash;
		private String name;
		private String link;
	}

}
