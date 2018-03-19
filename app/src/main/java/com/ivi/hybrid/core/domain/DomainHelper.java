package com.ivi.hybrid.core.domain;

import android.net.Uri;
import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;
import com.ivi.hybrid.core.call.CallModule;
import com.ivi.hybrid.core.net.client.ClientManager;
import com.ivi.hybrid.utils.LogUtil;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Request;
import okhttp3.Response;

import static com.ivi.hybrid.core.cons.Cons.PrefrencesKeys.CDN;
import static com.ivi.hybrid.core.cons.Cons.PrefrencesKeys.DOMAIN;


/**
 * author: Rea.X
 * date: 2017/7/7.
 */

public class DomainHelper {
    private static DomainIP canUsedDomain;
    private static final String IP_REGEX = "^([0-9]{1,3}\\.){3}[0-9]{1,3}(:[0-9]+)?$";


    private static Map<String, String> hostChangeIPMap;

    static {
        hostChangeIPMap = new HashMap<>();
    }

    /**
     * 检测传入的url有没有host头，如果没有就添加
     *
     * @param url
     * @return
     */
    public static String checkAndAddHost(String url) {
        String domain = getCanuserHost();
        if (domain.substring(domain.length() - 1).equals("/")) {
            if (url.substring(0, 1).equals("/")) {
                url = url.substring(1, url.length());
            }
        } else if (!url.substring(0, 1).equals("/")) {
            url = "/" + url;
        }

        if (!url.startsWith("http://") && !url.contains("https://")) {
            url = domain + url;
        }

        return url;
    }

    public static String getChangeIP(String host) {
        if (hostChangeIPMap.containsKey(host))
            return hostChangeIPMap.get(host);
        return null;
    }


    public static String getHostHeader() {
        String host = canUsedDomain.domain;
        if (!host.startsWith("http://") && !host.startsWith("https://"))
            host = "http://" + host;
        return Uri.parse(host).getHost();
    }

    /**
     * 是否是本站的域名
     *
     * @param url
     * @return
     */
    public static boolean isWebHost(String url) {
        if (TextUtils.isEmpty(url)) return false;
        Uri uri = Uri.parse(url);
        if (uri == null) return false;
        String scheme = uri.getScheme();
        if (TextUtils.isEmpty(scheme)) return false;
        if (scheme.equalsIgnoreCase("file")) return false;
        if (canUsedDomain == null || canUsedDomain.domain == null) return false;
        String host = Uri.parse(canUsedDomain.domain).getHost();
        String ip = canUsedDomain.ip;
        String needCheckHost = Uri.parse(url).getHost();
        if (needCheckHost == null) return false;
        if (!TextUtils.isEmpty(host) && needCheckHost.equals(host)) return true;
        if (!TextUtils.isEmpty(ip) && needCheckHost.equals(ip)) return true;
        return false;
    }

    /**
     * 是否是本地加载
     *
     * @param url
     * @return
     */
    public static boolean isNativalDomain(String url) {
        Uri uri = Uri.parse(url);
        if (uri == null) return false;
        String scheme = uri.getScheme();
        if (TextUtils.isEmpty(scheme)) return false;
        if (scheme.equalsIgnoreCase("file")) return true;
        return false;
    }

    public static boolean isCDNHost(String url) {
        if (TextUtils.isEmpty(url)) return false;
        Uri uri = Uri.parse(url);
        if (uri == null) return false;
        String scheme = uri.getScheme();
        if (TextUtils.isEmpty(scheme)) return false;
        if (scheme.equalsIgnoreCase("file")) return false;
        String cdnHost = CallModule.invokeCacheModuleGet(CDN);
        if (TextUtils.isEmpty(cdnHost)) return false;
        String host = Uri.parse(cdnHost).getHost();
        String needCheckHost = Uri.parse(url).getHost();
        if (needCheckHost == null) return false;
        if (!TextUtils.isEmpty(host) && needCheckHost.equals(host)) return true;
        return false;
    }


    /**
     * 获取可以使用的domain
     *
     * @return
     */
    public static DomainIP getCanUsedDomain() {
        return canUsedDomain;
    }

    public static String getCanuserHost() {
        if (canUsedDomain == null) return null;
        String domain = canUsedDomain.domain;
        String ip = canUsedDomain.ip;
        if (!TextUtils.isEmpty(ip)) return ip;
        return domain;
    }


