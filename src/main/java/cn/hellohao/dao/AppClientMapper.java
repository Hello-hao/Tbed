package cn.hellohao.dao;

import cn.hellohao.pojo.AppClient;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author Hellohao
 * @version 1.0
 * @date 2022-06-10 18:33
 */

@Mapper
public interface AppClientMapper {
    AppClient getAppClientData(@Param("id") String id);

    Integer editAppClientData(AppClient appClient);

}
