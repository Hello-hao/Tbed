package cn.hellohao.auth.token;

import org.apache.shiro.authc.AuthenticationToken;

public final class JwtAuthenticationToken implements AuthenticationToken {

    private static final long serialVersionUID = 1L;

    private final String token;

    public JwtAuthenticationToken(String token) {
        this.token = token;
    }

    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }
}
