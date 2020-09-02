package com.mob.products.mobpush

import com.mob.ConfigCreator
import com.mob.DObject

class PluginsDevInfo extends DObject {
	static Set factories
	ConfigCreator config
	String translationChar = "\\"
	int sdkVersion

	static {
		factories = [
				"HUAWEI",
				"XIAOMI",
				"MEIZU",
				"FCM",
				"OPPO",
				"VIVO"
		]
	}

	void setConfigCreator(ConfigCreator config) {
		this.config = config
		String version = config.gradlePluginVersion
		if (version == null || !version.startsWith("3.")) {
			translationChar = "\\ "
		}
	}

	void setSdkVersion(int sdkVersion) {
		this.sdkVersion = sdkVersion
	}

	def methodMissing(String name, def args) {
		String fac = factories.find {
			return name.equalsIgnoreCase(it)
		}
		if (fac != null) {
			PluginsInfoNode info = new PluginsInfoNode()
			info.closure = args[0]
			addActivity(fac, info)
			addDependency(fac, info)
		}
	}

	private void addActivity(String name, PluginsInfoNode info) {
		String appidTag = '${applicationId}'
		if ("HUAWEI".equals(name)) {
			if (sdkVersion == 0 || sdkVersion >= 20400) {
				config.directToAdd.add("""
					<meta-data
						xmlns:android="http://schemas.android.com/apk/res/android"
						android:name="com.huawei.hms.client.appid"
						android:value="${translationChar}${info.AppId}">
					</meta-data>
				""")
				return;
			}
			config.directToAdd.add("""
				<activity
					xmlns:android="http://schemas.android.com/apk/res/android"
					xmlns:tools="http://schemas.android.com/tools"
					tools:node="merge"
                    android:name="com.huawei.hms.activity.BridgeActivity"
                    android:configChanges="orientation|locale|screenSize|layoutDirection|fontScale"
                    android:excludeFromRecents="true"
                    android:exported="false"
                    android:hardwareAccelerated="true"
                    android:theme="@android:style/Theme.Translucent" >
                    <meta-data
						android:name="hwc-theme"
						android:value="androidhwext:style/Theme.Emui.Translucent" />
                </activity>
			""")
			config.directToAdd.add("""
				<provider
					xmlns:android="http://schemas.android.com/apk/res/android"
                    android:name="com.huawei.hms.update.provider.UpdateProvider"
                    android:authorities="${appidTag}.hms.update.provider"
                    android:exported="false"
                    android:grantUriPermissions="true" >
                </provider>
			""")
			config.directToAdd.add("""
				<receiver 
				xmlns:android="http://schemas.android.com/apk/res/android"
				android:name="com.mob.pushsdk.plugins.huawei.PushHaiWeiRevicer">
                    <intent-filter>
                        <action android:name="com.huawei.android.push.intent.REGISTRATION" />
                        <action android:name="com.huawei.android.push.intent.RECEIVE" />
                        <action android:name="com.huawei.android.push.intent.CLICK" />
                        <action android:name="com.huawei.intent.action.PUSH_STATE" />
                    </intent-filter>
                </receiver>
			""")
			config.directToAdd.add("""
				<receiver 
				xmlns:android="http://schemas.android.com/apk/res/android"
				android:name="com.huawei.hms.support.api.push.PushEventReceiver"
                          android:exported="false">
                    <intent-filter>
                        <action android:name="com.huawei.intent.action.PUSH" />
                    </intent-filter>
                </receiver>
			""")
			config.directToAdd.add("""
				<meta-data
					xmlns:android="http://schemas.android.com/apk/res/android"
                    android:name="com.huawei.hms.client.appid"
                    android:value="${translationChar}${info.AppId}">
                </meta-data>
			""")
		} else if ("XIAOMI".equals(name)) {
			if(sdkVersion != 0 && sdkVersion < 10400 ) {
				config.directToAdd.add("""
					<service
						xmlns:android="http://schemas.android.com/apk/res/android"
						android:enabled="true"
						android:process=":pushservice"
						android:name="com.xiaomi.push.service.XMPushService"/>
				""")
				config.directToAdd.add("""
					<service
						xmlns:android="http://schemas.android.com/apk/res/android"
						android:name="com.xiaomi.push.service.XMJobService"
						android:enabled="true"
						android:exported="false"
						android:permission="android.permission.BIND_JOB_SERVICE"
						android:process=":pushservice" />
				""")
				config.directToAdd.add("""
					<receiver
						xmlns:android="http://schemas.android.com/apk/res/android"
						android:exported="false"
						android:process=":pushservice"
						android:name="com.xiaomi.push.service.receivers.PingReceiver" >
						<intent-filter>
							<action android:name="com.xiaomi.push.PING_TIMER" />
						</intent-filter>
					</receiver>
				""")
			}
			config.directToAdd.add("""
				 <service
				 	xmlns:android="http://schemas.android.com/apk/res/android"
                    android:enabled="true"
                    android:exported="true"
                    android:name="com.xiaomi.mipush.sdk.PushMessageHandler" />
			""")
			config.directToAdd.add("""
				<service
					xmlns:android="http://schemas.android.com/apk/res/android"
                    android:enabled="true"
                    android:name="com.xiaomi.mipush.sdk.MessageHandleService" />
			""")
			config.directToAdd.add("""
				<receiver
					xmlns:android="http://schemas.android.com/apk/res/android"
                    android:exported="true"
                    android:name="com.xiaomi.push.service.receivers.NetworkStatusReceiver" >
                    <intent-filter> <action android:name="android.net.conn.CONNECTIVITY_CHANGE" />
                        <category android:name="android.intent.category.DEFAULT" />
                    </intent-filter>
                </receiver>
			""")
			config.directToAdd.add("""
				<receiver
					xmlns:android="http://schemas.android.com/apk/res/android"
                    android:exported="true"
                    android:name="com.mob.pushsdk.plugins.xiaomi.PushXiaoMiRevicer">
                    <intent-filter>
                        <action android:name="com.xiaomi.mipush.RECEIVE_MESSAGE" />
                    </intent-filter>
                    <intent-filter>
                        <action android:name="com.xiaomi.mipush.MESSAGE_ARRIVED" />
                    </intent-filter>
                    <intent-filter>
                        <action android:name="com.xiaomi.mipush.ERROR" />
                    </intent-filter>
                </receiver>
			""")
			config.directToAdd.add("""
				<meta-data
					xmlns:android="http://schemas.android.com/apk/res/android"
                    android:name="com.mob.push.xiaomi.appid"
                    android:value="${translationChar}${info.AppId}"/>
			""")
			config.directToAdd.add("""
				<meta-data
					xmlns:android="http://schemas.android.com/apk/res/android"
                    android:name="com.mob.push.xiaomi.appkey"
                    android:value="${translationChar}${info.AppKey}"/>
			""")
		} else if ("MEIZU".equals(name)) {
			config.directToAdd.add("""
				<receiver
					xmlns:android="http://schemas.android.com/apk/res/android"
					android:name="com.mob.pushsdk.plugins.meizu.PushMeiZuRevicer">
                    <intent-filter>
                        <action android:name="com.meizu.flyme.push.intent.MESSAGE" />
                        <action android:name="com.meizu.flyme.push.intent.REGISTER.FEEDBACK" />
                        <action android:name="com.meizu.flyme.push.intent.UNREGISTER.FEEDBACK"/>
                        <action android:name="com.meizu.c2dm.intent.REGISTRATION" />
                        <action android:name="com.meizu.c2dm.intent.RECEIVE" />
                        <category android:name="${appidTag}"></category>
                    </intent-filter>
                </receiver>
			""")
			config.directToAdd.add("""
				<meta-data
					xmlns:android="http://schemas.android.com/apk/res/android"
                    android:name="com.mob.push.meizu.appid"
                    android:value="${translationChar}${info.AppId}"/>
			""")
			config.directToAdd.add("""
				<meta-data
					xmlns:android="http://schemas.android.com/apk/res/android"
                    android:name="com.mob.push.meizu.appkey"
                    android:value="${translationChar}${info.AppKey}"/>
			""")
		} else if ("FCM".equals(name)){
			config.directToAdd.add("""
				<service
					xmlns:android="http://schemas.android.com/apk/res/android"
            		android:name="com.mob.pushsdk.plugins.fcm.FCMFirebaseMessagingService">
            			<intent-filter>
                			<action android:name="com.google.firebase.MESSAGING_EVENT"/>
            			</intent-filter>
        		</service>
			""")
			config.directToAdd.add("""
				<meta-data
					xmlns:android="http://schemas.android.com/apk/res/android"
        			android:name="com.google.firebase.messaging.default_notification_icon"
        			android:resource="${info.IconRes}" />
			""")
		} else if ("OPPO".equals(name)){
            if (sdkVersion != 0 && sdkVersion <= 20401) {
                config.directToAdd.add("""
				<service
					xmlns:android="http://schemas.android.com/apk/res/android"
            		android:name="com.mob.pushsdk.plugins.oppo.PushOppoReceiver"
            		android:permission="com.coloros.mcs.permission.SEND_MCS_MESSAGE">
            			<intent-filter>
							<action android:name="com.coloros.mcs.action.RECEIVE_MCS_MESSAGE" />
						</intent-filter>
        		</service>
			""")
            }
			config.directToAdd.add("""
				<meta-data
					xmlns:android="http://schemas.android.com/apk/res/android"
        			android:name="com.mob.push.oppo.appkey"
					android:value="${translationChar}${info.AppKey}" />
			""")
			config.directToAdd.add("""
				<meta-data
					xmlns:android="http://schemas.android.com/apk/res/android"
        			android:name="com.mob.push.oppo.appsecret"
					android:value="${translationChar}${info.AppSecret}" />
			""")
		} else if ("VIVO".equals(name)) {
			config.directToAdd.add("""
				<service
					xmlns:android="http://schemas.android.com/apk/res/android"
					android:name="com.vivo.push.sdk.service.CommandClientService"
					android:exported="true" />
			""")
			config.directToAdd.add("""
				<activity
					xmlns:android="http://schemas.android.com/apk/res/android"
					android:name="com.vivo.push.sdk.LinkProxyClientActivity"
					android:exported="false"
					android:screenOrientation="portrait"
					android:theme="@android:style/Theme.Translucent.NoTitleBar" />
			""")
			config.directToAdd.add("""
				<receiver
					xmlns:android="http://schemas.android.com/apk/res/android"
					android:name="com.mob.pushsdk.plugins.vivo.PushVivoReceiver">
					<intent-filter>
						<action android:name="com.vivo.pushclient.action.RECEIVE" />
					</intent-filter>
				</receiver>
			""")
			config.directToAdd.add("""
				<meta-data
					xmlns:android="http://schemas.android.com/apk/res/android"
					android:name="com.vivo.push.api_key"
					android:value="${translationChar}${info.AppKey}" />
			""")
			config.directToAdd.add("""
				<meta-data
					xmlns:android="http://schemas.android.com/apk/res/android"
					android:name="com.vivo.push.app_id"
					android:value="${translationChar}${info.AppId}" />
			""")
		}
	}

