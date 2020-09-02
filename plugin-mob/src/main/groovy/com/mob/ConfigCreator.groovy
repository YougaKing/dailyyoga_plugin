package com.mob

import groovy.xml.XmlUtil
import org.gradle.api.Project

import java.lang.reflect.Field
import java.util.zip.ZipEntry
import java.util.zip.ZipFile

class ConfigCreator {
	Project project

	void setProject(Project project) {
		this.project = project
		project.configurations.all {
            //采用动态版本声明的依赖缓存2小时
			resolutionStrategy.cacheDynamicVersionsFor 2, 'hours'
            //每隔1小时检查远程依赖是否存在更新
			resolutionStrategy.cacheChangingModulesFor 60 * 60, 'seconds'
		}
	}

	Project getProject() {
		return project
	}

	private Map getGlobalVariants() {
		boolean found = false
		try {
			def fld = project.rootProject.getClass().getDeclaredField("mobsdk_global_variants")
			found = fld != null
		} catch (Throwable t) {}
		if (!found) {
			project.rootProject.metaClass.mobsdk_global_variants = [:]
		}
		return project.rootProject.mobsdk_global_variants
	}

	void setAppBundleMode(String appBundleMode) {
		globalVariants.appBundleMode = appBundleMode
	}

	void setDelMobTmpDir(String delMobTmpDir) {
		globalVariants.delMobTmpDir = delMobTmpDir
	}

    void setUseMobPrivacy(String useMobPrivacy) {
        globalVariants.useMobPrivacy = useMobPrivacy
    }

    void setAppkey(String appkey) {
		globalVariants.appkey = appkey
	}

	void setAppSecret(String appSecret) {
		globalVariants.appSecret = appSecret
	}

	void setAutoConfig(boolean autoConfig) {
		globalVariants.autoConfig = autoConfig
	}

	void setDouyinCallbackAct(String douyinCallbackAct) {
		globalVariants.douyinCallbackAct = douyinCallbackAct
	}

	void setGooglePlusOfficialVersion(String officialVersion) {
		globalVariants.googlePlusOfficialVersion = officialVersion
	}

	void setFacebookOfficialVersion(String officialVersion) {
		globalVariants.facebookOfficialVersion = officialVersion
	}

	String getGooglePlusOfficialVersion() {
		return globalVariants.googlePlusOfficialVersion
	}

	String getFacebookOfficialVersion() {
		return globalVariants.facebookOfficialVersion
	}

	void setAutoInit(String autoInit) {
		globalVariants.autoInit = autoInit
	}

	Set getActivitiesToAdd() {
		Set activitiesToAdd = globalVariants.activitiesToAdd
		if (activitiesToAdd == null) {
			activitiesToAdd = []
			globalVariants.activitiesToAdd = activitiesToAdd
		}
		return activitiesToAdd
	}

	Set getIntents() {
		Set intents = globalVariants.intents
		if (intents == null) {
			intents = []
			globalVariants.intents = intents
		}
		return intents
	}

	Set getDirectToAdd() {
		Set directToAdd = globalVariants.directToAdd
		if (directToAdd == null) {
			directToAdd = []
			globalVariants.directToAdd = directToAdd
		}
		return directToAdd
	}

	Map getSharesdkInfoMap() {
		Map sharesdkInfoMap = globalVariants.sharesdkInfoMap
		if (sharesdkInfoMap == null) {
			sharesdkInfoMap = [:]
			globalVariants.sharesdkInfoMap = sharesdkInfoMap
		}
		return sharesdkInfoMap
	}

	Set getPermissions() {
		Set permissions = globalVariants.permissions
		if (permissions == null) {
			permissions = []
			globalVariants.permissions = permissions
		}
		return permissions
	}

	Set getFeatures() {
		Set features = globalVariants.features
		if (features == null) {
			features = []
			globalVariants.features = features
		}
		return features
	}

	Set getCustomPermissions() {
		Set permissions = globalVariants.customPermissions

		if (permissions == null) {
			permissions = []
			globalVariants.customPermissions = permissions
		}
		return permissions
	}

	Set getExcludePermissions() {
		Set permissions = globalVariants.excludePermissions
		if (permissions == null) {
			permissions = []
			globalVariants.excludePermissions = permissions
		}
		return permissions
	}

