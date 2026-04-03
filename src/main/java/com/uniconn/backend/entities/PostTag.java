package com.uniconn.backend.entities;

import com.uniconn.backend.composite_keys.PostTagId;
import jakarta.persistence.*;

@Table(name = "post_tag")
@Entity
public class PostTag {
	@EmbeddedId
	private PostTagId id;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @MapsId("postId")
    @JoinColumn(name = "post_id")
    private Post post;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @MapsId("tagId")
    @JoinColumn(name = "tag_id")
    private Tag tag;
	
	public PostTag() {}
	
	public PostTag(Post post, Tag tag) {
		this.id = new PostTagId(post.getPostId(), tag.getTagId());
		this.post = post;
		this.tag = tag;		
	}
	
	//getters&setters
	public PostTagId getId() {
		return id;
	}

	public void setId(PostTagId id) {
		this.id = id;
	}

	public Post getPost() {
		return post;
	}

	public void setPost(Post post) {
		this.post = post;
	}

	public Tag getTag() {
		return tag;
	}

	public void setTag(Tag tag) {
		this.tag = tag;
	}
	
}
