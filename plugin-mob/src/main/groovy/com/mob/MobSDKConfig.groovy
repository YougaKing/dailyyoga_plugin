package com.mob


import com.mob.products.*
import org.gradle.api.Action
import org.gradle.api.Project

class MobSDKConfig extends DObject {
	Project project
	private HashSet<String> printedNoticeSet = new HashSet<>()

	void setProject(Project project) {
		hiddenMembers {
            mIpv6 = false
			mtdou = false
			mkgyy = false
			mmGuard = true//如果为false，则只删除manifest配置
			mmmGuard = true//如果为false，则彻底删除，包括jar和manifest配置
            dewu = false
			mzssq = false
			googlePlay = false
			custom = false
			cmcc = false
            cmcc_sc = false
			pcy = false
			jrxy = false //今日校园定制
			sp = false
			enableWrapper = true
            fp = false
            xhcp = false
			config = new ConfigCreator()
			MobTools = new MobToolsConfig()
			MobCommons = new MobCommonsConfig()
			SDKWrapper = new SDKWrapperConfig()
			permissions = new PermissionConfig()

			ShareSDK = new ShareSDKConfig()
			SMSSDK = new SMSSDKConfig()
			MobVerify = new MobVerifyConfig()
			SecVerify = new SecVerifyConfig()
			AWK = new AWKConfig()
			ShareREC = new ShareRECConfig()
			MobAPI = new MobAPIConfig()
			MobLink = new MobLinkConfig()
			BBSSDK = new BBSSDKConfig()
			UMSSDK = new UMSSDKConfig()
			CMSSDK = new CMSSDKConfig()
			AnalySDK = new AnalySDKConfig()
			PaySDK = new PaySDKConfig()
			MobPush = new MobPushConfig()
			ShopSDK = new ShopSDKConfig()
			MobIM = new MobIMConfig()
			MobAdSDK = new MobAdSDKConfig()
			MobGuard = new MobGuardConfig()
		}

		this.project = project
		config.project = project
		ShareSDK.configCreator = config
		MobPush.configCreator = config
		MobLink.configCreator = config
		BBSSDK.configCreator = config
		PaySDK.configCreator = config
		SecVerify.configCreator = config
		MobAdSDK.configCreator = config
		MobGuard.configCreator = config
		permissions.configCreator = config
        AWK.configCreator = config
        config.create()
		processMobCommons()
	}

	// ======================================

