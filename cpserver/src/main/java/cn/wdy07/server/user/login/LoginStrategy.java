package cn.wdy07.server.user.login;

import cn.wdy07.server.user.Client;
import cn.wdy07.server.user.User;

public interface LoginStrategy {
	boolean canLogin(User user, Client client);
}
