package com.mob.products

import com.mob.ConfigCreator

class AWKConfig extends MobProductConfig {
    ConfigCreator config
    boolean alive = true

    void alive(boolean alive) {
        this.alive = alive
    }

    void setConfigCreator(ConfigCreator config) {
        this.config = config
        hiddenMembers {
            mGuard = true
        }
    }

    def methodMissing(String name, Object args) {
       if ("mGuard".equalsIgnoreCase(name)) {
            if (args == null) {
                mGuard = true
            } else if (args.length == 0) {
                mGuard = true
            } else {
                mGuard = "true".equalsIgnoreCase(String.valueOf(args[0]))
            }
        }
        return super.methodMissing(name, args)
    }
}