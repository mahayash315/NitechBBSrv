package models.request.api.bb;

import java.util.List;

public class UpdatePostsRequest {
	public List<PostEntry> posts;
	
	public static class PostEntry {
		public String idDate;
		public int idIndex;
		public List<WordEntry> words;
		
		public static class WordEntry {
			public Long id;
			public int value;
		}
	}
}