	def methodMissing(String name, def args) {
		if ("mkgyy".equalsIgnoreCase(name)) {
			if (args == null || args.length == 0) {
				mkgyy = false
			} else {
				//酷狗音乐
				mkgyy = "true".equalsIgnoreCase(String.valueOf(args[0]))
			}
		} else if ("mmmGuard".equalsIgnoreCase(name)) {
			if (args == null || args.length == 0) {
                mmmGuard = true
			} else {
				//mmmGuard 所有sdk都添加
                mmmGuard = "true".equalsIgnoreCase(String.valueOf(args[0]))
			}
			processMobCommons(mmGuard)
        } else if ("mmGuard".equalsIgnoreCase(name)) {
			if (args == null || args.length == 0) {
				mmGuard = true
			} else {
				//mmGuard 所有sdk都添加
				mmGuard = "true".equalsIgnoreCase(String.valueOf(args[0]))
			}
			processMobCommons(mmGuard)
		} else if ("dewu".equalsIgnoreCase(name)) {
			mmGuard = false
            mmmGuard = false
			if (args == null || args.length == 0) {
				dewu = false
			} else {
				//dewu修改一人崩溃日志
				dewu = "true".equalsIgnoreCase(String.valueOf(args[0]))
			}
			processMobCommons()
		} else if ("mtdou".equalsIgnoreCase(name)) {
			mtdou = false
			if (args == null || args.length == 0) {
				mtdou = false
			} else {
				//糖豆广告SDK修改一个崩溃日志getDeclearedMethods
				mtdou = "true".equalsIgnoreCase(String.valueOf(args[0]))
			}
			processMobCommons()
		} else if ("mzssq".equalsIgnoreCase(name)) {
            mmGuard = false
            mmmGuard = false
			if (args == null || args.length == 0) {
				mzssq = false
			} else {
				//mzssq为追书神器定制版本
				mzssq = "true".equalsIgnoreCase(String.valueOf(args[0]))
			}
			processMobCommons()
		} else if ("googlePlay".equalsIgnoreCase(name)) {
            mmGuard = false
            mmmGuard = false
            if (args == null) {
				googlePlay = false
			} else if (args.length == 0) {
				googlePlay = false
			} else {
				//使googlePlay无效，应为googlePlay版本和正式版本没什么区别，后续如果有特殊处理，再打开
//				googlePlay = "true".equalsIgnoreCase(String.valueOf(args[0]))
				googlePlay = false
			}
			processMobCommons()
		} else if ("custom".equalsIgnoreCase(name)) {
            mmGuard = false
            mmmGuard = false
            if (args == null || args.length == 0) {
				custom = false
			} else {
				//TODO custom目前YY约战在使用，去掉了目录日活统计功能，后续修复后，再关掉custom
				custom = "true".equalsIgnoreCase(String.valueOf(args[0]))
			}
			processMobCommons()
		} else if ("cmcc".equalsIgnoreCase(name)) {
            mmGuard = false
            mmmGuard = false
            if (args == null || args.length == 0) {
				cmcc = false
			} else {
				//TODO cmcc由于梆梆安全检测，去掉的统计项：计步器、加速度、地磁、重力、陀螺仪、sdcard活跃、运行服务列表、蓝牙、蓝牙列表
				//并且sdcard缓存都改成了沙箱缓存，除了如下项：应用列表、系统应用列表、duid\imei\mac\serialno（.mn_文件）
				cmcc = "true".equalsIgnoreCase(String.valueOf(args[0]))
			}
			processMobCommons()
		} else if ("cmcc_sc".equalsIgnoreCase(name)) {
            mmGuard = false
            mmmGuard = false
            if (args == null || args.length == 0) {
				cmcc_sc = false
			} else {
				//四川移动需要使用隐私版本，且隐私开关是默认是开的，线上默认是关的，所以需要重新打个包
				cmcc_sc = "true".equalsIgnoreCase(String.valueOf(args[0]))
			}
			processMobCommons()
		} else if ("pcy".equalsIgnoreCase(name)) {
            mmGuard = false
            mmmGuard = false
            if (args == null || args.length == 0) {
				pcy = false
			} else {
				//澎湃新闻默认走隐私版本
				pcy = "true".equalsIgnoreCase(String.valueOf(args[0]))
			}
			processMobCommons()
		} else if ("jrxy".equalsIgnoreCase(name)) {
            mmGuard = false
            mmmGuard = false
            if (args == null || args.length == 0) {
				jrxy = false
			} else {
				//澎湃新闻默认走隐私版本
				jrxy = "true".equalsIgnoreCase(String.valueOf(args[0]))
			}
			processMobCommons()
		} else if ("sp".equalsIgnoreCase(name)) {//给广告用的，快看漫画
            mmGuard = false
            mmmGuard = false
            if (args == null || args.length == 0) {
				sp = false
			} else {
				sp = "true".equalsIgnoreCase(String.valueOf(args[0]))
			}
			processMobCommons()
		} else if ("appKey".equalsIgnoreCase(name)) {
			this.appKey(args[0])
		} else if ("appSecret".equalsIgnoreCase(name)) {
			this.appSecret(args[0])
		} else if ("wrapper".equalsIgnoreCase(name)) {
			if (args == null) {
				enableWrapper = true
			} else if (args.length == 0) {
				enableWrapper = true
			} else {
				enableWrapper = "true".equalsIgnoreCase(String.valueOf(args[0]))
			}
		} else if ("autoConfig".equalsIgnoreCase(name)) {
			if (args == null) {
				config.autoConfig = true
			} else if (args.length == 0) {
				config.autoConfig = true
			} else {
				config.autoConfig = "true".equalsIgnoreCase(String.valueOf(args[0]))
			}
		} else if ("autoInit".equalsIgnoreCase(name)) {//是否自动初始化，true的话去掉MobProvider
			if (args == null || args.length == 0) {
				config.autoInit = true
			} else {
				config.autoInit = "true".equalsIgnoreCase(String.valueOf(args[0]))
			}
		} else if ("https".equalsIgnoreCase(name)) {
			config.https = args[0]
		} else if ("domain".equalsIgnoreCase(name) || "defaultDomain".equalsIgnoreCase(name)) {
			config.domain = args[0]
		}  else if ("mIpv6".equalsIgnoreCase(name)) {
            if (args == null || args.length == 0) {
                mIpv6 = false
            } else {
                mIpv6 = "true".equalsIgnoreCase(String.valueOf(args[0]))
            }
            this.ipv6(mIpv6)
        } else if ("fp".equalsIgnoreCase(name)) {
            mmGuard = false
            mmmGuard = false
            if (args == null || args.length == 0) {
                fp = false
            } else {
                //客户端强制隐私版本
                fp = "true".equalsIgnoreCase(String.valueOf(args[0]))
            }
            processMobCommons()
        } else if ("xhcp".equalsIgnoreCase(name)) {
            mmGuard = false
            mmmGuard = false
            if (args == null || args.length == 0) {
                xhcp = false
            } else {
                //客户端强制隐私版本
                xhcp = "true".equalsIgnoreCase(String.valueOf(args[0]))
            }
            processMobCommons()
        } else if ("updateCacheDynamicVersionsFor".equalsIgnoreCase(name)) {
			try {
				int seconds = Integer.parseInt(String.valueOf(args[0]))
				config.updateCacheDynamicVersionsFor = seconds
			} catch (Throwable t) {}
		} else if ("Permissions".equalsIgnoreCase(name)) {
			permissions.closure = args[0]
		} else if ("MobTools".equalsIgnoreCase(name) || "tools".equalsIgnoreCase(name)) {
			this.MobTools.closure = args[0]
			processMobTools()
		} else if ("MobGUI".equalsIgnoreCase(name) || "gui".equalsIgnoreCase(name)) {
			if (args == null) {
				processMobTools()
				addDependencies('com.mob:MobGUI:' + MobTools.version)
			} else if (args.length == 0) {
				processMobTools()
				addDependencies('com.mob:MobGUI:' + MobTools.version)
			} else if ("true".equals(String.valueOf(args[0]).toLowerCase())) {
				processMobTools()
				addDependencies('com.mob:MobGUI:' + MobTools.version)
			}
		} else if ("MobCommons".equalsIgnoreCase(name) || "commons".equalsIgnoreCase(name) || "comm".equalsIgnoreCase(name)) {
			this.MobCommons.closure = args[0]
			processMobCommons()
		} else if ("SDKWrapper".equalsIgnoreCase(name)) {
			this.SDKWrapper.closure = args[0]
			processSDKWrapper()
		} else if ("CloudStorage".equalsIgnoreCase(name)) {
			CoreProductConfig config = new CoreProductConfig()
			config.closure = args[0]
			if (config.version == null) {
				config.version = '+'
			}
			processMobCommons()
			addDependencies('com.mob:CloudStorage:' + config.version)
		} else if ("java8".equalsIgnoreCase(name) || "java8Support".equalsIgnoreCase(name)) {
			java8Support(args[0])
		} else if ("ShareSDK".equalsIgnoreCase(name) || "share".equalsIgnoreCase(name) || "login".equalsIgnoreCase(name)) {
			processShareSDK(args[0])
		} else if ("SMSSDK".equalsIgnoreCase(name) || "sms".equalsIgnoreCase(name)) {
			processSMSSDK(args[0])
		} else if ("MobVerify".equalsIgnoreCase(name) || "verify".equalsIgnoreCase(name)) {
			processMobVerify(args[0])
		} else if ("SecVerify".equalsIgnoreCase(name)) {
			processSecVerify(args[0])
		} else if ("AWK".equalsIgnoreCase(name) || "awaker".equalsIgnoreCase(name)) {
			processAWK(args[0])
		} else if ("ShareREC".equalsIgnoreCase(name) || "rec".equalsIgnoreCase(name)) {
			processShareREC(args[0])
		} else if ("MobAPI".equalsIgnoreCase(name) || "api".equalsIgnoreCase(name)) {
			processMobAPI(args[0])
		} else if ("MobLink".equalsIgnoreCase(name) || "link".equalsIgnoreCase(name)) {
			processMobLink(args[0])
		} else if ("BBSSDK".equalsIgnoreCase(name) || "bbs".equalsIgnoreCase(name)) {
			processBBSSDK(args[0])
		} else if ("UMSSDK".equalsIgnoreCase(name) || "ums".equalsIgnoreCase(name) || "user".equalsIgnoreCase(name)) {
			processUMSSDK(args[0])
		} else if ("CMSSDK".equalsIgnoreCase(name) || "cms".equalsIgnoreCase(name) || "news".equalsIgnoreCase(name)) {
			processCMSSDK(args[0])
		} else if ("AnalySDK".equalsIgnoreCase(name) || "analy".equalsIgnoreCase(name)) {
			processAnalySDK(args[0])
		} else if ("PaySDK".equalsIgnoreCase(name) || "pay".equalsIgnoreCase(name) || "MobPay".equalsIgnoreCase(name)) {
			processPaySDK(args[0])
		} else if ("MobPush".equalsIgnoreCase(name) || "push".equalsIgnoreCase(name)) {
			processMobPush(args[0])
		} else if ("ShopSDK".equalsIgnoreCase(name) || "shop".equalsIgnoreCase(name)) {
			processShopSDK(args[0])
		} else if ("MobIM".equalsIgnoreCase(name) || "im".equalsIgnoreCase(name) || "chat".equalsIgnoreCase(name)) {
			processMobIM(args[0])
		} else if("MobAdSDK".equalsIgnoreCase(name) || "MobAd".equalsIgnoreCase(name) || "Ad".equalsIgnoreCase(name)){
			processMobAdSDK(args[0])
		} else if("MobGuardSDK".equalsIgnoreCase(name) || "MobGuard".equalsIgnoreCase(name)){
			processMobGuardSDK(args[0])
		} else if ("GrowSolution".equalsIgnoreCase(name) || "grow".equalsIgnoreCase(name)) {
			MobSDKSolution solution = project.extensions.getByName("MobSDKSolution")
			solution.hmGrowSolution(args[0])
		}
		return null
	}

