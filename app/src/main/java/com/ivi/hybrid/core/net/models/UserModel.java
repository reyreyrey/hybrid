package com.ivi.hybrid.core.net.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * author: Rea.X
 * date: 2017/5/23.
 */

public class UserModel implements Serializable {

    private static final long serialVersionUID = -2382348385084758521L;
    @SerializedName("before_login_date")
    private String before_login_date;
    @SerializedName("before_login_ip")
    private String before_login_ip;
    @SerializedName("created_by")
    private String created_by;
    @SerializedName("created_date")
    private String created_date;
    @SerializedName("credit")
    private String credit;
    @SerializedName("currency")
    private String currency;
    @SerializedName("customer_id")
    private String customer_id;
    @SerializedName("customer_level")
    private String customer_level;
    @SerializedName("customer_type")
    private String customer_type;
    @SerializedName("deposit_level")
    private String deposit_level;
    @SerializedName("effectivity_date")
    private String effectivity_date;
    @SerializedName("expiry_date")
    private String expiry_date;
    @SerializedName("flag")
    private String flag;
    @SerializedName("ip_address")
    private String ip_address;
    @SerializedName("last_login_date")
    private String last_login_date;
    @SerializedName("last_login_ip")
    private String last_login_ip;
    @SerializedName("login_name")
    private String login_name;
    @SerializedName("login_times")
    private String login_times;
    @SerializedName("parent_id")
    private String parent_id;
    @SerializedName("parent_login_name")
    private String parent_login_name;
    @SerializedName("phone")
    private String phone;
    @SerializedName("product_id")
    private String product_id;
    @SerializedName("priority_level")
    private String priority_level;
    @SerializedName("pwd")
    private String pwd;
    @SerializedName("pwd_expiry_date")
    private String pwd_expiry_date;
    @SerializedName("sex")
    private String sex;
    @SerializedName("game_key")
    private String game_key;
    @SerializedName("last_update")
    private String last_update;
    @SerializedName("last_updated_by")
    private String last_updated_by;
    @SerializedName("integral")
    private String integral;
    @SerializedName("reserve1")
    private String reserve1;
    @SerializedName("domain_name")
    private String domain_name;
    @SerializedName("integral_sports")
    private String integral_sports;
    @SerializedName("integral_bet_amount")
    private String integral_bet_amount;
    @SerializedName("integral_bet_times")
    private String integral_bet_times;
    @SerializedName("integral_recommend")
    private String integral_recommend;
    @SerializedName("integral_deposit")
    private String integral_deposit;
    @SerializedName("integral_ol_deposit")
    private String integral_ol_deposit;
    @SerializedName("integral_sd_ep")
    private String integral_sd_ep;
    @SerializedName("integral_used")
    private String integral_used;
    @SerializedName("is_special")
    private String is_special;
    @SerializedName("phone_flag")
    private String phone_flag;
    @SerializedName("integral_flag")
    private String integral_flag;
    @SerializedName("promotion_flag")
    private String promotion_flag;
    @SerializedName("xm_flag")
    private String xm_flag;
    @SerializedName("sms_times")
    private int sms_times;
    @SerializedName("effective_flag")
    private String effective_flag;
    @SerializedName("pwd_errors")
    private String pwd_errors;
    @SerializedName("hg_flag")
    private String hg_flag;
    @SerializedName("try_worth_flag")
    private String try_worth_flag;
    @SerializedName("game_credit")
    private String game_credit;
    @SerializedName("currency_rate")
    private String currency_rate;
    @SerializedName("match_sign_flag")
    private String match_sign_flag;
    @SerializedName("last_game")
    private String last_game;
    @SerializedName("call_flag")
    private String call_flag;
    @SerializedName("pt_key")
    private String pt_key;
    @SerializedName("ap_effective_date")
    private String ap_effective_date;
    @SerializedName("ap_key_status")
    private int ap_key_status;
    @SerializedName("ap_key_reset_count")
    private int ap_key_reset_count;
    @SerializedName("ap_key_errors_count")
    private int ap_key_errors_count;
    @SerializedName("ap_bank_verify_start")
    private int ap_bank_verify_start;
    @SerializedName("ap_bank_verify_end")
    private int ap_bank_verify_end;
    @SerializedName("ap_transfer_status")
    private String ap_transfer_status;
    @SerializedName("is_tm_sales")
    private String is_tm_sales;
    @SerializedName("show_balance")
    private String show_balance;
    @SerializedName("user_token")
    private String user_token;
    @SerializedName("real_name")
    private String real_name;

    public String getBefore_login_date() {
        return before_login_date;
    }

    public void setBefore_login_date(String before_login_date) {
        this.before_login_date = before_login_date;
    }

