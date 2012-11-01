package org.manalith.ircbot.plugin.alias;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AliasManager {
	@Autowired
	private AliasDao aliasDao;

	public AliasDao getAliasDao() {
		return aliasDao;
	}

	public void setAliasDao(AliasDao aliasDao) {
		this.aliasDao = aliasDao;
	}

	public Alias getAlias(String alias) {
		return aliasDao.findByWord(alias);
	}

	public void add(Alias alias) {
		aliasDao.save(alias);
	}

	public void remove(String alias) throws NotRegisteredException {
		Alias a = aliasDao.findByWord(alias);

		if (a == null)
			throw new NotRegisteredException();

		aliasDao.delete(a);
	}
}