	void appKey(String appkey) {
		config.appkey = appkey
	}

    void appBundleMode(boolean appBundleMode) {
		config.appBundleMode = appBundleMode
	}

	void delMobTmpDir(boolean delMobTmpDir) {
		config.delMobTmpDir = delMobTmpDir
	}

    void useMobPrivacy(boolean useMobPrivacy) {
        config.useMobPrivacy = useMobPrivacy
    }

    void ipv6(boolean ipv6) {
        config.ipv6 = ipv6
    }

	void appSecret(String appSecret) {
		config.appSecret = appSecret
	}

	void java8Support(boolean enable) {
		if (enable) {
			processMobTools()
			addDependencies('com.mob:Java8Suport:' + MobTools.version)
		}
	}

	protected void addDependencies(String lib) {
		addDependencies(lib, false)
	}

	protected void addDependencies(String lib, boolean api) {
		String version = config.gradlePluginVersion
		if (version == null || version.startsWith("3.")) {
			project.dependencies {
				if (api) {
					add('api', lib)
				} else {
					add('implementation', lib, {
						transitive = true
					})
				}
			}
		} else {
			project.dependencies {
				add('compile', lib, {
					transitive = true
				})
			}
		}
	}

	protected def getConfigCreator() {
		return config
	}

	private void addPermission(Set permissions) {
		config.permissions.addAll(permissions)
	}

