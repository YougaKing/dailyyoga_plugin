package com.mob.products.secverify

import com.mob.AutoCorrectNode

class SecVerifyInfoNode extends AutoCorrectNode {
    Set getFieldNames() {
        return [
                "Enable",
                "Version"
        ]
    }
}