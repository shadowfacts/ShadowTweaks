package net.shadowfacts.shadowtweaks.features.screenshot.services.puush;

import retrofit.http.POST;
import retrofit.http.Query;

/**
 * @author shadowfacts
 */
public interface PuushAPI {

	public static String server = "http://puush.me";

	@POST("/api/up")
	void uploadImage(
			@Query("k") String key,
			@Query("z") String clientId
	);

}