	private void processMobTools() {
		if (MobTools.version == null) {
			MobTools.version = "+"
		}
		if (custom) {
			project.configurations.all {
				exclude group: 'com.mob', module: 'MobTools'
			}
			addDependencies('com.mob:MobToolsC:' + MobTools.version)
		} else if (googlePlay) {
			project.configurations.all {
				exclude group: 'com.mob', module: 'MobTools'
			}
			addDependencies('com.mob:MobToolsGP:' + MobTools.version)
		} else if (cmcc) {
			project.configurations.all {
				exclude group: 'com.mob', module: 'MobTools'
			}
			addDependencies('com.mob:MobToolsCM:' + MobTools.version)
		} else if (cmcc_sc) {
			project.configurations.all {
				exclude group: 'com.mob', module: 'MobTools'
			}
			addDependencies('com.mob:MobToolsCMSC:' + MobTools.version)
		} else if (pcy || jrxy) { //今日校园与pcy的公共库相同
			project.configurations.all {
				exclude group: 'com.mob', module: 'MobTools'
			}
			addDependencies('com.mob:MobToolsPCY:' + MobTools.version)
		} else if (sp) {
			project.configurations.all {
				exclude group: 'com.mob', module: 'MobTools'
			}
			addDependencies('com.mob:MobToolsSP:' + MobTools.version)
		} else if (dewu) {
			project.configurations.all {
				exclude group: 'com.mob', module: 'MobTools'
			}
			addDependencies('com.mob:MobToolsDW:' + MobTools.version)
		} else if (mtdou) {
			project.configurations.all {
				exclude group: 'com.mob', module: 'MobTools'
			}
			addDependencies('com.mob:MobToolsTDOU:' + MobTools.version)
        } else if (fp) {
            project.configurations.all {
                exclude group: 'com.mob', module: 'MobTools'
            }
            addDependencies('com.mob:MobToolsFP:' + MobTools.version)
        } else if (xhcp) {
            project.configurations.all {
                exclude group: 'com.mob', module: 'MobTools'
            }
            addDependencies('com.mob:MobToolsXHCP:' + MobTools.version)
        } else {
            addDependencies('com.mob:MobTools:' + MobTools.version)
        }
		addPermission(MobTools.getPermission())
	}

	private void processMobCommons() {
		processMobTools()
		if (MobCommons.version == null) {
			MobCommons.version = "+"
		}
		if (custom) {
			println("====== custom edition ======")
			project.configurations.all {
				exclude group: 'com.mob', module: 'MobCommons'
			}
			addDependencies('com.mob:MobCommonsC:' + MobCommons.version)
		} else if (googlePlay) {
			println("====== googlePlay edition ======")
			project.configurations.all {
				exclude group: 'com.mob', module: 'MobCommons'
			}
			addDependencies('com.mob:MobCommonsGP:' + MobCommons.version)
        } else if (cmcc) {
			println("====== cmcc edition ======")
			project.configurations.all {
				exclude group: 'com.mob', module: 'MobCommons'
			}
			addDependencies('com.mob:MobCommonsCM:' + MobCommons.version)
		} else if (cmcc_sc) {
			println("====== cmcc_sc edition ======")
			project.configurations.all {
				exclude group: 'com.mob', module: 'MobCommons'
			}
			addDependencies('com.mob:MobCommonsCMSC:' + MobCommons.version)
		} else if (pcy || jrxy) {
			println("====== pcy edition ======")
			project.configurations.all {
				exclude group: 'com.mob', module: 'MobCommons'
			}
			addDependencies('com.mob:MobCommonsPCY:' + MobCommons.version)
		} else if (sp) {
			println("====== sp edition ======")
			project.configurations.all {
				exclude group: 'com.mob', module: 'MobCommons'
			}
			addDependencies('com.mob:MobCommonsSP:' + MobCommons.version)
		} else if (dewu) {
			println("====== dewu edition ======")
			project.configurations.all {
				exclude group: 'com.mob', module: 'MobCommons'
			}
			addDependencies('com.mob:MobCommonsDW:' + MobCommons.version)
		} else if (mtdou) {
			println("====== tdou edition ======")
			project.configurations.all {
				exclude group: 'com.mob', module: 'MobCommons'
			}
			addDependencies('com.mob:MobCommonsTDOU:' + MobCommons.version)
        } else if (fp) {
            println("====== fp edition ======")
            project.configurations.all {
                exclude group: 'com.mob', module: 'MobCommons'
            }
            addDependencies('com.mob:MobCommonsFP:' + MobCommons.version)
        } else if (xhcp) {
            println("====== xhcp edition ======")
            project.configurations.all {
                exclude group: 'com.mob', module: 'MobCommons'
            }
            addDependencies('com.mob:MobCommonsXHCP:' + MobCommons.version)
        } else {
            addDependencies('com.mob:MobCommons:' + MobCommons.version)
        }
		addPermission(MobCommons.getPermission())
	}

	private void processSDKWrapper() {
		processMobCommons()
		addDependencies('com.mob:MobGUI:' + MobTools.version)
		if (enableWrapper) {
			if (SDKWrapper.version == null) {
				SDKWrapper.version = "+"
			}
			addDependencies('com.mob:SDKWrapper:' + SDKWrapper.version)
			addPermission(SDKWrapper.getPermission())
		}
	}

	private void execConfig(Object arg, MobProductConfig config) {
		if (arg instanceof Action) {
			arg.execute(config)
		} else {
			config.closure = arg
		}
		processSDKWrapper()
		if (config.version == null) {
			config.version = "+"
		}
		addPermission(config.permission)
	}

	void ShareSDK(Action<ShareSDKConfig> config) {
		processShareSDK(config)
	}

