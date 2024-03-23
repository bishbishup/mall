package com.imooc.mall.controller;

import com.imooc.mall.common.ApiRestResponse;
import com.imooc.mall.common.Constant;
import com.imooc.mall.exception.ImoocMallException;
import com.imooc.mall.exception.ImoocMallExceptionEnum;
import com.imooc.mall.model.pojo.User;
import com.imooc.mall.service.EmailService;
import com.imooc.mall.service.UserService;
import com.imooc.mall.util.EmailUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 *  描述：  用户控制器
 */
@Controller
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    EmailService emailService;

    @ResponseBody
    @GetMapping("/test")
    public User perseonalPage(){
        return userService.getUser();
    }

    /**
     * 注册
     * @param userName
     * @param password
     * @return
     * @throws ImoocMallException
     */
    @PostMapping("/register")
    @ResponseBody
    public ApiRestResponse register(@RequestParam("userName") String userName,@RequestParam("password") String password) throws ImoocMallException {
        if (StringUtils.isEmpty(userName)){
            return ApiRestResponse.error(ImoocMallExceptionEnum.NEED_USER_NAME);
        }
         if (StringUtils.isEmpty(password)){
            return ApiRestResponse.error(ImoocMallExceptionEnum.NEED_PASSWORD);
        }
         // 密码长度不能少于8位
        if (password.length() < 8){
            return ApiRestResponse.error(ImoocMallExceptionEnum.PASSWORD_TOO_SHORT);
        }
        userService.register(userName,password);
        return ApiRestResponse.success();
    }

    /**
     * 登录
     * @param userName
     * @param password
     * @param session
     * @return
     * @throws ImoocMallException
     */
    @GetMapping("/login")
    @ResponseBody
    public ApiRestResponse login(@RequestParam("userName") String userName, @RequestParam("password") String password, HttpSession session) throws ImoocMallException {
         if (StringUtils.isEmpty(userName)){
            return ApiRestResponse.error(ImoocMallExceptionEnum.NEED_USER_NAME);
         }
         if (StringUtils.isEmpty(password)){
            return ApiRestResponse.error(ImoocMallExceptionEnum.NEED_PASSWORD);
         }
         User user = userService.login(userName, password);
         user.setPassword(null);
         session.setAttribute(Constant.IMOOC_MALL_USER,user);
         return ApiRestResponse.success(user);
    }

    /**
     * 更新个性签名
     * @param session
     * @param signature
     * @return
     * @throws ImoocMallException
     */
    @PostMapping("/user/update")
    @ResponseBody
    public ApiRestResponse updateInfo(HttpSession session,@RequestParam String signature) throws ImoocMallException {
        User currentUser = (User)session.getAttribute(Constant.IMOOC_MALL_USER);
        if (currentUser == null){
            return ApiRestResponse.error(ImoocMallExceptionEnum.NEED_LOGIN);
        }
        User user = new User();
        user.setId(currentUser.getId());
        user.setPersonalizedSignature(signature);
        userService.updateInformation(user);
        return ApiRestResponse.success();
    }

    /**
     * 登出，清楚session
     * @param session
     * @return
     */
    @PostMapping("/user/logout")
    @ResponseBody
    public ApiRestResponse logout(HttpSession session){
        session.removeAttribute(Constant.IMOOC_MALL_USER);
        return ApiRestResponse.success();
    }

    /**
     * 管理员登录
     * @param userName
     * @param password
     * @param session
     * @return
     * @throws ImoocMallException
     */
    @GetMapping("/adminLogin")
    @ResponseBody
    public ApiRestResponse adminLogin(@RequestParam("userName") String userName, @RequestParam("password") String password, HttpSession session) throws ImoocMallException {
         if (StringUtils.isEmpty(userName)){
            return ApiRestResponse.error(ImoocMallExceptionEnum.NEED_USER_NAME);
         }
         if (StringUtils.isEmpty(password)){
            return ApiRestResponse.error(ImoocMallExceptionEnum.NEED_PASSWORD);
         }
         User user = userService.login(userName, password);
         // 校验是否是管理员
         if (userService.checkAdminRole(user)) {
             // 是管理员，执行操作
             user.setPassword(null);
             session.setAttribute(Constant.IMOOC_MALL_USER,user);
             return ApiRestResponse.success(user);
         }else {
             return ApiRestResponse.error(ImoocMallExceptionEnum.NEED_ADMIN);
         }

    }

     /**
     * 发送邮件
     */
    @PostMapping("/user/sendEmail")
    @ResponseBody
    public ApiRestResponse sendEmail(@RequestParam("emailAddress") String emailAddress) throws ImoocMallException {
        // 检查邮件地址是否有效，检查是否已注册
        boolean validEmailAddress = EmailUtil.isValidEmailAddress(emailAddress);
        if (validEmailAddress){
            boolean emailPassed = userService.checkEmailRegistered(emailAddress);
            if (!emailPassed){
                return ApiRestResponse.error(ImoocMallExceptionEnum.EMAIL_ALREADY_BEEN_REGISTERED);
            }else {
                String verificationCode = EmailUtil.genVerificationCode();
                Boolean saveEmailToRedis = emailService.saveEmailTORedis(emailAddress, verificationCode);
                if (saveEmailToRedis){
                    emailService.sendSimpleMessage(emailAddress,Constant.EMAIL_SUBJECT,"欢迎注册，您的验证码是"+verificationCode);
                    return ApiRestResponse.success();
                }else {
                    return ApiRestResponse.error(ImoocMallExceptionEnum.EMAIL_ALREADY_BEEN_SEND);
                }
            }
        }else {
            return ApiRestResponse.error(ImoocMallExceptionEnum.WRONG_EMAIL);
        }
    }
}
