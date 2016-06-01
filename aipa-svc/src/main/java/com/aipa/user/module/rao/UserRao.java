package com.aipa.user.module.rao;

import com.aipa.user.module.entity.User;
import com.qy.data.common.rao.GenericInfoRao;

public interface UserRao extends GenericInfoRao<User, Long>{
	
	
	public void addWithUniqueKey(User user);

	
}