	private void processShareSDK(Object arg) {
		execConfig(arg, ShareSDK)
		def v = ShareSDK.version
		printPolicyToast(v, 371)

		if (mkgyy) {
			addDependencies('cn.sharesdk:ShareSDKKGYY:+@aar')
		} else {
			addDependencies('cn.sharesdk:ShareSDK:' + v + '@aar')
		}
		config.sharesdkInfoMap.each { platform, info ->
			if (info != null) {
				DevInfo.jarNames.get(platform).each { jar ->
					if ("FourSquare".equals(jar)) {
						jar = "Foursquare"
					} else if ("LinkedIn".equals(jar)) {
						jar = "Linkedin"
					}
					String version = config.gradlePluginVersion
					if (version == null || version.startsWith("3.")) {
						project.dependencies {
							add('implementation', "cn.sharesdk:ShareSDK-${jar}:${v}")
						}
					} else {
						project.dependencies {
							add('compile', "cn.sharesdk:ShareSDK-${jar}:${v}")
						}
					}
                    if ("QQ".equals(jar) || "QZone".equals(jar)) {
                        boolean qqUpdate = false
                        try {
                            qqUpdate = v != null && !v.trim().equals("+") &&
									Integer.parseInt(v.trim().replace(".", "")) > 363 &&
									Integer.parseInt(v.trim().replace(".", "")) < 374
                        } catch (Throwable t) {}
                        if (qqUpdate) {//3.7.4开始不需要此包
                            addDependencies("cn.sharesdk:ShareSDK-QQ-Core:${v}")
                        }
                    } else if ("Facebook".equals(jar) && config.facebookOfficialVersion != null) {
						String tmpVersion = config.facebookOfficialVersion.trim()
						if (tmpVersion.equals("default")) {
							tmpVersion = "[5,6)"
						}
						addDependencies("com.facebook.android:facebook-core:${tmpVersion}", true)
						addDependencies("com.facebook.android:facebook-login:${tmpVersion}", true)
						addDependencies("com.facebook.android:facebook-share:${tmpVersion}", true)
					} else if ("GooglePlus".equals(jar) && config.googlePlusOfficialVersion != null) {
						String tmpVersion = config.googlePlusOfficialVersion.trim()
						if (tmpVersion.equals("default")) {
							tmpVersion = "17.0.0"
						}
						addDependencies("com.google.android.gms:play-services-auth:${tmpVersion}", false)
					} else if ("KakaoTalk".equals(jar)) {
						boolean oldKakaoVersion = false
						try {
							oldKakaoVersion = v != null && !v.trim().equals("+") &&
									Integer.parseInt(v.trim().replace(".", "")) < 375
						} catch (Throwable t) {}
						if (!oldKakaoVersion) {//3.7.5开始需要如下包
							addDependencies("cn.sharesdk:ShareSDK-KakaoTalkCommon:${v}")
							addDependencies("cn.sharesdk:ShareSDK-KakaoTalkLink:${v}")
							addDependencies("cn.sharesdk:ShareSDK-KakaoTalkMessage:${v}")
							addDependencies("cn.sharesdk:ShareSDK-KakaoTalkNetwork:${v}")
							addDependencies("cn.sharesdk:ShareSDK-KakaoTalkUntil:${v}")
						}
					}
				}
			}
		}
		if (ShareSDK.gui) {
			addDependencies('cn.sharesdk:OneKeyShare:' + v + '@aar')
		}
		if (ShareSDK.loopShare) {
			if (jrxy) {
				addDependencies('cn.sharesdk:ShareSDK-LoopShare-JRXY:+')
			} else {
//				processMobLink(arg)//arg中是sharesdk的版本号等属性与mobLink不一致
				addDependencies('cn.sharesdk:ShareSDK-LoopShare:' + v)
			}
		}
        processMobGuardSDK()
    }

	void SMSSDK(Action<SMSSDKConfig> config) {
		processSMSSDK(config)
	}

	private void processSMSSDK(Object arg) {
		execConfig(arg, SMSSDK)
		printPolicyToast(SMSSDK.version, 340)
		addDependencies('cn.smssdk:SMSSDK:' + SMSSDK.version + '@aar')
		if (SMSSDK.gui) {
			addDependencies('cn.smssdk:ShortMessageSDKGUI:' + SMSSDK.version + '@aar')
		}
        processMobGuardSDK()
    }

	void MobVerify(Action<MobVerifyConfig> config) {
		processMobVerify(config)
	}

	private void processMobVerify(Object arg) {
		execConfig(arg, MobVerify)
		addDependencies('com.mob:MobVerify:' + MobVerify.version + '@aar')
		if (!MobVerify.version.startsWith("1.0")) {
			addDependencies('com.mob.verify.dependency:telecom:+@aar')
		}
		if (MobVerify.gui) {
			addDependencies('com.mob:MobVerifyGUI:' + MobVerify.version + '@aar')
		}

		//自动添加短信依赖，不需要用户再自行配置
		if (SMSSDK.version == null) {
			SMSSDK.version = "+"
		}
		addDependencies('cn.smssdk:SMSSDK:' + SMSSDK.version + '@aar')
		addPermission(SMSSDK.getPermission())
        processMobGuardSDK()
    }

	void SecVerify(Action<SecVerifyConfig> config) {
		processSecVerify(config)
	}