	void setUpdateCacheDynamicVersionsFor(int seconds) {
		if (seconds < 0 || seconds > 86400) {
			seconds = 60 * 10
		}

		project.configurations.all {
			resolutionStrategy.cacheDynamicVersionsFor seconds, 'seconds'
			resolutionStrategy.cacheChangingModulesFor 60 * 60, 'seconds'
		}
	}

	String getGradlePluginVersion() {
		String version = getRealGradlePluginVersion()
		if(version != null && version.length() > 0){
			if (!Character.isDigit(version.charAt(0))){
				version = "3."
			} else{
				int startNum = Integer.valueOf(version.charAt(0).toString())
				if(startNum > 3){
					version = "3."
				}
			}
		}
		return version
	}

	String getRealGradlePluginVersion() {
		String version = globalVariants.gradlePluginVersion
		if (version == null) {
			try{
				// 反射去拿插件版本号
				Class myClass = Class.forName("com.android.build.gradle.internal.Version")
				Object obj = myClass.newInstance()
				Field field = myClass.getField("ANDROID_GRADLE_PLUGIN_VERSION")
				version = field.get(obj).toString()
			} catch (Throwable t){
				//反射拿不到就读文件
				try{
					Class clz = Class.forName("com.android.build.gradle.AppPlugin")
					URL url = clz.getProtectionDomain().getCodeSource().getLocation()
					File file = new File(url.toURI())
					ZipFile zip = new ZipFile(file)
					ZipEntry ent = zip.getEntry("META-INF/MANIFEST.MF")
					InputStream is = zip.getInputStream(ent)
					Properties prop = new Properties()
					prop.load(is)
					is.close()
					zip.close()
					version = prop.getProperty("Plugin-Version")

					if ("unspecified".equals(version)) {
						String name = file.name.toLowerCase()
						if (name.startsWith("gradle") && name.endsWith(".jar") && name.concat("-")) {
							int index = name.indexOf("-")
							version = name.substring(index + 1, name.length() - 4)
						}
					}
				} catch (Throwable tt){}
			}
			globalVariants.gradlePluginVersion = version
		}
		return version
	}

	Map getApplicationMetaData() {
		Map metaData = globalVariants.applicationMetaData
		if (metaData == null) {
			metaData = [:]
			globalVariants.applicationMetaData = metaData
		}
		return metaData
	}

	void setDisableShareSDKConfig(boolean disable) {
		globalVariants.disableShareSDKConfig = disable
	}

	void setPlaceShareSDKConfigHere(String path) {
		Map map = [:]
		map.project = project.projectDir
		map.path = path
		globalVariants.placeShareSDKConfigHere = map
	}

	boolean isDisableShareSDKConfig() {
		return "true".equalsIgnoreCase(String.valueOf(globalVariants.disableShareSDKConfig))
	}

	void setDomain(String domain) {
		globalVariants.domain = domain
	}

	void setHttps(boolean https) {
		globalVariants.https = https
	}

	void setMobLinkUri(String uri) {
		globalVariants.mobLinkUri = uri
	}

	void setMobLinkAppLinkHost(String host) {
		globalVariants.mobLinkAppLinkHost = host
	}

	void setMobLinkAppLinkAutoVerify(boolean autoVerify) {
		globalVariants.mobLinkAppLinkAutoVerify = autoVerify
	}

	void setLoopShare(boolean loopShare) {
		globalVariants.loopShare = loopShare
	}

	void setMobPushDefaultOpenActivityName(String activityName) {
		globalVariants.mobPushDefaultOpenActivityName = activityName
	}

    void setIpv6(boolean ipv6) {
        globalVariants.ipv6 = ipv6
    }

	// ===========================

