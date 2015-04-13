package com.forum.web.atom;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.forum.web.parse.Article;
import com.forum.web.parse.Parser;

@Entity
@Table(name="entries")
public class AtomEntry implements Article {

	//required attributes
	private String title;
	
	@Column(name="global_entry_id")
	private String globalId;
	private long updated;

	// recommended attributes
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name="entry_id")
	// @LazyCollection(LazyCollectionOption.FALSE)
	private Set<Author> authors;
	
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name="entry_id")
	// @LazyCollection(LazyCollectionOption.FALSE)
	private Set<Contributor> contributors;

	private String link; 
	private String content; 
	private String summary;

	// optional attributes
	@ElementCollection
	@CollectionTable(name="categories", joinColumns = @JoinColumn(name="entry_id"))
	@Column(name="category")	
	private List<String> categories;
	
	@ManyToOne
	@JoinColumn(name="feed_id")
	private AtomFeed feed;
	
	private long published;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="source_id")
	private AtomFeed source;
	private String rights;
	
	@Id
	@Column(name="entry_id")
	private String id;
	private int hash;
	
	public AtomEntry() {}
	
	public AtomEntry(String title, String globalId, long updated) {
		this.title = title;
		this.globalId = globalId;
		this.updated = updated;
		this.id = generateId(globalId);
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getGlobalId() {
		return globalId;
	}

	public void setGlobalId(String globalId) {
		this.globalId = globalId;
	}

	public long getUpdated() {
		return updated;
	}

	public void setUpdated(long updated) {
		this.updated = updated;
	}

	public Set<Author> getAuthors() {
		return authors;
	}

	public void setAuthors(Set<Author> author) {
		this.authors = author;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public List<String> getCategories() {
		return categories;
	}

	public void setCategories(List<String> categories) {
		this.categories = categories;
	}

	public Set<Contributor> getContributors() {
		return contributors;
	}

	public void setContributors(Set<Contributor> contributors) {
		this.contributors = contributors;
	}

	public long getPublished() {
		return published;
	}

	public void setPublished(long published) {
		this.published = published;
	}

	public AtomFeed getSource() {
		return source;
	}

	public void setSource(AtomFeed source) {
		this.source = source;
	}

	public String getRights() {
		return rights;
	}

	public void setRights(String rights) {
		this.rights = rights;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	// unimplemented Article interface methods
	public List<String> people() {
		List<String> names = new ArrayList<String>();
		for (Author author: authors) {
			names.add(author.getName());
		}
		return names;
	}
	
	public String date() {
		return Parser.buildDate(updated);
	}
	
	public String description() {
		return summary;
	}
	
	public String link() {
		return this.link;
	}
	
	public String title() {
		return this.title;
	}
	
	public AtomFeed getFeed() {
		return feed;
	}

	public void setFeed(AtomFeed feed) {
		this.feed = feed;
	}

	public int getHash() {
		return hash;
	}

	public void setHash(int hash) {
		this.hash = hash;
	}

	private String generateId(String id) {
		UUID uuid = UUID.nameUUIDFromBytes(id.getBytes());
		return uuid.toString();
	}

	@Override
	public int hashCode() {
		if (this.hash != 0) {
			return this.hash;
		}
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AtomEntry other = (AtomEntry) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "AtomEntry [title=" + title + ", globalId=" + globalId
				+ ", updated=" + updated + ", author=" + authors + ", link="
				+ link + ", summary=" + summary
				+ ", category=" + categories + ", contributors=" + contributors
				+ ", published=" + published + ", source=" + source
				+ ", rights=" + rights + ", id=" + id + "]";
	}
	
}
