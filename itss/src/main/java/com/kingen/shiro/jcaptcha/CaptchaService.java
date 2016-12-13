package com.kingen.shiro.jcaptcha;

import com.octo.captcha.service.captchastore.FastHashMapCaptchaStore;
import com.octo.captcha.service.image.DefaultManageableImageCaptchaService;
import com.octo.captcha.service.image.ImageCaptchaService;

/**
 * 复杂的图片
 * @author f
 *
 */
public class CaptchaService {  
	  
    // 不允许构造实例  
    private CaptchaService() {  
    }  
  
    private static ImageCaptchaService instance = null;  
  
    /** 
     * SimpleListSoundCaptchaEngine //还可以用声音 SpellerSoundCaptchaEngine 
     * SpellerSoundCaptchaEngine DefaultGimpyEngineCaptcha 
     * BaffleListGimpyEngineCaptcha BasicListGimpyEngineCaptcha 
     * DeformedBaffleListGimpyEngineCaptcha DoubleRandomListGimpyEngineCaptcha 
     * SimpleListImageCaptchaEngineCaptcha SimpleFishEyeEngineCaptcha 
     */  
    // 传入样式类  
    static {  
//        instance = new MyManageableImageCaptchaService(  
        		instance = new DefaultManageableImageCaptchaService(  
                new FastHashMapCaptchaStore(), new ImageCaptchaEngine(),  
                180, 100000, 75000);  
    }  
  
    public static ImageCaptchaService getInstance() {  
        return instance;  
    }  
  
}  