    public String getBefore_login_ip() {
        return before_login_ip;
    }

    public void setBefore_login_ip(String before_login_ip) {
        this.before_login_ip = before_login_ip;
    }

    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }

    public String getCredit() {
        return credit;
    }

    public void setCredit(String credit) {
        this.credit = credit;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public String getCustomer_level() {
        return customer_level;
    }

    public void setCustomer_level(String customer_level) {
        this.customer_level = customer_level;
    }

    public String getCustomer_type() {
        return customer_type;
    }

    public void setCustomer_type(String customer_type) {
        this.customer_type = customer_type;
    }

    public String getDeposit_level() {
        return deposit_level;
    }

    public void setDeposit_level(String deposit_level) {
        this.deposit_level = deposit_level;
    }

    public String getEffectivity_date() {
        return effectivity_date;
    }

    public void setEffectivity_date(String effectivity_date) {
        this.effectivity_date = effectivity_date;
    }

    public String getExpiry_date() {
        return expiry_date;
    }

    public void setExpiry_date(String expiry_date) {
        this.expiry_date = expiry_date;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getIp_address() {
        return ip_address;
    }

    public void setIp_address(String ip_address) {
        this.ip_address = ip_address;
    }

    public String getLast_login_date() {
        return last_login_date;
    }

    public void setLast_login_date(String last_login_date) {
        this.last_login_date = last_login_date;
    }

    public String getLast_login_ip() {
        return last_login_ip;
    }

    public void setLast_login_ip(String last_login_ip) {
        this.last_login_ip = last_login_ip;
    }

    public String getLogin_name() {
        return login_name;
    }

    public void setLogin_name(String login_name) {
        this.login_name = login_name;
    }

    public String getLogin_times() {
        return login_times;
    }

    public void setLogin_times(String login_times) {
        this.login_times = login_times;
    }

    public String getParent_id() {
        return parent_id;
    }

    public void setParent_id(String parent_id) {
        this.parent_id = parent_id;
    }

    public String getParent_login_name() {
        return parent_login_name;
    }

    public void setParent_login_name(String parent_login_name) {
        this.parent_login_name = parent_login_name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getPriority_level() {
        return priority_level;
    }

    public void setPriority_level(String priority_level) {
        this.priority_level = priority_level;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getPwd_expiry_date() {
        return pwd_expiry_date;
    }

    public void setPwd_expiry_date(String pwd_expiry_date) {
        this.pwd_expiry_date = pwd_expiry_date;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getGame_key() {
        return game_key;
    }

    public void setGame_key(String game_key) {
        this.game_key = game_key;
    }

    public String getLast_update() {
        return last_update;
    }

    public void setLast_update(String last_update) {
        this.last_update = last_update;
    }

    public String getLast_updated_by() {
        return last_updated_by;
    }

    public void setLast_updated_by(String last_updated_by) {
        this.last_updated_by = last_updated_by;
    }

    public String getIntegral() {
        return integral;
    }

    public void setIntegral(String integral) {
        this.integral = integral;
    }

    public String getReserve1() {
        return reserve1;
    }

    public void setReserve1(String reserve1) {
        this.reserve1 = reserve1;
    }

    public String getDomain_name() {
        return domain_name;
    }

    public void setDomain_name(String domain_name) {
        this.domain_name = domain_name;
    }

    public String getIntegral_sports() {
        return integral_sports;
    }

    public void setIntegral_sports(String integral_sports) {
        this.integral_sports = integral_sports;
    }

    public String getIntegral_bet_amount() {
        return integral_bet_amount;
    }

    public void setIntegral_bet_amount(String integral_bet_amount) {
        this.integral_bet_amount = integral_bet_amount;
    }

    public String getIntegral_bet_times() {
        return integral_bet_times;
    }

    public void setIntegral_bet_times(String integral_bet_times) {
        this.integral_bet_times = integral_bet_times;
    }

    public String getIntegral_recommend() {
        return integral_recommend;
    }

    public void setIntegral_recommend(String integral_recommend) {
        this.integral_recommend = integral_recommend;
    }

    public String getIntegral_deposit() {
        return integral_deposit;
    }

    public void setIntegral_deposit(String integral_deposit) {
        this.integral_deposit = integral_deposit;
    }

    public String getIntegral_ol_deposit() {
        return integral_ol_deposit;
    }

    public void setIntegral_ol_deposit(String integral_ol_deposit) {
        this.integral_ol_deposit = integral_ol_deposit;
    }

    public String getIntegral_sd_ep() {
        return integral_sd_ep;
    }

    public void setIntegral_sd_ep(String integral_sd_ep) {
        this.integral_sd_ep = integral_sd_ep;
    }

    public String getIntegral_used() {
        return integral_used;
    }

    public void setIntegral_used(String integral_used) {
        this.integral_used = integral_used;
    }

    public String getIs_special() {
        return is_special;
    }

    public void setIs_special(String is_special) {
        this.is_special = is_special;
    }

    public String getPhone_flag() {
        return phone_flag;
    }

    public void setPhone_flag(String phone_flag) {
        this.phone_flag = phone_flag;
    }

    public String getIntegral_flag() {
        return integral_flag;
    }

    public void setIntegral_flag(String integral_flag) {
        this.integral_flag = integral_flag;
    }

    public String getPromotion_flag() {
        return promotion_flag;
    }

    public void setPromotion_flag(String promotion_flag) {
        this.promotion_flag = promotion_flag;
    }

    public String getXm_flag() {
        return xm_flag;
    }

    public void setXm_flag(String xm_flag) {
        this.xm_flag = xm_flag;
    }

    public int getSms_times() {
        return sms_times;
    }

    public void setSms_times(int sms_times) {
        this.sms_times = sms_times;
    }

    public String getEffective_flag() {
        return effective_flag;
    }

    public void setEffective_flag(String effective_flag) {
        this.effective_flag = effective_flag;
    }

    public String getPwd_errors() {
        return pwd_errors;
    }

    public void setPwd_errors(String pwd_errors) {
        this.pwd_errors = pwd_errors;
    }

    public String getHg_flag() {
        return hg_flag;
    }

    public void setHg_flag(String hg_flag) {
        this.hg_flag = hg_flag;
    }

    public String getTry_worth_flag() {
        return try_worth_flag;
    }

    public void setTry_worth_flag(String try_worth_flag) {
        this.try_worth_flag = try_worth_flag;
    }

    public String getGame_credit() {
        return game_credit;
    }

    public void setGame_credit(String game_credit) {
        this.game_credit = game_credit;
    }

    public String getCurrency_rate() {
        return currency_rate;
    }

    public void setCurrency_rate(String currency_rate) {
        this.currency_rate = currency_rate;
    }

    public String getMatch_sign_flag() {
        return match_sign_flag;
    }

    public void setMatch_sign_flag(String match_sign_flag) {
        this.match_sign_flag = match_sign_flag;
    }

    public String getLast_game() {
        return last_game;
    }

    public void setLast_game(String last_game) {
        this.last_game = last_game;
    }

    public String getCall_flag() {
        return call_flag;
    }

    public void setCall_flag(String call_flag) {
        this.call_flag = call_flag;
    }

    public String getPt_key() {
        return pt_key;
    }

    public void setPt_key(String pt_key) {
        this.pt_key = pt_key;
    }

    public String getAp_effective_date() {
        return ap_effective_date;
    }

    public void setAp_effective_date(String ap_effective_date) {
        this.ap_effective_date = ap_effective_date;
    }

    public int getAp_key_status() {
        return ap_key_status;
    }

    public void setAp_key_status(int ap_key_status) {
        this.ap_key_status = ap_key_status;
    }

    public int getAp_key_reset_count() {
        return ap_key_reset_count;
    }

    public void setAp_key_reset_count(int ap_key_reset_count) {
        this.ap_key_reset_count = ap_key_reset_count;
    }

    public int getAp_key_errors_count() {
        return ap_key_errors_count;
    }

    public void setAp_key_errors_count(int ap_key_errors_count) {
        this.ap_key_errors_count = ap_key_errors_count;
    }

    public int getAp_bank_verify_start() {
        return ap_bank_verify_start;
    }

    public void setAp_bank_verify_start(int ap_bank_verify_start) {
        this.ap_bank_verify_start = ap_bank_verify_start;
    }

    public int getAp_bank_verify_end() {
        return ap_bank_verify_end;
    }

    public void setAp_bank_verify_end(int ap_bank_verify_end) {
        this.ap_bank_verify_end = ap_bank_verify_end;
    }

    public String getAp_transfer_status() {
        return ap_transfer_status;
    }

    public void setAp_transfer_status(String ap_transfer_status) {
        this.ap_transfer_status = ap_transfer_status;
    }

    public String getIs_tm_sales() {
        return is_tm_sales;
    }

    public void setIs_tm_sales(String is_tm_sales) {
        this.is_tm_sales = is_tm_sales;
    }

    public String getShow_balance() {
        return show_balance;
    }

    public void setShow_balance(String show_balance) {
        this.show_balance = show_balance;
    }

    public String getUser_token() {
        return user_token;
    }

    public void setUser_token(String user_token) {
        this.user_token = user_token;
    }

    public String getReal_name() {
        return real_name;
    }

    public void setReal_name(String real_name) {
        this.real_name = real_name;
    }
}
