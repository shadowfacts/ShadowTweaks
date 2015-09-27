package net.shadowfacts.shadowtweaks.features.screenshot.services.imgur;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.Header;
import retrofit.http.POST;
import retrofit.http.Query;
import retrofit.mime.TypedFile;

/**
 * @author shadowfacts
 */
public interface ImgurAPI {

	String server = "https://api.imgur.com";

	@POST("/3/image")
	void postImage(
			@Header("Authorization") String auth,
			@Query("title") String title,
			@Body TypedFile file,
			Callback<ImgurResponse> cb
	);

}
