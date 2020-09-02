package com.mob.products.sharesdk

import com.mob.AutoCorrectNode

class InfoNode extends AutoCorrectNode {
	Set getFieldNames() {
		return [
				"Id",
				"SortId",
				"AppId",       // String appId, clientID, applicationId, channelID
				"AppKey",      // String appKey, consumerKey, apiKey, oAuthConsumerKey
				"AppSecret",   // String appSecret, consumerSecret, secretKey, secret, clientSecret, apiSecret, channelSecret
				"CallbackUri", // String redirectUrl, redirectUri, callbackUrl
				"ShareByAppClient",
				"Enable",
				"BypassApproval",
				"userName",
				"path",
				"HostType",
				"WithShareTicket",
				"MiniprogramType",
				"callbackscheme",
				"AgentId",
				"Schema",
				"callbackAct",//分享or登录后的回调Activity，目前抖音平台会用到
				"FaceBookLoginProtocolScheme",//facebook专用
				"OfficialVersion"//添加使用官方版本，google和facebook
		]
	}
}