	private void processSecVerify(Object arg) {
		execConfig(arg, SecVerify)
		if (mzssq) {
			//追书神器定制版本
			addDependencies('com.mob:SecVerifyZSSQ:' + SecVerify.version + '@aar')
			addDependencies('com.mob.sec.plugins:SecPluginsZSSQ-Ctcc:' + SecVerify.version + '@aar')
			addDependencies('com.mob.sec.plugins:SecPluginsZSSQ-Cucc:' + SecVerify.version + '@aar')
			addDependencies('com.mob.sec.plugins:SecPluginsZSSQ-Cmcc:' + SecVerify.version + '@aar')
			SecVerify.addActivityDependencies(SecVerify.version)
			return
		}
		printPolicyToast(SecVerify.version, 208)
		addDependencies('com.mob:SecVerify:' + SecVerify.version + '@aar')
		if (SecVerify.version.startsWith("2.0.0") || SecVerify.version.equals("2.0.1") || SecVerify.version.startsWith("1.0")) {
			//处理老版本的秒验，对电信、联通、移动版本的兼容
			addDependencies('com.mob.sec.dependency:ctcc:1.5.1@aar')
			if (SecVerify.version.startsWith("1.0")) {
				addDependencies('com.mob.sec.dependency:cucc:6.6@aar')
			} else {
				addDependencies('com.mob.sec.dependency:cucc:6.7@aar')
			}
			if (SecVerify.version.equals("1.0.0")) {
				addDependencies('com.mob.sec.dependency:cmcc:5.6.5@aar')
			} else {
				addDependencies('com.mob.sec.dependency:cmcc:5.6.5.1@aar')
			}
		} else if(SecVerify.version.startsWith("2.0.2") || SecVerify.version.startsWith("2.0.3")
				|| SecVerify.version.startsWith("2.0.4") || SecVerify.version.startsWith("2.0.5")){
			addDependencies('com.mob.sec.dependency:ctcc:+@aar')
			addDependencies('com.mob.sec.dependency:cucc:+@aar')
			addDependencies('com.mob.sec.dependency:cmcc:+@aar')
		} else {
			addDependencies('com.mob.sec.plugins:SecPlugins-Ctcc:' + SecVerify.version + '@aar')
			addDependencies('com.mob.sec.plugins:SecPlugins-Cucc:' + SecVerify.version + '@aar')
			addDependencies('com.mob.sec.plugins:SecPlugins-Cmcc:' + SecVerify.version + '@aar');
		}
		SecVerify.addActivityDependencies(SecVerify.version)
        processMobGuardSDK()
    }

	void AWK(Action<AWKConfig> config) {
		processAWK(config)
	}

	private void processAWK(Object arg) {
		execConfig(arg, AWK)
		if (AWK.version == null) {
			AWK.version = "+"
		}
		addDependencies('com.awstar:AWK:' + AWK.version)
		addDependencies('com.mob:AdPush:+@aar')
		MobPush.addMobService()
        processMobGuardSDK()
    }

	void ShareREC(Action<ShareRECConfig> config) {
		processShareREC(config)
	}

	private void processShareREC(Object arg) {
		printNotice("ShareREC")
		execConfig(arg, ShareREC)
		addDependencies('cn.sharerec:ShareREC-Core:' + ShareREC.version + '@aar')
		if (ShareREC.gui) {
			if (ShareREC.community) {
				addDependencies('cn.sharerec:ShareREC-VideoEditor:' + ShareREC.version + '@aar')
				addDependencies('cn.sharerec:ShareREC-VideoUploader:' + ShareREC.version + '@aar')
				addDependencies('cn.sharerec:ShareREC:' + ShareREC.version + '@aar')
			} else if (ShareREC.uploader) {
				addDependencies('cn.sharerec:ShareREC-VideoEditor:' + ShareREC.version + '@aar')
				addDependencies('cn.sharerec:ShareREC-VideoUploader:' + ShareREC.version + '@aar')
			} else if (ShareREC.editor) {
				addDependencies('cn.sharerec:ShareREC-VideoEditor:' + ShareREC.version + '@aar')
			}
		}
	}

	void MobAPI(Action<MobAPIConfig> config) {
		processMobAPI(config)
	}

	private void processMobAPI(Object arg) {
		printNotice("MobAPI")
		execConfig(arg, MobAPI)
		addDependencies('com.mob:mobAPI:' + MobAPI.version + '@aar')
	}

	void MobLink(Action<MobLinkConfig> config) {
		processMobLink(config)
	}

	private void processMobLink(Object arg) {
		execConfig(arg, MobLink)
		MobLink.addHosts()
		if (MobLink.preferClassicalEdition) {
			addDependencies('com.mob:MobLink-Classical:' + MobLink.version + '@aar')
		} else {
			addDependencies('com.mob:MobLink:' + MobLink.version + '@aar')
		}
        processMobGuardSDK()
    }

	void BBSSDK(Action<BBSSDKConfig> config) {
		processBBSSDK(config)
	}