	void create() {
		project.afterEvaluate {
			def android = project.extensions.getByName("android")
			if (globalVariants.autoConfig == null) {
				globalVariants.autoConfig = true
			}
			if (globalVariants.autoConfig) {
				if (android != null) {
					configShareSDKXML(android)

					def variants = null
					boolean appModel = false
					try {
						variants = android.applicationVariants
						appModel = true
					} catch (Throwable t) {
						try {
							variants = android.libraryVariants
						} catch (Throwable tt) {}
					}
					if (variants != null) {
						variants.all { variant ->
							variant.outputs.each { output ->
                                try {
                                    def task = getProcessManifestTask(output)
									if (task != null) {
										task.doLast {
											configManifest(output, appModel, variant, false)
											if (globalVariants.appBundleMode) {
												println("---- merge bundle manifest ----")
												configManifest(output, appModel, variant, true)
											}
                                        }
                                    }
                                } catch (Throwable t){}
							}
							try {
								//TODO 先不删除防止版本兼容有问题，用户如果强制要求可以通过配置delMobTmpDir true来进行删除
								if (globalVariants.delMobTmpDir) {
									def assetsTask = getMergeAssetsTask(variant)
									if (assetsTask != null) {
										assetsTask.doLast {
											File tmpMobAssetsDir = new File(project.rootProject.projectDir, "tmpmob")
											if (tmpMobAssetsDir.exists()) {
												tmpMobAssetsDir.deleteDir()
											}
										}
									}
								}
							} catch (Throwable t){}
						}
					}
				}
			}
		}
	}

	private File findAssetsDir(def android, String targetFile) {
		def assetsDir = null
		def assetsDirs = android.sourceSets.main.assets.srcDirs
		assetsDirs.each { dir ->
			dir.list().each { name ->
				if (targetFile.equals(name)) {
					assetsDir = dir
					new File(dir, name).delete()
					return
				}
			}
		}

		if (assetsDir == null) {
			def map = globalVariants.placeShareSDKConfigHere
			def cproj = null
			def cpath = null
			if (map != null) {
				try {
					cproj = map.project
					cpath = map.path
				} catch (Throwable t) {}
			}
			if (cproj != null) {
				String assetsFolder = cpath == null ? "assets" : cpath
				assetsDir = new File(cproj, assetsFolder)
			} else {
				String assetsFolder = "tmpmob${File.separator}ShareSDK${File.separator}assets"
				assetsDir = new File(project.rootProject.projectDir, assetsFolder)
			}
			if (assetsDirs.size() > 0) {
				HashSet s = new HashSet()
				s.addAll(assetsDirs)
				s.add(assetsDir)
				android.sourceSets.main.assets.srcDirs = s
			} else {
				android.sourceSets.main.assets.srcDirs = [assetsDir]
			}
		}

		if (!assetsDir.exists()) {
			assetsDir.mkdirs()
		}
		return assetsDir
	}

	// 将配置中的devInfo添加到ShareSDK.xml中
	private void configShareSDKXML(def android) {
		boolean disable = isDisableShareSDKConfig()
		Map sharesdkInfoMap = globalVariants.sharesdkInfoMap
		if (sharesdkInfoMap != null && !sharesdkInfoMap.isEmpty()) {
			def sb = new StringBuilder()
			sharesdkInfoMap.each { platform, info ->
				if (info == null || info.isEmpty()) {
					if (!disable) {
						sb.append("<").append(platform).append(" ")
						sb.append("Enable=\"false\" />\n")
					}
				} else {
					sb.append("<").append(platform).append(" ")
					info.each {key, value ->
						sb.append(key).append("=\"").append(value).append("\"").append(" ")
					}
					sb.append("/>\n")
				}
			}

			if (sb.length() > 0) {
				StringBuilder xml = new StringBuilder()
				xml.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n")
				xml.append("<DevInfor>\n")
				xml.append(sb)
				xml.append("</DevInfor>")
				File assetsDir = findAssetsDir(android, "ShareSDK.xml")
				File xmlFile = new File(assetsDir, "ShareSDK.xml")
				xmlFile.setText(xml.toString(), "UTF-8")
			}
		}
	}

