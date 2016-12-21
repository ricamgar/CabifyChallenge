package com.ricamgar.challenge.domain.model;


public class LoginRequest {

    public final UserDevice userDevice;
    public final String username;
    public final String password;
    public final String grantType;

    public LoginRequest(UserDevice userDevice, String username, String password, String grantType) {
        this.userDevice = userDevice;
        this.username = username;
        this.password = password;
        this.grantType = grantType;
    }

    private class UserDevice {
        public final String riderPushToken;
        public final String appName;
        public final String make;
        public final String model;
        public final String systemVersion;
        public final String systemName;
        public final String uuid;

        private UserDevice(String riderPushToken, String appName, String make, String model,
                           String systemVersion, String systemName, String uuid) {
            this.riderPushToken = riderPushToken;
            this.appName = appName;
            this.make = make;
            this.model = model;
            this.systemVersion = systemVersion;
            this.systemName = systemName;
            this.uuid = uuid;
        }
    }
}
