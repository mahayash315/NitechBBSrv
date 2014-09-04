package models.request.api.bb;

import java.util.List;

public class UpdatePostsRequest {
	public List<PostEntry> posts;
	
	public static class PostEntry {
		public String idDate;
		public int idIndex;
		public String author;
		public String title;
	}
}