	// 修改AndroidManifest.xml
	private void configManifest(def output, boolean appModel, def variant, boolean bundleManifest) {
		String packageName = null
		if (appModel) {
			try {
				packageName = variant.applicationId
			} catch (Throwable t) {}
		}

		def manifestFiles
		if (bundleManifest) {
			manifestFiles = getBundleManifestOutputFile(output)
		} else {
			manifestFiles = getManifestOutputFile(output, appModel)
		}

		manifestFiles.each { manifestFile->
			if (manifestFile != null && manifestFile.exists()) {
				def parser = new XmlSlurper()
				def manifest = parser.parse(manifestFile)

				// 尝试自动加入权限列表
				def usedPermissions = []
				def shouldAdd = []
				manifest.'uses-permission'.each { permission ->
					String permissionName = permission.getProperty('@android:name').toString()
					usedPermissions.add(permissionName)
//					if (permissionName.equals("android.permission.READ_PHONE_STATE")) {
//						//为READ_PHONE_STATE添加maxSdkVersion限制，适配Android 29
//						def permissionMaxSdkVersion = permission.@'android:maxSdkVersion'.toString()
//						if (permissionMaxSdkVersion == null || permissionMaxSdkVersion.length() == 0) {
//							//如果没有设置maxSdkVersion，则删掉该节点，然后添加到shouldAdd里面去
//							permission.replaceNode{}
//							shouldAdd.add(permissionName)
//						}
//					}
				}

				Set permissions = globalVariants.permissions
				if (permissions != null) {
					Set excludePermissions = globalVariants.excludePermissions
					if (excludePermissions == null) {
						excludePermissions = []
					}
					permissions.each { permission ->
						if (!usedPermissions.contains(permission) && !excludePermissions.contains(permission)) {
							shouldAdd.add(permission)
						}
					}
				}
				def ns = 'xmlns:android="http://schemas.android.com/apk/res/android"'
				shouldAdd.each { per ->
					String lastPermission
//					if (per.equals("android.permission.READ_PHONE_STATE")) {
//						//为READ_PHONE_STATE添加maxSdkVersion限制，适配Android 29
//						lastPermission =  """
//							<uses-permission ${ns}
//								android:name="${per}"
//								android:maxSdkVersion="28"/>
//							"""
//					} else {
						lastPermission = "<uses-permission ${ns} android:name=\"${per}\" />"
//					}
					if (packageName != null && lastPermission.contains('${applicationId}')) {
						lastPermission = lastPermission.replace('${applicationId}', packageName)
					}
					def permission = parser.parseText(lastPermission)
					manifest.appendNode(permission)
				}

				// 尝试加入特性列表
				def usedFeatures = []
				manifest.'uses-feature'.each { feature ->
					usedFeatures.add(feature.getProperty('@android:name').toString())
				}
				shouldAdd = []
				Set features = globalVariants.features
				if (features != null) {
					features.each { feature ->
						if (!usedFeatures.contains(feature)) {
							shouldAdd.add(feature)
						}
					}
				}
				shouldAdd.each { fea ->
					def feature = parser.parseText("<uses-feature ${ns} android:name=\"${fea}\" />")
					manifest.appendNode(feature)
				}

				//尝试加入自定义权限（仅针对push）
				def coustomPermissions = []
				manifest.'permission'.each { permission ->
					coustomPermissions.add(permission.getProperty('@android:name').toString())
				}
				def shouldAddCoustom = []
				Set permissionsCoustom = globalVariants.customPermissions
				if (permissionsCoustom != null) {
					permissionsCoustom.each { permission ->
						if (!coustomPermissions.contains(permission)) {
							shouldAddCoustom.add(permission)
						}
					}
				}
				def nsCustom = 'xmlns:android="http://schemas.android.com/apk/res/android"'
				def level = 'android:protectionLevel="signature"'
				shouldAddCoustom.each { per ->
					String lastPermission = "<permission ${nsCustom} android:name=\"${per}\" ${level}/>"
					if (packageName != null && lastPermission.contains('${applicationId}')) {
						lastPermission = lastPermission.replace('${applicationId}', packageName)
					}
					def permission = parser.parseText(lastPermission)
					manifest.appendNode(permission)
				}

				// 尝试替换application为MobApplication
				def app = manifest.'application'[0]
				if (app == null || app.isEmpty()) {
					def appNode = """
							<application
									xmlns:android="http://schemas.android.com/apk/res/android"
									xmlns:tools="http://schemas.android.com/tools"
									tools:node="merge"
									android:name="com.mob.MobApplication"/>
							"""
					manifest.appendNode(parser.parseText(appNode))
					app = manifest.'application'[0]
				}

				def name = app.@'android:name'.toString()
				if (name == null || name.length() == 0) {
					app.@'android-name____' = "com.mob.MobApplication"
					def xml = XmlUtil.serialize(manifest)
					manifest = parser.parseText(xml.replace("android-name____", "android:name"))
					app = manifest.'application'[0]
				} else if (!"false".equalsIgnoreCase(String.valueOf(globalVariants.autoInit))) {
					String prefix = packageName == null ? '${applicationId}' : packageName
					def appNode = """
							<provider
									xmlns:android="http://schemas.android.com/apk/res/android"
									xmlns:tools="http://schemas.android.com/tools"
									android:name="com.mob.MobProvider"
									android:multiprocess="true"
									android:authorities="${prefix}.com.mob.MobProvider"
									android:exported="false"/>
							"""
					app.appendNode(parser.parseText(appNode))
				}
				// 尝试加入AppKey、AppSecret和其它metadata
				Map metaData = globalVariants.applicationMetaData
				if (metaData == null) {
					metaData = [:]
				}
				metaData.put("Mob-AppKey", globalVariants.appkey)
				metaData.put("Mob-AppSecret", globalVariants.appSecret)
				String domain = globalVariants.domain
				if (domain != null) {
					metaData.put("Domain", domain)
				}
				Object https = globalVariants.https
				if (https != null && "true".equalsIgnoreCase(String.valueOf(https))) {
					metaData.put("Mob-Https", "yes")
				}
                Object useMobPrivacy = globalVariants.useMobPrivacy
                if (useMobPrivacy != null && "true".equalsIgnoreCase(String.valueOf(useMobPrivacy))) {
                    metaData.put("Mob-PpNecessary", "true")
                }
                Object ipv6 = globalVariants.ipv6
                if (ipv6 != null && "true".equalsIgnoreCase(String.valueOf(ipv6))) {
                    metaData.put("Mob-V6", "true")
                }


				boolean found
				metaData.each { field, value ->
					if (value != null) {
						//将value中的引号和单引号去掉
						String tmpValue = value.toString().replaceAll("\"", " ").trim()
						tmpValue = tmpValue.toString().replaceAll("\'", "").trim()
						found = false
						app.'meta-data'.each { md ->
							def dataName = md.@'android:name'.toString()
							if (field.equals(dataName)) {
								found = true
								md.@'android:value' = tmpValue
							}
						}
						if (!found) {
							def data = """
									<meta-data 
											xmlns:android="http://schemas.android.com/apk/res/android"
											xmlns:tools="http://schemas.android.com/tools"
											tools:node="merge"
											android:name="${field}" android:value="${tmpValue}"/>
									"""
							app.appendNode(parser.parseText(data))
						}
					}
				}

				// 尝试自动加入MobUIShell
				found = false
				boolean hasSetMobPushDefaultOpenActivityName = globalVariants.mobPushDefaultOpenActivityName != null && globalVariants.mobPushDefaultOpenActivityName.toString().length() > 0
				boolean hasSetDouyinCallbackAct = globalVariants.douyinCallbackAct != null && globalVariants.douyinCallbackAct.toString().length() > 0
				boolean addDouyinCallbackActAtrribute = false;
				app.'activity'.each { act ->
					def actName = act.@'android:name'.toString()
					if ("com.mob.tools.MobUIShell".equals(actName)) {
						found = true
					}
					if (found && !hasSetMobPushDefaultOpenActivityName) {
						return
					}
					//为MobPush指定默认打开的activity
					if (hasSetMobPushDefaultOpenActivityName && globalVariants.mobPushDefaultOpenActivityName.equals(actName)) {
						def tmpValue =  act.attributes().get('android:exported')
						if (tmpValue == null || String.valueOf(tmpValue).trim().length() == 0) {
							act.@'android_exported____' = "true"
							addDouyinCallbackActAtrribute = true
						} else if (!"true".equalsIgnoreCase(String.valueOf(tmpValue))) {
							act.attributes().put('android:exported', 'true')
						}
						def tmp = """
								<intent-filter>
										<action 
											xmlns:android="http://schemas.android.com/apk/res/android"
											android:name="${packageName}.default.MAIN" />
										<category 
											xmlns:android="http://schemas.android.com/apk/res/android"
											android:name="android.intent.category.DEFAULT" />
								</intent-filter>
								"""
						act.appendNode(parser.parseText(tmp))
						hasSetMobPushDefaultOpenActivityName = false
						if (found && !hasSetDouyinCallbackAct) {
							return
						}
					}

					//douyinCallbackAct
					if (hasSetDouyinCallbackAct && globalVariants.douyinCallbackAct.equals(actName)) {
						act.@'android:exported' = "true"
						def tmp = """
								<intent-filter>
         							<action 
         								xmlns:android="http://schemas.android.com/apk/res/android"
         								android:name="douyin.callback.action" />
         							<category 
         								xmlns:android="http://schemas.android.com/apk/res/android"
         								android:name="android.intent.category.DEFAULT" />
 								</intent-filter>
								"""
						act.appendNode(parser.parseText(tmp))
						hasSetDouyinCallbackAct = false
						if (found && !hasSetMobPushDefaultOpenActivityName) {
							return
						}
					}
				}

				if (addDouyinCallbackActAtrribute) {
					def xml = XmlUtil. (manifest)
					manifest = parser.parseText(xml.replace("android_exported____", "android:exported"))
					app = manifest.'application'[0]
				}

				if (!found) {
					def tmp = """
							<activity
									xmlns:android="http://schemas.android.com/apk/res/android"
									xmlns:tools="http://schemas.android.com/tools"
									tools:node="merge"
									android:theme="@android:style/Theme.Translucent.NoTitleBar"
									android:name="com.mob.tools.MobUIShell"
									android:configChanges="keyboardHidden|orientation|screenSize"
									android:windowSoftInputMode="stateHidden|adjustResize"/>
							"""
					def mobUiShell = parser.parseText(tmp)
					Set intents = globalVariants.intents
					if (intents != null) {
						intents.each { i ->
							mobUiShell.appendNode(parser.parseText(i))
						}
					}
					app.appendNode(mobUiShell)
				}

				// 尝试添加QQ等平台的特殊回调Activity
				Set directToAdd = globalVariants.directToAdd
				if (directToAdd != null) {
					directToAdd.each { actToAdd ->
						if (((String)actToAdd).contains("#MobApplicationId#")) {
							actToAdd = (String)actToAdd.replace('#MobApplicationId#', '${applicationId}')
						}
						if (packageName != null && (String)actToAdd.contains('${applicationId}')) {
							actToAdd = (String)actToAdd.replace('${applicationId}', packageName)
						}
                        app.appendNode(parser.parseText(actToAdd))
					}
				}

				// 添加ShareSDK的回调Activity
				Set activitiesToAdd = globalVariants.activitiesToAdd
				if (activitiesToAdd != null) {
					activitiesToAdd.each { actToAdd ->
						found = false
						app.'activity'.each { act ->
							def actName = act.@'android:name'.toString()
							if (packageName != null && actName.startsWith(packageName)) {
								actName = actName.substring(packageName.length())
							}
							if (actName.equals(actToAdd)) {
								found = true
								return
							}
						}
						if (!found) {
							def className
							if (packageName == null) {
								className = '${applicationId}' + actToAdd
							} else {
								className = packageName + actToAdd
							}

							String superClass = null
							String[] parts = actToAdd.substring(1).split("\\.")
							if ("wxapi".equals(parts[0])) {
								if ("WXPayEntryActivity".equals(parts[1])) {
									superClass = "com.mob.paysdk.PaymentActivity"
								} else {
									superClass = "cn.sharesdk.wechat.utils.WechatHandlerActivity"
								}
							} else if ("yxapi".equals(parts[0])) {
								superClass = "cn.sharesdk.yixin.utils.YixinHandlerActivity"
							} else if ("apshare".equals(parts[0])) {
								superClass = "cn.sharesdk.alipay.utils.AlipayHandlerActivity"
							} else if ("ddshare".equals(parts[0])) {
								superClass = "cn.sharesdk.dingding.utils.DingdingHandlerActivity"
							} else if ("tiktokapi".equals(parts[0])) {
								superClass = "cn.sharesdk.tiktok.tiktokapi.TikTokHandlerActivity"
							}

							String tmpPkg = packageName == null ? '${applicationId}' : packageName
							def superAct
							if ("cn.sharesdk.wechat.utils.WechatHandlerActivity".equals(superClass)) {
								//单独处理微信
								superAct = """
									<activity
											xmlns:android="http://schemas.android.com/apk/res/android"
											xmlns:tools="http://schemas.android.com/tools"
											tools:node="merge"
											android:theme="@android:style/Theme.Translucent.NoTitleBar"
											android:name="${superClass}"
											android:configChanges="keyboardHidden|orientation|screenSize"
											android:exported="false" />
									"""
							} else {
								superAct = """
									<activity
											xmlns:android="http://schemas.android.com/apk/res/android"
											xmlns:tools="http://schemas.android.com/tools"
											tools:node="merge"
											android:name="${superClass}"
											android:configChanges="keyboardHidden|orientation|screenSize"
											android:exported="false" />
									"""
							}
							app.appendNode(parser.parseText(superAct))

							def actAlias
							if ("cn.sharesdk.wechat.utils.WechatHandlerActivity".equals(superClass)) {
								//单独处理微信
								actAlias = """
									<activity-alias
											xmlns:android="http://schemas.android.com/apk/res/android"
											xmlns:tools="http://schemas.android.com/tools"
											tools:node="merge"
											android:theme="@android:style/Theme.Translucent.NoTitleBar"
											android:name="${className}"
											android:exported="true"
											android:targetActivity="${superClass}" />
									"""
							} else {
								actAlias = """
									<activity-alias
											xmlns:android="http://schemas.android.com/apk/res/android"
											xmlns:tools="http://schemas.android.com/tools"
											tools:node="merge"
											android:name="${className}"
											android:exported="true"
											android:targetActivity="${superClass}" />
									"""
							}
							app.appendNode(parser.parseText(actAlias))
						}
					}
				}

				//闭环分享在share中添加中转activity
				if (globalVariants.loopShare) {
					def loopShareAct = """
						<activity
							xmlns:android="http://schemas.android.com/apk/res/android"
							android:name="cn.sharesdk.framework.loopshare.RestoreTempActivity"
							android:configChanges="keyboardHidden|orientation|screenSize"
							android:theme="@android:style/Theme.Translucent.NoTitleBar"
							android:windowSoftInputMode="stateHidden|adjustResize"
							android:launchMode="singleTop"/>
					"""
					app.appendNode(parser.parseText(loopShareAct))


					String loopShareFilter = """
							<intent-filter>
								<action android:name="android.intent.action.VIEW" />
								<category android:name="android.intent.category.DEFAULT"/>
								<category android:name="android.intent.category.BROWSABLE"/>
								<data android:scheme="ssdk${globalVariants.appkey}" android:host="cn.sharesdk.loop"/>
							</intent-filter>
						"""

					String mlinkActivity = """
						<activity
							xmlns:android="http://schemas.android.com/apk/res/android"
							xmlns:tools="http://schemas.android.com/tools"
							android:name="cn.sharesdk.loopshare.LoopShareActivity"
							android:launchMode="singleTask"
							android:theme="@android:style/Theme.Translucent.NoTitleBar.Fullscreen">
							${loopShareFilter}
						</activity>
					"""
					app.appendNode(parser.parseText(mlinkActivity))
				}

				// 注册MobLink的uri和appLinks
				registerMobLink(app, parser)

				// 重新输出manifest文件
				manifestFile.setText(XmlUtil.serialize(manifest), "utf-8")
			}
		}
	}

