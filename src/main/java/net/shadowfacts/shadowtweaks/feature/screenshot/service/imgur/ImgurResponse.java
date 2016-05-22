package net.shadowfacts.shadowtweaks.feature.screenshot.service.imgur;

/**
 * @author shadowfacts
 */
public class ImgurResponse {

	public boolean success;
	public int status;
	public UploadedImage data;

	public static class UploadedImage {
		public String id;
		public String title;
		public String description;
		public String type;
		public boolean animated;
		public int width;
		public int height;
		public int size;
		public int views;
		public int bandwidth;
		public String vote;
		public boolean favorite;
		public String accountUrl;
		public String deleteHash;
		public String name;
		public String link;
	}

}
