package com.yxx.mall.backend.controller;

import com.google.code.kaptcha.Producer;
import com.yxx.mall.backend.service.impl.RedisService;
import com.yxx.mall.common.utils.Base64;
import com.yxx.mall.common.utils.R;
import com.yxx.mall.common.utils.constant.Constants;
import com.yxx.mall.common.utils.uuid.IdUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.FastByteArrayOutputStream;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @author xyong
 * date 2021-05-07
 *
 * 验证码操作处理
 */
@RestController
public class CaptchaController {

    @Resource(name = "captchaProducer")
    private Producer captchaProducer;

    @Resource(name = "captchaProducerMath")
    private Producer captchaProducerMath;


    @Autowired
    private RedisService redisService;

    // 验证码类型
    @Value("${mall.captchaType}")
    private String captchaType;

    @GetMapping("/captchaImage")
    public R getCode(HttpServletResponse response) throws IOException {

        //验证码信息
        String uuid= IdUtils.simpleUUID();
        String verifyKey = Constants.CAPTCHA_CODE_KEY + uuid;

        String capStr = null, code = null;
        BufferedImage image = null;
        // 生成验证码
        if ("math".equals(captchaType))
        {
            String capText = captchaProducerMath.createText();
            capStr = capText.substring(0, capText.lastIndexOf("@"));
            code = capText.substring(capText.lastIndexOf("@") + 1);
            image = captchaProducerMath.createImage(capStr);
        }
        else if ("char".equals(captchaType))
        {
            capStr = code = captchaProducer.createText();
            image = captchaProducer.createImage(capStr);
        }
        redisService.setCacheObject(verifyKey, code, Constants.CAPTCHA_EXPIRATION, TimeUnit.MINUTES);
        // 转换流信息写出
        FastByteArrayOutputStream os = new FastByteArrayOutputStream();
        try
        {
            ImageIO.write(image, "jpg", os);
        }
        catch (IOException e)
        {
            return R.error(e.getMessage());
        }
        return R.ok().put("uuid",uuid).put("img", Base64.encode(os.toByteArray()));
    }


}
