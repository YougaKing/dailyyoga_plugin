package com.mob.products.mobadsdk

import com.mob.AutoCorrectNode

class MobAdInfoNode extends AutoCorrectNode {

    Set getFieldNames() {
        return [
                "AppId",
                "Enable",
                "Version"
        ]
    }

}