package com.seller.panel.util;

public final class EndPointConstants {

    private EndPointConstants() {
        //
    }

    public static final String ENDPOINTS_PREFIX = "/api/v1";

    public static final class Ping {
        private Ping() {
            //
        }

        public static final String PING = "/ping";

    }

    public static final class Join {
        private Join() {
            //
        }

        public static final String JOIN = ENDPOINTS_PREFIX+"/join";

    }

    public static final class Invitation {
        private Invitation() {
            //
        }

        public static final String INVITE = ENDPOINTS_PREFIX+"/invite/{access_token_id}";

    }

    public static final class Registration {
        private Registration() {
            //
        }

        public static final String REGISTER = ENDPOINTS_PREFIX+"/register";

    }

    public static final class Login {
        private Login() {
            //
        }

        public static final String LOGIN = ENDPOINTS_PREFIX+"/login";

    }


}
