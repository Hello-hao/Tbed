package cn.hellohao.utils;

public class MyStringUtils {

    /**
     * 版本号比较
     *
     * @param v1
     * @param v2
     * @return 0代表相等，1代表左边大，-1代表右边大
     * compareVersion("1.0.358_20180820090554","1.0.358_20180820090553")=1
     */
    public static int compareVersion(String v1, String v2) {
        if (v1.equals(v2)) {
            return 0;
        }
        String[] version1Array = v1.split("[._]");
        String[] version2Array = v2.split("[._]");
        int index = 0;
        int minLen = Math.min(version1Array.length, version2Array.length);
        long diff = 0;

        while (index < minLen
                && (diff = Long.parseLong(version1Array[index])
                - Long.parseLong(version2Array[index])) == 0) {
            index++;
        }
        if (diff == 0) {
            for (int i = index; i < version1Array.length; i++) {
                if (Long.parseLong(version1Array[i]) > 0) {
                    return 1;
                }
            }
            for (int i = index; i < version2Array.length; i++) {
                if (Long.parseLong(version2Array[i]) > 0) {
                    return -1;
                }
            }
            return 0;
        } else {
            return diff > 0 ? 1 : -1;
        }
    }

    /**
     * 移除 URL 中的第一个 '/'
     * @return 如 path = '/folder1/file1', 返回 'folder1/file1'
     */
    public static String removeFirstSeparator(String path) {
        if (!"".equals(path) && path.charAt(0) == '/') {
            path = path.substring(1);
        }
        return path;
    }
    /**
     * 移除 URL 中的最后一个 '/'
     * @return 如 path = '/folder1/file1/', 返回 '/folder1/file1'
     */
    public static String removeLastSeparator(String path) {
        if (!"".equals(path) && path.charAt(path.length() - 1) == '/') {
            path = path.substring(0, path.length() - 1);
        }
        return path;
    }

    /**
     * 将域名和路径组装成 URL, 主要用来处理分隔符 '/'
     * @param domain    域名
     * @param path      路径
     * @return          URL
     */
    public static String concatDomainAndPath(String domain, String path) {
        if (path != null && path.length() > 1 && path.charAt(0) != '/') {
            path = '/' + path;
        }
        if (domain.charAt(domain.length() - 1) == '/') {
            domain = domain.substring(0, domain.length() - 2);
        }
        return domain + path;
    }
}
