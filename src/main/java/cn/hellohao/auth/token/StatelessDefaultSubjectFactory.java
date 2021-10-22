package cn.hellohao.auth.token;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.SubjectContext;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;

/**
 * @author Hellohao
 * @version 1.0
 * @date 2021/6/15 17:30
 */
public class StatelessDefaultSubjectFactory extends DefaultWebSecurityManager {
    //不创建Session
    public Subject createSubject(SubjectContext context){
        context.setSessionCreationEnabled(false);
        return super.createSubject(context);
    }
}
