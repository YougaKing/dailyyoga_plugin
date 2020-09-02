package com.mob

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.repositories.ArtifactRepository

class MobSDK implements Plugin<Project> {
	void apply(Project project) {
		try {
			configMobMaven(project)
		} catch (Throwable t) {
			println("=============mob maven config failed, use normal mode================")
			t.printStackTrace()
			// 如果配置失败，那就和以前一样
			project.repositories {
				maven {
					name "MobSDK"
					url 'http://mvn.mob.com/android'
				}
			}
		}

		MobSDKConfig sdk = project.extensions.create("MobSDK", MobSDKConfig)
		sdk.project = project
		MobSDKSolution solution = project.extensions.create("MobSDKSolution", MobSDKSolution)
		project.extensions.add("MobSolution", solution)
		solution.mobSDK = sdk

		println("=============================")
		println("=== MobSDK ${MobSDKVersion.NAME} ===")
		println("=============================")
	}

	void configMobMaven(Project project) {
		boolean usingDebugServer = false
		List<ArtifactRepository> mobRepos = []
		//1. 获取用户配置的mob仓库
		project.repositories.each {repo ->
			try {
				if (repo.url != null) {
					String url = String.valueOf(repo.url)
					if (url.startsWith("http://mvn.mob.com") || url.startsWith("https://mvn.mob.com")) {
						mobRepos.add(repo)
					} else if (url.startsWith("http://10.18.97.56:8080/job/iMaven/ws/android")) {
						usingDebugServer = true
					}
				}
			} catch (Throwable t) {}
		}

		//2. 去掉用户配置的mob仓库相关配置
		if (mobRepos.size() > 0) {
			project.repositories.removeAll(mobRepos)
		}

		//3. 配置mob仓库，测试环境除外； 正式环境处理分gradle版本5.1以下、和5.1及以上
		if (!usingDebugServer) {
			//非测试环境，则进行mob仓库的添加
			if (gradleVersionIsEqualOrUpper51(project)) {
				//如果版本是5.1及以上版本，则可以指定包下载地址
				def mobGroups = ['com.awstar', 'cn.sharesdk', 'cn.smssdk', 'cn.sharerec']
				//3.1 排除去非mob仓库中寻找MobSDK
				boolean customSkip = false
				project.repositories.each {repo ->
					if (repo != null) {
						customSkip = repo.name.endsWith("_SKIP_MClUDE")
						if (!customSkip){
							repo.content {
								excludeGroupByRegex "com\\.mob.*"
								mobGroups.each {excludeGroup it}
							}
						}
					}
				}

				//3.2 加入mob仓库，并指定MobSDK去mob仓库中寻找
				project.repositories {
					maven {
						name "MobSDK"
						url 'http://mvn.mob.com/android'
						if (!customSkip) {
							content {
								includeGroupByRegex "com\\.mob.*"
								mobGroups.each {includeGroup it}
							}
						}
					}
				}
			} else {
				//如果版本不符合，那就和以前一样
				project.repositories {
					maven {
						name "MobSDK"
						url 'http://mvn.mob.com/android'
					}
				}
			}
		}
	}

	boolean gradleVersionIsEqualOrUpper51(Project project) {
		// 判断gradle版本，5.1以后可以设置每一个maven包含的库
		try {
			def ps = project.gradle.gradleVersion.split("\\.")
			if (ps != null && ps.length > 0) {
				int i = Integer.parseInt(ps[0])
				if (i > 5) {
					return true;
				} else if (i == 5 && ps.length > 1) {
					i = Integer.parseInt(ps[1])
					return i >= 1;
				}
			}
		} catch (Throwable t) {}
		return false;
	}
}