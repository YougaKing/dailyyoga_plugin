package com.mob.solutions

class GrowSolutionConfig extends SolutionConfig {

	GrowSolutionConfig() {
		hiddenMembers {
			baiduMobAdID = 'bae4740c'
		}
	}

	def methodMissing(String name, def args) {
		if ("BaiduMobAdID".equalsIgnoreCase(name)) {
			baiduMobAdID = args[0]
		}
		return super.methodMissing(name, args)
	}

	void process() {
		mobSDK.ShareSDK {
			disableConfig true
			devInfo {
				SinaWeibo {}
				Wechat {}
				QQ {}
				QZone {}
				WechatMoments {}
			}
		}

		mobSDK.CMSSDK {
			gui false
		}

		mobSDK.CloudStorage {}

		mobSDK.addDependencies('com.mob:GrowSolution:+@aar')

		mobSDK.addDependencies('com.mob:BaiduMobAds:+')
		mobSDK.configCreator.applicationMetaData.BaiduMobAd_APP_ID = baiduMobAdID
		String appidTag = '${applicationId}'
		mobSDK.configCreator.directToAdd.add("""
				<provider
						xmlns:android="http://schemas.android.com/apk/res/android" 
						xmlns:tools="http://schemas.android.com/tools"
						tools:node="merge"
						android:name="com.baidu.mobads.openad.FileProvider"
						android:authorities="${appidTag}.bd.provider"
						android:exported="false"
						android:grantUriPermissions="true">
					<meta-data
							xmlns:android="http://schemas.android.com/apk/res/android" 
							xmlns:tools="http://schemas.android.com/tools"
							tools:node="merge"
							android:name="android.support.FILE_PROVIDER_PATHS"
							android:resource="@xml/bd_file_paths" />
				</provider>
		""")
		mobSDK.configCreator.directToAdd.add("""
				<activity
						xmlns:android="http://schemas.android.com/apk/res/android" 
						xmlns:tools="http://schemas.android.com/tools"
						tools:node="merge"
						android:name="com.baidu.mobads.AppActivity" 
						android:configChanges="screenSize|keyboard|keyboardHidden|orientation" 
						android:theme="@android:style/Theme.Translucent.NoTitleBar"/>
		""")
	}
}