	private def registerMobLink(def app, def parser) {
		// 注册MobLink的uri和appLinks
		boolean addUri = false
		boolean addAppLink = false
		String uri = globalVariants.mobLinkUri
		if (uri != null) {
			URI u = new URI(uri)
			String scheme = u.getScheme()
			String host = u.getHost()
			addUri = !app.'activity'.any { act ->
				return act.'intent-filter'.any { filter ->
					return filter.'data'.any { data ->
						def sk = act.@'android:scheme'.toString()
						def hst = act.@'android:host'.toString()
						return (sk.equalsIgnoreCase(scheme) && hst.equalsIgnoreCase(host))
					}
				}
			}
		}
		String host = globalVariants.mobLinkAppLinkHost
		if (host != null) {
			addAppLink = !app.'activity'.any { act ->
				return act.'intent-filter'.any { filter ->
					return filter.'data'.any { data ->
						def sk = act.@'android:scheme'.toString()
						def hst = act.@'android:host'.toString()
						return ((sk.equalsIgnoreCase("http") || sk.equalsIgnoreCase("https")) && hst.equalsIgnoreCase(host))
					}
				}
			}
		}
		if (addUri || addAppLink) {
			String uriFilter = ""
			if (addUri) {
				URI u = new URI(uri)
				uriFilter = """
							<intent-filter>
								<action android:name="android.intent.action.VIEW" />
								<category android:name="android.intent.category.DEFAULT"/>
								<category android:name="android.intent.category.BROWSABLE"/>
								<data android:scheme="${u.getScheme()}" android:host="${u.getHost()}"/>
							</intent-filter>
						"""
			}
			String linkFilter = ""
			if (addAppLink) {
				String autoVerify
				if ("true".equalsIgnoreCase(String.valueOf(globalVariants.mobLinkAppLinkAutoVerify))) {
					autoVerify = "android:autoVerify=\"true\""
				} else {
					String compileSdkVersion = project.extensions.getByName("android").compileSdkVersion
					int compileLevel = Integer.parseInt(compileSdkVersion.substring("android-".length()))
					autoVerify = compileLevel >= 23 ? "android:autoVerify=\"true\"" : ""
				}
				linkFilter = """
							<intent-filter ${autoVerify}>
								<action android:name="android.intent.action.VIEW" />
								<category android:name="android.intent.category.DEFAULT"/>
								<category android:name="android.intent.category.BROWSABLE"/>
								<data android:scheme="http" android:host="${host}"/>
								<data android:scheme="https" android:host="${host}"/>
							</intent-filter>
						"""
			}
			String mobLinkActivity = """
						<activity
							xmlns:android="http://schemas.android.com/apk/res/android"
							xmlns:tools="http://schemas.android.com/tools"
							android:name="com.mob.moblink.MobLinkActivity"
							android:launchMode="singleTask"
							android:theme="@android:style/Theme.Translucent.NoTitleBar.Fullscreen">
							${uriFilter}
							${linkFilter}
						</activity>
					"""
			app.appendNode(parser.parseText(mobLinkActivity))
		}
	}