    /**
     * 开始检测
     *
     * @param model
     */
    public static void start(DnsCNDModel model) {
        try {
            initCheckHash(model);
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<DnsCNDModel.DnsBean> dnsModels = model.getDns();
        //首先进行排序
        List<DomainIP> list = sort(dnsModels);
        LogUtil.d(":>>>>>>>>>>>>>>>>>>>" + list.toString());
        //然后循环检测ip和域名是否可用
        int position;
        while (list.size() != 0) {
            position = (int) (Math.random() * list.size());
            DomainIP domainIP = list.remove(position);
            String hostHead = getHostHead(domainIP);
            String needCheckUrl = domainIP.ip;
            if (TextUtils.isEmpty(needCheckUrl))
                needCheckUrl = domainIP.domain;
            needCheckUrl = dealDomain(needCheckUrl, hostHead);
            if (TextUtils.isEmpty(needCheckUrl)) continue;
            try {
                if (checkUrl(needCheckUrl, domainIP, hostHead)) {
                    break;
                }
            } catch (IOException e) {
                LogUtil.d(needCheckUrl + "->>>>>>>>不能够使用 " + e.getLocalizedMessage());
            }
        }
    }

    private static void initCheckHash(DnsCNDModel model) throws MalformedURLException {
        List<DnsCNDModel.DomainOthersBean> list = model.getDomainOthers();
        if (list != null && list.size() != 0) {
            for (DnsCNDModel.DomainOthersBean bean : list) {
                String domain = bean.getDomain();
                List<String> ips = bean.getIp_list();
                if (ips == null || ips.size() == 0) continue;
                String ip = bean.getIp_list().get(0);
                if (TextUtils.isEmpty(ip)) continue;
                Uri uri = Uri.parse(domain);
                if (uri == null) continue;
                String host = uri.getHost();
                if (TextUtils.isEmpty(host)) continue;
                hostChangeIPMap.put(host, ip);
            }
        }
    }

    /**
     * 获取域名的host头
     *
     * @param domainIP
     * @return
     */
    private static String getHostHead(DomainIP domainIP) {
        String domain = domainIP.domain;
        if (domain.startsWith("https://")) return "https://";
        if (domain.startsWith("http://")) return "http://";
        return "https://";
    }


    /**
     * 检测url是否可用
     *
     * @param needCheckUrl
     * @return
     * @throws IOException
     */
    private static boolean checkUrl(String needCheckUrl, DomainIP model, String hostHead) throws IOException {
        String url = needCheckUrl + "version.txt";
        LogUtil.d("start check------->" + url);
        String domain = model.domain;

        domain = dealDomain(domain, hostHead);

        String host = Uri.parse(domain).getHost();
        Request request = new Request.Builder().url(url).header("Host", host).get().build();
        Response response = ClientManager.getCheckDomainClient().newCall(request).execute();
        int code = response.code();
        response.close();
        if (code == 200) {
            LogUtil.d(url + "->>>>>>>>可以使用");
            LogUtil.d("domain=================" + DomainHelper.getCanuserHost());
            canUsedDomain = model;
            if (!TextUtils.isEmpty(canUsedDomain.ip)) {
                Uri url1 = Uri.parse(canUsedDomain.domain);
                String host1 = url1.getHost();
                hostChangeIPMap.put(host1, dealDomain(canUsedDomain.ip, hostHead));
            }
            CallModule.invokeCacheModuleSave(DOMAIN, domain);
            return true;
        } else {
            LogUtil.d(url + "->>>>>>>>不能够使用");
        }
        return false;
    }

    public static boolean isIP(String s) {
        Pattern pattern = Pattern.compile(IP_REGEX);
        Matcher matcher = pattern.matcher(s);
        return matcher.matches();
    }


    /**
     * 排序，ip在前，host在后
     *
     * @param dnsModels
     * @return
     */
    private static List<DomainIP> sort(List<DnsCNDModel.DnsBean> dnsModels) {
        List<DomainIP> list = new ArrayList<>();

        List<DomainIP> tempIps = new ArrayList<>();
        List<DomainIP> tempDomains = new ArrayList<>();
        for (DnsCNDModel.DnsBean model : dnsModels) {
            if (model.getIp_list() != null && model.getIp_list().size() != 0) {
                int position = 0;
                for (String ip : model.getIp_list()) {
                    DomainIP domainIP = new DomainIP();
                    domainIP.domain = model.getDomain();
                    domainIP.ip = ip;
                    tempIps.add(domainIP);


                    if (position == 0) {
                        domainIP = new DomainIP();
                        domainIP.domain = model.getDomain();
                        tempDomains.add(domainIP);
                    }
                    position++;
                }
            } else {
                DomainIP domainIP = new DomainIP();
                domainIP.domain = model.getDomain();
                tempDomains.add(domainIP);
            }
        }

        list.addAll(tempIps);
        list.addAll(tempDomains);
        return list;
    }

    /**
     * 检测host头，如果没有host头的话，就默认加上https
     *
     * @param str
     * @return
     */
    private static String dealDomain(String str, String hostHead) {
        if (TextUtils.isEmpty(str) || str.equals("255.255.255.255") || str.equals("0.0.0.0"))
            return null;
//            Pattern pattern = Pattern.compile(IP_REGEX);
//            Matcher matcher = pattern.matcher(str);
        if (!str.startsWith("http://") && !str.startsWith("https://"))
            str = hostHead + str;
        if (!str.substring(str.length() - 1).equals("/")) {
            str = str + "/";
        }
        return str;
    }

    public static class DomainIP {
        @SerializedName("domain")
        public String domain;
        @SerializedName("ip")
        public String ip;

        @Override
        public String toString() {
            return "DomainIP{" +
                    "domain='" + domain + '\'' +
                    ", ip='" + ip + '\'' +
                    '}';
        }
    }
}