	private void processBBSSDK(Object arg) {
		printNotice("BBSSDK")
		execConfig(arg, BBSSDK)
		boolean add3rdLibs = false
		boolean addNewsPackage = false
		int code = 0
		String version = config.gradlePluginVersion
		addDependencies('com.mob:BBSSDK:' + BBSSDK.version + '@aar')
		if (!"+".equals(BBSSDK.version)) {
			String[] parts = BBSSDK.version.split("\\.")
			for (int i = 0; i < 3; i++) {
				code = code * 100 + Integer.parseInt(parts[i])
			}
			if (code < 20200) {
				add3rdLibs = true
			} else if (code < 20401) {
				addNewsPackage = true
			}
		}
		if (BBSSDK.gui) {
			if (add3rdLibs) {
				project.repositories.jcenter()
				if (version == null || version.startsWith("3.")) {
					project.dependencies {
						add('implementation', 'org.apache.commons:commons-csv:1.4')
					}
					project.dependencies {
						add('implementation', 'com.android.support:support-v4:23.2.0')
					}
					project.dependencies {
						add('implementation', 'jp.wasabeef:glide-transformations:2.0.2')
					}
					project.dependencies {
						add('implementation', 'com.github.bumptech.glide:glide:3.8.0')
					}
					project.dependencies {
						add('implementation', 'pl.droidsonroids.gif:android-gif-drawable:1.2.8')
					}
				} else {
					project.dependencies {
						add('compile', 'org.apache.commons:commons-csv:1.4')
					}
					project.dependencies {
						add('compile', 'com.android.support:support-v4:23.2.0')
					}
					project.dependencies {
						add('compile', 'jp.wasabeef:glide-transformations:2.0.2')
					}
					project.dependencies {
						add('compile', 'com.github.bumptech.glide:glide:3.8.0')
					}
					project.dependencies {
						add('compile', 'pl.droidsonroids.gif:android-gif-drawable:1.2.8')
					}
				}
				addDependencies('com.mob:BBSSDKGUI:' + BBSSDK.version + '@aar')
				addDependencies('com.mob:Jimu:+@aar')
				if (BBSSDK.theme != null) {
					if ("theme0".equals(BBSSDK.theme)) {
						addDependencies('com.mob:BBSSDKTHEME0:' + BBSSDK.version + '@aar')
					} else if ("theme1".equals(BBSSDK.theme)) {
						if (version == null || version.startsWith("3.")) {
							project.dependencies {
								add('implementation', 'jp.wasabeef:blurry:2.1.1')
							}
							project.dependencies {
								add('implementation', 'com.android.support:appcompat-v7:23.2.0')
							}
						} else {
							project.dependencies {
								add('compile', 'jp.wasabeef:blurry:2.1.1')
							}
							project.dependencies {
								add('compile', 'com.android.support:appcompat-v7:23.2.0')
							}
						}
						addDependencies('com.mob:BBSSDKTHEME0:' + BBSSDK.version + '@aar')
						addDependencies('com.mob:BBSSDKTHEME1:' + BBSSDK.version + '@aar')
					}
				}
			} else {
				addDependencies('com.mob:BBSSDKGUI:' + BBSSDK.version + '@aar')
				if (BBSSDK.theme == null) {
					if (addNewsPackage) {
						if ("theme0".equals(BBSSDK.newsTheme)) {
							addDependencies('com.mob:BBSSDKTHEME0NEWS:' + BBSSDK.version + '@aar')
						} else if ("theme1".equals(BBSSDK.newsTheme)) {
							addDependencies('com.mob:BBSSDKTHEME1NEWS:' + BBSSDK.version + '@aar')
						}
					}
				} else if ("theme0".equals(BBSSDK.theme)) {
					if (addNewsPackage) {
						addDependencies('com.mob:BBSSDKTHEME0NEWS:' + BBSSDK.version + '@aar')
						if ("theme1".equals(BBSSDK.newsTheme)) {
							addDependencies('com.mob:BBSSDKTHEME1NEWS:' + BBSSDK.version + '@aar')
						}
					}
					addDependencies('com.mob:BBSSDKTHEME0:' + BBSSDK.version + '@aar')
				} else if ("theme1".equals(BBSSDK.theme)) {
					if (addNewsPackage) {
						addDependencies('com.mob:BBSSDKTHEME0NEWS:' + BBSSDK.version + '@aar')
						addDependencies('com.mob:BBSSDKTHEME1NEWS:' + BBSSDK.version + '@aar')
					}
					addDependencies('com.mob:BBSSDKTHEME0:' + BBSSDK.version + '@aar')
					addDependencies('com.mob:BBSSDKTHEME1:' + BBSSDK.version + '@aar')
				}
			}
		}
	}

	void UMSSDK(Action<UMSSDKConfig> config) {
		processUMSSDK(config)
	}

	private void processUMSSDK(Object arg) {
		printNotice("UMSSDK")
		execConfig(arg, UMSSDK)
		addDependencies('com.mob:Jimu:+@aar')
		addDependencies('com.mob:UMSSDK:' + UMSSDK.version + '@aar')
		if (UMSSDK.gui) {
			addDependencies('com.mob:UMSSDKGUI:' + UMSSDK.version + '@aar')
		}
	}

	void CMSSDK(Action<CMSSDKConfig> config) {
		processCMSSDK(config)
	}

	private void processCMSSDK(Object arg) {
		printNotice("CMSSDK")
		execConfig(arg, CMSSDK)
		addDependencies('com.mob:Jimu:+@aar')
		addDependencies('com.mob:CMSSDK:' + CMSSDK.version + '@aar')
		if (CMSSDK.gui) {
			addDependencies('com.mob:CMSSDKGUI:' + CMSSDK.version + '@aar')
		}
	}

	void AnalySDK(Action<AnalySDKConfig> config) {
		processAnalySDK(config)
	}

	private void processAnalySDK(Object arg) {
		printNotice("AnalySDK")
		execConfig(arg, AnalySDK)
		addDependencies('com.mob:AnalySDK:' + AnalySDK.version + '@aar')
	}

	void PaySDK(Action<PaySDKConfig> config) {
		processPaySDK(config)
	}

	private void processPaySDK(Object arg) {
		printNotice("PaySDK")
		execConfig(arg, PaySDK)
		addDependencies('com.mob:PaySDK:' + PaySDK.version + '@aar')
	}

	void MobPush(Action<MobPushConfig> config) {
		processMobPush(config)
	}