	private void addDependency(String platform, PluginsInfoNode info) {
		String version = config.gradlePluginVersion
		String libraryName
		if ("HUAWEI".equals(platform)) {
			if (sdkVersion == 0 || sdkVersion >= 20400) {
                config.project.repositories.maven { maven ->
                    maven.url = "http://developer.huawei.com/repo/"
                }
                if (info.version == null || info.verion == "" || info.verion == "null") {
                    info.version = "4.0.2.300"
                }
                libraryName = "com.huawei.hms:push:${info.version}"
                //				config.project.plugins.apply("com.huawei.agconnect") 这个操作只是添加了appid
            } else if(sdkVersion >= 10600){
                libraryName = 'com.mob.push.plugins:huawei:+@aar'
            } else if(sdkVersion >= 10400){
                libraryName = 'com.mob.push.plugins:huawei:2.6.0.1@aar'
            } else{
                config.project.repositories.maven {
                    url 'http://developer.huawei.com/repo/'
                }
                libraryName = 'com.huawei.android.hms:push:2.6.0.301'
            }

			if (version == null || version.startsWith("3.")){
				config.project.dependencies {
					add('implementation', libraryName)
				}
			} else{
				config.project.dependencies {
					add('compile', libraryName)
				}
			}
			config.permissions.addAll(["android.permission.REQUEST_INSTALL_PACKAGES"])
		}
		if ("XIAOMI".equals(platform)) {
			if(sdkVersion == 0 || sdkVersion >= 10600){
				libraryName = 'com.mob.push.plugins:xiaomi:+@aar'
			} else if(sdkVersion >= 10400){
				libraryName = 'com.mob.push.plugins:xiaomi:3.6.1.2@aar'
			} else{
				libraryName = 'com.mob.push.plugins:xiaomi:3.6.1@aar'
			}
			if (version == null || version.startsWith("3.")){
				config.project.dependencies {
					add('implementation', libraryName)
				}
			} else{
				config.project.dependencies {
					add('compile', libraryName)
				}
			}
			config.permissions.addAll(['${applicationId}.permission.MIPUSH_RECEIVE'])
			config.customPermissions.addAll(['${applicationId}.permission.MIPUSH_RECEIVE'])
		}
		if ("MEIZU".equals(platform)) {
			if(sdkVersion == 0 || sdkVersion >= 10600){
				libraryName = 'com.mob.push.plugins:meizu:+@aar'
			} else if(sdkVersion >= 10400){
				libraryName = 'com.mob.push.plugins:meizu:3.6.5.2@aar'
			} else {
				libraryName = 'com.meizu.flyme.internet:push-internal:3.6.5'
			}
			if (version == null || version.startsWith("3.")){
				config.project.dependencies {
					add('implementation', libraryName)
				}
			} else{
				config.project.dependencies {
					add('compile', libraryName)
				}
			}
			config.permissions.addAll([
					"com.meizu.flyme.push.permission.RECEIVE",
					'${applicationId}.push.permission.MESSAGE',
					"com.meizu.c2dm.permission.RECEIVE",
					'${applicationId}.permission.C2D_MESSAGE'
			])
			config.customPermissions.addAll([
					'${applicationId}.push.permission.MESSAGE',
					'${applicationId}.permission.C2D_MESSAGE'
			])
		}
		if ("FCM".equals(platform)) {
			if (info.version == null || info.verion == "" || info.verion == "null") {
				info.version = "+"
			}
			def fcmRepo = "com.google.firebase:firebase-messaging:${info.version}"
			if (version == null || version.startsWith("3.")){
				config.project.repositories.google()
				config.project.dependencies {
					add('implementation', fcmRepo)
				}
			} else{
				config.project.repositories.maven { maven ->
					maven.url = "http://mvn.mob.com/android"
				}
				config.project.dependencies {
					add('compile', fcmRepo)
				}
			}
		}
		if("OPPO".equals(platform)){
			if (version == null || version.startsWith("3.")){
				config.project.dependencies {
					add('implementation', 'com.mob.push.plugins:oppo:+@aar')
				}
			} else{
				config.project.dependencies {
					add('compile', 'com.mob.push.plugins:oppo:+@aar')
				}
			}
		}
		if("VIVO".equals(platform)){
			if (version == null || version.startsWith("3.")){
				config.project.dependencies {
					add('implementation', 'com.mob.push.plugins:vivo:+@aar')
				}
			} else {
				config.project.dependencies {
					add('compile', 'com.mob.push.plugins:vivo:+@aar')
				}
			}
		}
	}
}