    private def getMergeAssetsTask(def output) {
        def result = null
        try {
            result = output.getMergeAssetsProvider().get()//gradle 3.3.0 +
        } catch (Throwable t) {}
        if (result == null) {
            try {
                result = output.getMergeAssets()
            } catch (Throwable t) {}
        }
        return result
    }

	private def getProcessManifestTask(def output) {
		def result = null
		try {
			result = output.processManifestProvider.get()//gradle 3.3.0 +
		} catch (Throwable t) {}
		if (result == null) {
			try {
				result = output.processManifest
			} catch (Throwable t) {}
		}
		return result
	}

    private def getManifestOutputFile(def output, boolean appModel) {
        def manifestFiles = []
        try {
            manifestFiles.add(new File(getProcessManifestTask(output).manifestOutputDirectory, "AndroidManifest.xml"))
        } catch (Throwable t) {
            try {
				def file = getProcessManifestTask(output).manifestOutputFile
				if (file.exists()) {
					manifestFiles.add(file)
				}
            } catch (Throwable tt) {//gradle 3.3.0 +
                try {
                    manifestFiles.add(new File(getProcessManifestTask(output).manifestOutputDirectory.get().getAsFile(), "AndroidManifest.xml"))
                } catch (Throwable ttt) {}
            }
        }
        try {
            manifestFiles.add(new File(getProcessManifestTask(output).instantRunManifestOutputDirectory, "AndroidManifest.xml"))
        } catch (Throwable t) {
            try {
                manifestFiles.add(getProcessManifestTask(output).instantRunManifestOutputFile)
            } catch (Throwable tt) {}
        }
        return manifestFiles
    }

	private def getBundleManifestOutputFile(def output) {
		def manifestFiles = []
		try {
			manifestFiles.add(new File(getProcessManifestTask(output).bundleManifestOutputDirectory, "AndroidManifest.xml"))
		} catch (Throwable t) {
			try {
				def file = getProcessManifestTask(output).bundleManifestOutputFile
				if (file.exists()) {
					manifestFiles.add(file)
				}
			} catch (Throwable tt) {//gradle 3.3.0 +
				try {
					manifestFiles.add(new File(getProcessManifestTask(output).bundleManifestOutputDirectory.get().getAsFile(), "AndroidManifest.xml"))
				} catch (Throwable ttt) {}
			}
		}
		try {
			manifestFiles.add(new File(getProcessManifestTask(output).instantRunBundleManifestOutputDirectory, "AndroidManifest.xml"))
		} catch (Throwable t) {
			try {
				manifestFiles.add(getProcessManifestTask(output).instantRunBundleManifestOutputFile)
			} catch (Throwable tt) {}
		}
		return manifestFiles
	}


}