	private void processMobPush(Object arg) {
		execConfig(arg, MobPush)
		if (MobPush.adPush) {//添加ad
			addDependencies('com.mob:AdPush:+@aar')
		} else {
			addDependencies('com.mob:MobPush:' + MobPush.version + '@aar')
		}
		if (MobPush.awk) {//添加唤醒到mobpush中
			addDependencies('com.awstar:AWK:+')
		}
		try {
			if (MobPush.version == null || MobPush.version.trim().equals("+")
					|| Integer.parseInt(MobPush.version.trim().replace(".", "")) >= 320) {
				addDependencies('com.mob:MobConnect:' + MobPush.version + '@aar')
				MobPush.addTCPService()
			}
		} catch (Throwable t) {}

		MobPush.addMobService()
		if (MobPush.version.startsWith("1.")) {
			printOldPushVersionNotice()
		}
        processMobGuardSDK()
    }

	void ShopSDK(Action<ShopSDKConfig> config) {
		processShopSDK(config)
	}

	private void processShopSDK(Object arg) {
		printNotice("ShopSDK")
		execConfig(arg, ShopSDK)
		addDependencies('com.mob:ShopSDK:' + ShopSDK.version + '@aar')
		if (ShopSDK.gui) {
			addDependencies('com.mob:ShopGUI:' + ShopSDK.version + '@aar')
		}
	}

	void MobIM(Action<MobIMConfig> config) {
		processMobIM(config)
	}

	private void processMobIM(Object arg) {
		printNotice("MobIM")
		execConfig(arg, MobIM)
		addDependencies('com.mob:MobIM:' + MobIM.version + '@aar')
	}

	void MobAdSDK(Action<MobAdSDKConfig> config) {
		processMobAdSDK(config)
	}

	private void processMobAdSDK(Object arg) {
		MobAdSDK.addManifestConfig()
		execConfig(arg, MobAdSDK)
		addDependencies('com.mob:MobAdSDK:' + MobAdSDK.version + '@aar')
		if (!MobAdSDK.version.startsWith("2.0") || MobAdSDK.version.startsWith("+")) {
			addDependencies('com.mob.ad.plugins.channel:Channel-Four:' + MobAdSDK.version)
			addDependencies('com.mob.ad.plugins.channel:Channel-Five:' + MobAdSDK.version)
			addDependencies('com.mob.ad.plugins.channel:Channel-Six:' + MobAdSDK.version)
			addDependencies('com.mob.ad.plugins.channel:Channel-Thirteen:' + MobAdSDK.version)
		}

		try {
			if (MobAdSDK.version != null && (MobAdSDK.version.trim().equals("+") ||
					Integer.parseInt(MobAdSDK.version.trim().replace(".", "")) >= 215)) {
				addDependencies('com.mob.ad.plugins.channel:Channel-TwentyTwo:' + MobAdSDK.version)
			}
		} catch (Throwable t) {}
	}

	private void processMobGuardSDK(Object arg) {
		execConfig(arg, MobGuard)
		addDependencies('com.mob:MobGuard:' + MobGuard.version + '@aar')
		try {
			if (MobGuard.version == null || MobGuard.version.trim().equals("+")
					|| Integer.parseInt(MobGuard.version.trim().replace(".", "")) >= 106) {
				MobGuard.addNewService()
			} else {
				MobGuard.addMobGuardService()
			}
		} catch (Throwable t) {}
	}

    private void processMobGuardSDK() {
        if (mmmGuard && !mzssq) {
            //给全局添加看护SDK，排除定制版本
            addDependencies('com.mob:MobGuard:+@aar')
            if (mmGuard) { //当mmGuard为false时，不添加配置
				MobGuard.addNewService()
            }
        }
    }

	private void printNotice(String sdkName) {
		if (printedNoticeSet.contains(sdkName)) {
			return
		}
		printedNoticeSet.add(sdkName)
		String notice = """
尊敬的Mob用户，您好！您使用的“${sdkName}”已停止运营，给您带来不便我们深感歉意。平台已停止提供该产品服务，为保证您App的有序运营，请及时做好更换。
"""
		println("=======================================================================================")
		println(notice)
		println("=======================================================================================")
	}

	private void printOldPushVersionNotice() {
		String notice = """
亲爱的开发者伙伴：
    MobTech一直致力于给更多的合作伙伴提供稳定且优质的开发者服务，已发布深度优化的MobPush V2.2.0版本。原1.x版本的相关接口将于2020年2月29日24:00停止服务。
请各位开发者伙伴在2020年1月1日24:00前完成新版本服务更新，避免停止服务对您造成影响。
MobTech
2019年9月9日
"""
		println("=======================================================================================")
		println(notice)
		println("=======================================================================================")
	}

	private void printPolicyToast(String v, int toastV) {
		try {
			if (v == null || v.trim().equals("+") || Integer.parseInt(v.trim().replace(".", "")) >= toastV) {
				printPolicyToast()
			}
		} catch (Throwable t) {}
	}

	private void printPolicyToast() {
		if (printedNoticeSet.contains("PolicyToast")) {
			return
		}
		printedNoticeSet.add("PolicyToast")
		String notice = """
根据国家法律法规要求（详见《App违法违规收集使用个人信息行为认定方法》)，开发者在使用MobTech提供的各SDK产品时，需向终端用户展示MobTech的隐私服务协议，并获取用户的授权。
MobTech提供了隐私服务相应的接口供开发者使用。
注意：请所有开发者务必按照本文档接入MobTech隐私服务流程，否则可能造成无法使用MobTech各SDK提供的相关服务。
MobTech隐私服务流程接入指导详见：http://www.mob.com/wiki/detailed?wiki=mob_anzhuo&id=14

关于中华人民共和国工业和信息化部网络安全管理局印发《App违法违规收集使用个人信息行为认定方法》的通知：
http://www.miit.gov.cn/n1146285/n1146352/n3054355/n3057724/n3057729/c7591259/content.html
		"""
		System.err.println(notice)
	}
}