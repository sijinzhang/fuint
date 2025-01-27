package com.fuint.util;

/**
 * 系统常量定义接口
 *
 * Created by FSQ
 * Contact wx fsq_better
 * Site https://www.fuint.cn
 */
public interface Constant {
    /**
     * 密码加密过程系统常量
     */
    public interface SaltConstant {
        /**
         * sha1算法常量
         */
        public static final String HASH_ALGORITHM = "SHA-1";
        /**
         * sha次数
         */
        public static final int HASH_INTERATIONS = 1024;
        /**
         * 生成salt中随机的Byte[]的长度
         */
        public static final int SALT_SIZE = 8;

    }

    public interface sessionConstant {
        /**
         * 强制退出会话标识
         */
        public static final String SESSION_FORCE_LOGOUT_KEY = "session.force.logout";
    }

}
