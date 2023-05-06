package com.example.security.config;

import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.mgt.DefaultFilterChainManager;
import org.apache.shiro.web.filter.mgt.PathMatchingFilterChainResolver;
import org.apache.shiro.web.servlet.AbstractShiroFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.validation.ValidationException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Shiro动态配置
 */
@Component
@ConfigurationProperties("shiro-config")
public class ShiroDynamicConfig {
    private static Map<String, String> shiroFilterDefinitionMap = new LinkedHashMap<String, String>(){{put("/**", "oauth2");}};

    @Autowired
    private ShiroFilterFactoryBean shiroFilterFactoryBean;

    public static Map<String, String> getShiroFilterDefinitionMap() {
        return shiroFilterDefinitionMap;
    }

    /**
     * 设置默认值
     */
    private void defaultFilterDefinitionMap() {
        if(shiroFilterDefinitionMap.get("/**") == null)
            shiroFilterDefinitionMap.put("/**", "oauth2");
    }

    /**
     * 处理FilterChainDefinition动态权限
     * @ConfigurationProperties
     * @param filterChainDefinition
     */
    public void setFilterChainDefinition(List<String> filterChainDefinition) {
        shiroFilterDefinitionMap.clear();
        filterChainDefinition.stream().forEach(chainString -> setDefinition(shiroFilterDefinitionMap, chainString));
        //增加默认值
        defaultFilterDefinitionMap();
        updatePermission();
    }

    private void setDefinition(Map<String, String> shiroFilterDefinitionMap, String text) {
        int eqIdx = text.indexOf(61);
        if (eqIdx <= 0) {
            throw new ValidationException("Unable to parse PredicateDefinition text '" + text + "', must be of the form uri=control");
        } else {
            shiroFilterDefinitionMap.put(text.substring(0, eqIdx), text.substring(eqIdx + 1));
        }
    }

    /**
     * 功能:动态更新shiro权限(无需重启)
     *
     * @return
     */
    public boolean updatePermission() {
        boolean flag = false;
        synchronized (shiroFilterFactoryBean) {
            AbstractShiroFilter shiroFilter = null;
            try {
                shiroFilter = (AbstractShiroFilter) shiroFilterFactoryBean.getObject();
                PathMatchingFilterChainResolver filterChainResolver = (PathMatchingFilterChainResolver) shiroFilter.getFilterChainResolver();
                DefaultFilterChainManager manager = (DefaultFilterChainManager) filterChainResolver.getFilterChainManager();
                // 1. 清空老的权限控制
                manager.getFilterChains().clear();
                shiroFilterFactoryBean.setFilterChainDefinitionMap(shiroFilterDefinitionMap);
                // ========== 2. 动态加载权限核心部分结束 ==========
                // 3. 重新构建生成
                Map<String, String> chains = shiroFilterFactoryBean.getFilterChainDefinitionMap();
                shiroFilterFactoryBean.getFilterChainDefinitionMap().entrySet().stream().forEach(entry -> {
                    manager.createChain(entry.getKey(), entry.getValue().trim().replace(" ", ""));
                });
                flag = true;
                System.out.println("更新权限成功");
            } catch (Exception e) {
                throw new RuntimeException("更新shiro权限出现错误!");
            }
        }
        return flag;
    }

}
