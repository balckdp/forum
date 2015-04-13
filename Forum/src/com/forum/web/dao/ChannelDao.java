package com.forum.web.dao;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.forum.web.rss.RssChannel;

@Transactional
@Component("channelDao")
public class ChannelDao {

	@Autowired
	private SessionFactory sessionFactory;

	public ChannelDao() {
		System.out.println("successfully loaded channelDao");
		
	}
	
	public Session session() {
		return sessionFactory.getCurrentSession();
	}
	
	@SuppressWarnings("unchecked")
	public Set<RssChannel> getAllChannels() {
		Query query = session().createQuery("from RssChannel");
		Set<RssChannel> result = new HashSet<RssChannel>(query.list());
		return result;
	}
	
	public void createChannel(RssChannel channel) {
		session().save(channel);
	}
	
	public RssChannel getChannelById(String id) {
		return (RssChannel) session().get(RssChannel.class, id);
	}
			
	// queries based on title and link;
	public boolean exists(RssChannel channel) {
		String id = channel.getId();
		RssChannel ret = (RssChannel) session().get(RssChannel.class, id);
		return ret != null ? true : false;
	}
	
	// Returns all the channels whose title strings at lease partially match (case insensitive) the argument string
	@SuppressWarnings("unchecked")
	public Set<RssChannel> getChannelsByTitle(String title) {
		Criteria crit = session().createCriteria(RssChannel.class);
		crit.add(Restrictions.ilike("title", title, MatchMode.ANYWHERE));
		Set<RssChannel> result =  new HashSet<RssChannel>(crit.list());
		return result;

	}
	
	@SuppressWarnings("unchecked")
	public Set<RssChannel> getChannelsByCategory(String category) {
		Criteria crit = session().createCriteria(RssChannel.class);
		crit.add(Restrictions.ilike("category", category));
		Set<RssChannel> result =  new HashSet<RssChannel>(crit.list());
		return result;
		
	}

	@SuppressWarnings("unchecked")
	public Set<RssChannel> getFeedsByPersonName(String name) {
		Criteria crit = session().createCriteria(RssChannel.class);
		crit.add(Restrictions.or(
				Restrictions.ilike("managingEditor", name, MatchMode.ANYWHERE), 
				Restrictions.ilike("webMaster", name, MatchMode.ANYWHERE) 
				));
		Set<RssChannel> result =  new LinkedHashSet<RssChannel>(crit.list());
		
		return result;
	}

	
}
