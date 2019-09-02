package com.lvideo.http;

import java.util.HashMap;

/**
 * Author: liuxiaojiang(liuxiaojiang@lvideo.com)
 * Date: 2016-03-11
 * Time: 11:54
 * Description: 请求时需要另加上的header
 */
public class LVideoRequestHeader {
    private String                  kToken   = "";
    private String                  kUid     = "";
    private String                  kAppid   = "";
    private String                  kMarket  = "";
    private String                  kVersion = "";
    private String                  kTeminal = "";
    private String                  kModel;
    private String                  kScreen;
    private String                  kOS;
    private String                  kSdk;
    private String                  kUuid;
    private String                  kDc;
    private String                  kAuid;
    private String                  kLdid    = "";
    private HashMap<String, String> hashMap;

    public String getkToken() {
        return kToken;
    }

    public void setkToken(String kToken) {
        this.kToken = kToken;
    }

    public String getkUid() {
        return kUid;
    }

    public void setkUid(String kUid) {
        this.kUid = kUid;
    }

    public String getkAppid() {
        return kAppid;
    }

    public void setkAppid(String kAppid) {
        this.kAppid = kAppid;
    }

    public String getkMarket() {
        return kMarket;
    }

    public void setkMarket(String kMarket) {
        this.kMarket = kMarket;
    }

    public String getkVersion() {
        return kVersion;
    }

    public void setkVersion(String kVersion) {
        this.kVersion = kVersion;
    }

    public String getkTeminal() {
        return kTeminal;
    }

    public void setkTeminal(String kTeminal) {
        this.kTeminal = kTeminal;
    }

    public String getkModel() {
        return kModel;
    }

    public void setkModel(String kModel) {
        this.kModel = kModel;
    }

    public String getkScreen() {
        return kScreen;
    }

    public void setkScreen(String kScreen) {
        this.kScreen = kScreen;
    }

    public String getkOS() {
        return kOS;
    }

    public void setkOS(String kOS) {
        this.kOS = kOS;
    }

    public String getkUuid() {
        return kUuid;
    }

    public void setkUuid(String kUuid) {
        this.kUuid = kUuid;
    }

    public String getkDc() {
        return kDc;
    }

    public void setkDc(String kDc) {
        this.kDc = kDc;
    }

    public String getkSdk() {
        return kSdk;
    }

    public void setkSdk(String kSdk) {
        this.kSdk = kSdk;
    }

    public String getkAuid() {
        return kAuid;
    }

    public void setkAuid(String kAuid) {
        this.kAuid = kAuid;
    }

    public HashMap<String, String> getDefineMap() {
        return hashMap;
    }

    public void setDefineMap(HashMap<String, String> map) {
        hashMap = map;
    }

    public String getkLdid() {
        return kLdid;
    }

    public void setkLdid(String kLdid) {
        this.kLdid = kLdid;
    }
}
