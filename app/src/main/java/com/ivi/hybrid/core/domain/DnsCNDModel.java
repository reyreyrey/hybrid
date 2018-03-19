package com.ivi.hybrid.core.domain;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * author: Rea.X
 * date: 2017/5/31.
 */

public class DnsCNDModel implements Serializable{
    private static final long serialVersionUID = -5029708220812475701L;


    @SerializedName("cdnHost")
    private String cdnHost;
    @SerializedName("dns")
    private List<DnsBean> dns;
    @SerializedName("domainOthers")
    private List<DomainOthersBean> domainOthers;

    public String getCdnHost() {
        return cdnHost;
    }

    public void setCdnHost(String cdnHost) {
        this.cdnHost = cdnHost;
    }

    public List<DnsBean> getDns() {
        return dns;
    }

    public void setDns(List<DnsBean> dns) {
        this.dns = dns;
    }

    public List<DomainOthersBean> getDomainOthers() {
        return domainOthers;
    }

    public void setDomainOthers(List<DomainOthersBean> domainOthers) {
        this.domainOthers = domainOthers;
    }

    public static class DnsBean implements Serializable{

        private static final long serialVersionUID = -566974955697815284L;
        @SerializedName("domain")
        private String domain;
        @SerializedName("ip_list")
        private List<String> ip_list;

        public String getDomain() {
            return domain;
        }

        public void setDomain(String domain) {
            this.domain = domain;
        }

        public List<String> getIp_list() {
            return ip_list;
        }

        public void setIp_list(List<String> ip_list) {
            this.ip_list = ip_list;
        }
    }

    public static class DomainOthersBean implements Serializable{

        private static final long serialVersionUID = -7886695994510241056L;
        @SerializedName("domain")
        private String domain;
        @SerializedName("ip_list")
        private List<String> ip_list;

        public String getDomain() {
            return domain;
        }

        public void setDomain(String domain) {
            this.domain = domain;
        }

        public List<String> getIp_list() {
            return ip_list;
        }

        public void setIp_list(List<String> ip_list) {
            this.ip_list = ip_list;
        }
    }
}
