package com.ruoyi.smty.utils;

import com.ruoyi.common.enums.SerialNoPrefix;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.ruoyi.smty.constant.Constant;
import com.ruoyi.system.service.ISysSerialConfigService;

import cn.hutool.core.util.RandomUtil;

/**
 * @author: Daniel 2021/4/8 16:30
 */
public class CodeUtil {
	// 时间流水 + 4位随机数，例如： 20210207133443 1515
	public static String genOrderNo() {
		return DateUtils.dateTimeNow() + RandomUtil.randomInt(1000, 9000);
	}

	/**
	 * 获取业务流水号
	 * 
	 * @param prefix
	 * @return
	 */
	public static String getSerialNo(SerialNoPrefix prefix) {
		ISysSerialConfigService configService = SpringUtils.getBean(ISysSerialConfigService.class);
		return configService.getSerialNo(prefix);
	}

	public static String genCode() {
		return String.valueOf(System.currentTimeMillis()) + RandomUtil.randomInt(1000, 9999);
	}

	// 生成6位验证码
	public static String genVerifyCode() {
		return String.valueOf(RandomUtil.randomInt(100000, 999999));
	}

	// 生成支付流水号
	public static String genPayNo() {
		return Constant.PAYNO_PREFIX + RandomUtil.randomNumbers(16);
	}
}
