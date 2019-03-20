package com.cy.recruit.common.utils;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.regex.Pattern;

public class CommonUtils {

    /**
     * 查询客户端ip
     * @param request
     * @return
     */
    public static String getIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if(StringUtils.isNotEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)){
            //多次反向代理后会有多个ip值，第一个ip才是真实ip
            int index = ip.indexOf(",");
            if(index != -1){
                return ip.substring(0,index);
            }else{
                return ip;
            }
        }
        ip = request.getHeader("X-Real-IP");
        if(StringUtils.isNotEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)){
            return ip;
        }
        return request.getRemoteAddr();
    }

    public static boolean isUrl(String url){
        String regex = "^([hH][tT]{2}[pP]:/*|[hH][tT]{2}[pP][sS]:/*|[fF][tT][pP]:/*|/*)(([A-Za-z0-9-~]+).)+([A-Za-z0-9-~\\/])+(\\?{0,1}(([A-Za-z0-9-~]+\\={0,1})([A-Za-z0-9-~]*)\\&{0,1})*)$";
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(url).matches();
    }

    public static String getContextPath(String url){
        if(StringUtils.isNotBlank(url)){
            if(url.indexOf("?") > 0){
                url = url.substring(0, url.indexOf("?"));
            }
            if(url.startsWith("http://") || url.startsWith("https://") || url.startsWith("//")){
                url = url.substring(url.indexOf("//") + 2, url.length());
            }
            return url.substring(url.indexOf("/"), url.length());
        }
        return null;
    }

    public static String getContextPathByRefer(HttpServletRequest request){
        String source = request.getHeader("Referer");
        if(StringUtils.isNotBlank(source)){
            source = CommonUtils.getContextPath(source);
        }
        return source;
    }
}
