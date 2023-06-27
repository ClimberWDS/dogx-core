package com.dogx.core.base.model;

public enum ExtraCodeEnum {

    /**
     * 系统独享的错误编码，避免冲突
     *
     * 服务支撑 SmsExtraCodeEnum使用   4000-4299
     * 运营管理 OmsExtraCodeEnum使用   4300-4699
     * 票据查询 CrtExtraCodeEnum使用   4700-4799
     * 消息服务 MssExtraCodeEnum使用   4800-4899
     * 消息服务 TrgExtraCodeEnum使用   4900-4999
     *
     */

    /**
     * 成功的响应码
     */
    NORMAL("0000", "OK"),
    NO_AUTH("9999", "无权限请求"),
    NO_BTN_AUTH("4003", "没有此操作权限"),
    NOT_LOGIN("1000", "账号未登录或无权限访问"),
    ERROR_PARAMS("1001", "请求参数错误或缺失"),
    NO_DATA("1002", "未找到相应的数据"),
    QUERY_ERROR("1003", "查询身份票据失败"),

    SQL_ERROR("1004", "操作数据库失败"),

    ERROR_REQUEST_PARAM("1005", "非法参数请求"),

    DATA_DEAL_ERROR("1006", "数据处理失败"),

    QUERY_ROOT_CERT_ERROR("1007", "查询根证书失败"),

    PUSH_ROOT_CERT_ERROR("1007", "保存根证书失败"),

    EMP_IDENTITY_DISABLED("1008", "身份已被服务管理系统禁用"),

    AUTH_SIGN_IVALID("1009", "签名值错误"),

    AUTH_ENCYPT_DECYPT("1010", "key不正确或加解密算法错误"),

    DATA_EXISTS("1011", "记录已存在"),

    INVALID_REQUEST("1012", "无效的请求"),

    MSG_SEND_ERROR("1013", "短信发送失败"),

    INVALID_RESPONSE("1014", "无效的返回数据"),

    REQUEST_CA_ERROR("1015", "请求CA失败"),

    INVALID_TOKEN("1016", "由于您长时间未操作，请重新登录。"),

    GATEWAY_ERROR("1017", "请求网关错误"),

    SERVICE_NOT_FOUND("1018", "服务不存在"),
    INVALID_SM4_KEY("1019", "key不正确或已失效"),
    REPEAT_REQUEST("1020", "重复的请求"),
    INFO_INPUT_ERROR("1021", "填写信息有误"),
    DATA_NOT_CHANGE("1022", "信息未发生变化"),

    CREDENTIAL_DECRYPT_ERROR("1023", "解密credential失败"),
    MQ_SEND_ERROR("1024", "MQ投递异常"),
    FILE_READ_ERROR("1025", "文件读取异常"),

    COMMIT_COUNT_LIMIT("1027", "今日提交次数超过限制，请明天再试"),
    ACTIVE_CODE_EXPIRE("1028", "激活码已过期"),

    SERVER_ERROR("5000", "服务器异常"),
    INNER_SERVER_ERROR("5001", "服务器异常"),
    SOCKET_CONNECT_ERROR("5002", "请求服务连接超时"),
    SOCKET_TIMEOUT_ERROR("5003", "请求服务响应超时"),

    //============权限系统account==============
    PHONE_HAS_EXISTS("1101", "手机号已经注册"),

    USERNAME_EXISTS("1102", "用户名已存在"),

    BINDING_EXISTS("1103", "存在绑定关系，不允许删除"),

    USERNAME_DISABLED("1104", "账号已禁用"),

    LOGIN_LIMIT_OVER("1105", "登录错误次数超过上限，请%s分钟后再登录"),

    LOGIN_PASSWORD_ERROR("1106", "用户名或密码不正确,请检查"),

    VALIDATE_CODE_FAIL("1107", "验证码输入错误或已失效"),

    DEFAULT_DATA_FORBID_DELETE("1108", "系统默认数据，不允许删除"),

    SOURCE_PASSWORD_ERROR("1109", "原始密码不正确"),

    SON_DATA_EXISTS("1110", "存在子级数据，不允许删除"),

    LOGIN_EXPIRE_OVER("1111", "登录已过期，请重新登录"),

    // ==============三要素校验 start 1201 =================
    NAME_ERROR("1201", "姓名有误"),

    IDCARD_ERROR("1202", "身份证号码有误"),

    PHONE_NOVERIFY("1203", "手机号码未实名认证"),

    IDCARDANDPHONE_MISMATCH("1204", "手机号码跟身份信息不一致，请检查并重新验证"),
    VALID_THREE_INFO_ERROR("1205", "身份信息不一致，请确保是同一个人信息，修改后重试。"),
    IDCARD_OCR_ERROR("1206", "身份证照片OCR识别失败"),
    LIVING_BODY_FAIL("1207", "活体视频校验不通过"),

    // ==============三要素校验 end 1230 =================

    // ==============ra 1301 =================
    APPLY_COMPANY_SZCA_FAIL("1301", "深圳CA申请机构证书失败"),

    COMPANY_ALREADY_SUBMIT("1302", "企业已提交审核,不可重复操作"),

    ERROR_SIGN_HINT("1303", "信息填写有误，请检查后重新输入"),
    COMPANY_ANNUAL_REVIEW_ERROR("1304", "ca企业续期失败"),

    // ==============sms sdk 1401 =================
    SMS_FREQUENCY_LIMIT_OVER("1401", "发送短信过于频繁，请%s分钟后再试"),

    SMS_DAY_LIMIT_OVER("1402", "一天内发送短信超过限制次数，请1天后再试"),

    VALIDATE_ACTIVE_CODE("1403", "验证码已失效,请重新获取"), // 统一用 VALIDATE_CODE_FAIL

    SDK_LOGIN_SIGN_IVALID("1404", "sdk登录签名值错误"),
    SDK_LOGIN_IVALID("1405", "未登录或登录已失效"),
    SDK_VERFICATION_IVALID("1406", "加密通道已失效"),

    // ==============oms sdk 1451 =================
    COMPANY_CONTACT_PERSON_DISABLE("1451", "企业通讯录已被关闭，如有疑问请联系企业管理员"),

    FACE_VERIFY_TIME_LIMIT_OVER("1452", "今日人脸头像实名比对已达到限制次数"),
    FACE_PHOTO_VERIFY_FAIL("1453", "人脸头像识别未通过"),


    //==============文件校验 2000=====================
    FILE_SIZE_LIMIT_FAIL("2001", "上传文件不能超过3M"),

    UPLOAD_FILE_FAIL("2002", "上传文件失败"),

    //==============企业业务 2100=================
    ADD_COMPANY_FAIL("2100", "添加企业失败"),

    BANK_NO_LENGTH_FAIL("2101", "银行账号长度不能超过20"),

    GENERATE_SESSION_NO_FAILED("2102", "生成会话编号失败"),

    GENERATE_ACTIVECODE_FAILED("2103", "生成激活码失败"),

    OPEN_COMPANY_FAIL("2104", "企业申请开通失败"),

    USB_KEY_SIGN_IN_FAIL("2105", "usbKey签收更新失败"),

    CA_APPLY_UKEY_FAIL("2106", "申请管理员Ukey失败"),

    CA_APPLY_UKEY_UPDATE_ORDER_FAIL("2107", "申请管理员Ukey更新订单信息失败"),

    UKEY_ORDER_ADD_FAIL("2108", "生成ukey订购订单失败"),

    SYNC_COMPANY_ERROR("2109", "同步企业信息失败"),

    CA_UPDATE_UKEY_FAIL("2106", "申请管理员Ukey失败"),

    UKEY_CERTSN_ERROR("2107", "Ukey证书唯一编号不一致"),

    COMPANY_UKEY_UPDATE_FAIL("2108", "更新管理员ukey失败"),

    UPDATE_COMPANY_UKEY_SZCA_FAIL("2109", "深圳CA变更管理员Ukey审核失败"),

    COMPANY_UNSUBSCRIBE_TASK_FAIL("2110", "企业注销定时器失败"),

    CA_COMPANY_UNSUBSCRIBE_TASK_FAIL("2111", "调用深圳CA注销企业失败"),

    DOWNLOAD_ERROR("2112", "下载失败"),

    UPDATE_SIGN_IN_TIME_ERROR("2113", "企业同步签收时间失败"),

    COMPANY_COMPLAINT_DELAY("2114", "企业封禁时间不能小于当前时间"),

    COMPANY_COMPLAINT_ALERT("2115", "员工所在企业已封禁,不能解封该企业员工"),

    COMPLAINT_FORBIDDEN("2116", "企业已封禁,退出登录，请联系管理员"),

    COMPLAINT_UNSUBSCRIBE("2117", "企业已注销，请联系管理员"),

    SELF_ACTIVECODE_SENDED("2118", "身份激活码已下发"),

    IMAGE_AUDITING_UNPASS("2119", "图片审核不通过"),
    IMAGE_AUDITING_INVOKER_ERROR("2120", "图片审核调用异常"),

    //==============ukey业务 2200====================
    UKEY_PRIMARY_NOT_EXIST("2200", "企业认证ukey记录不存在"),

    UKEY_MANAGER_EXIST("2201", "ukey管理员已存在"),

    UPLOAD_IDCARDBACKIMG_ISNULL("2202", "上传的身份证国徽未上传"),

    UPLOAD_IDCARDIMG_ISNULL("2203", "身份证人像面未上传"),

    UKEY_DATA_NO_EXIST("2204", "没有找到相应的ukey数据"),

    UKEY_NO_PRIMARY("2205", "企业只有一个ukey，却非主ukey"),
    NO_REQUEST_FINISH("2206", "请求未结束"),
    UKEY_ALREADY_RENEW("2207", "证书已过期并续期证书已续期成功"),
    //==============商品 2300====================
    SAVE_ERROR("2301", "保存失败"),

    MODIFY_ERROR("2302", "修改失败"),

    DELETE_ERROR("2303", "删除失败"),

    COMMODITY_KEY_NOT_EXIST("2304", "商品类型key不存在"),

    COMMODITY_KEY_HAS_USE("2305", "商品类型key已经使用"),


    //=================登录业务 2300==================
    COMPANY_NOT_ACTIVE("2300", "企业未激活"),

    SHENZHEN_CA_CHECKOUT("2301", "Ukey校验"),

    DECRYPT_ERROR("2302", "验签失败"),

    SIGN_IS_NULL("2303", "请插入USBKey!"),

    COMPANY_NO_AUTHED_LOGIN("2304", "公司未审核通过，无权限登录"),

    COMPANY_NOT_EXISTS("2305", "公司不存在或已刪除"),

    NEW_COMPANY_LOGIN("2306", "首次登录.请使用短信登录"),

    LOCK_COMPANY_LOGIN("2307", "企业被封禁,无法登录"),

    NUMBER_EMPTY("2308", "收取验证码号码不能为空!"),

    CERT_SIGN_VERIFY_FAILED("2309", "证书校验或验签失败"),

    COMPANY_SOURCE_PASSWORD_ERROR("2311", "原始密码不正确"),

    COMPANY_SUBJECT_NAME_NOT("2312", "企业主题不一致"),

    //==============告警系统 2400====================
    ALARM_KEY_NOT_EXIST("2401", "事故类型key不存在"),

    PRODUCE_ALARM_ERROR("2402", "生产告警异常"),

    SEND_SMS_ERROR("2403", "短信发送失败!"),


    //=================订单服务 2500==================
    ORDER_SERVICE_ACTIVE_ERROR("2501", "订单服务激活失败"),

    ORDER_SERVICE_INVALID_ERROR("2502", "订单服务作废失败"),

    ORDER_SERVICE_FREEZE_ERROR("2503", "订单服务冻结失败"),

    REJECT_ERROR("2504", "驳回失败"),

    PASS_ERROR("2505", "审核失败"),

    CONFIRM_ERROR("2506", "起租/止租失败"),

    SAVE_VALID_ERROR("2507", "保存校验失败"),

    REJECT_VALID_ERROR("2508", "驳回校验失败"),

    PASS_VALID_ERROR("2509", "审核通过校验失败"),

    CONFIRM_VALID_ERROR("2510", "起租/止租校验失败"),
    SERVICE_UNREGISTERED("2511", "服务未注册"),
    UPDATE_ORDER_ERROR("2512", "更新订单失败"),
    COMPANY_REPEAT("2513", "同一手机号码只能认证一家企业"),

    //================身份列表 2500========================
    EMPLOYEE_NOT_EXISTS("2600", "身份不存在"),

    UPDATE_EMPLOYEE_SESSION_NO_FAILED("2601", "运营更新会话编号失败"),

    SEND_SESSION_NO_ACTIVE_CODE_FAILED("2602", "发送会话编号激活码短信失败"),

    CACHE_ACTIVECODE_FAILED("2603", "缓存激活码失败"),

    NO_SESSIONID("2604", "二维码已失效或管理员暂未验证，请联系管理员"),

    NO_COMPANY_COMMODITY("2605", "无可用的企业套餐，请先购买"),

    STAFF_LESS("2606", "当前企业多人套餐身份数不足,可更换套餐或为此成员配置个人套餐"),

    PERSON_NOT_EXIST("2607", "个人身份不存在或已删除"),

    COMPANY_ID_EMPTY("2608", "公司id不能为空"),

    EMPLOYEE_ID_EMPTY("2609", "员工id不能为空"),

    COMPANY_HAS_NOT_SET_UP_APPLY_TYPE("2610", "企业未设置默认激活类型"),

    STAFF_EXCLUSIVE_LESS("2611", "独享套餐剩余身份数不足"),

    PERSONAL_AUTH_APPROVE_PASSED("2612", "个人身份认证已审核通过"),

    // -------------app版本 start---------------------
    IS_NEW_VERSION("2701", "当前已经是最新版本"),
    VERION_HAS_EXISTS("2702", "该版本号已经发布"),
    VERSION_NOT_LESS_LAST("2703", "版本号须大于前一版本"),
    NO_VERSION("2704", "沒有可安装的版本"),
    NO_CANCEL_PUBLISH("2705", "历史版本发布一周之后不允许撤销"),
    NO_CANCEL_REPEAT("2706", "该版本已经撤销，请勿重复撤销"),
    APPKEY_NOT_EXISTS("2707", "未找到该appkey"),
    // -------------app版本 end---------------------

    // --------------闪信 start-------------
    USSD_LANDLINE_NUMBER("2731", "被叫为固话，无法发送闪信"),
    USSD_MOBILE_PHONE_FORMAT_ERROR("2732", "被叫手机号码格式不正确"),
    USSD_NO_TEMPLATE("2733", "运营管理未配置闪信模板"),
    USSD_TIME_OUT("2734", "请求闪信平台超时"),
    USSD_FAIL("2735", "闪信平台返回失败：%s"),
    USSD_ERR_TEMPLATE_STATUS("2736", "闪信模板未审核通过"),
    //--------------闪信 end -------------

    //票据查询 28xx
    UNAUTH_CERT_NOT_SHOW("2801", "该版本暂不支持展示个人未认证身份凭证"),

    // 订户app start--------------
    SUB_APP_CERT_DELETED("2901", "当前可信订户证书被注销，请重新添加"),
    SUB_APP_CERT_CHANGE("2902", "当前可信订户证书已失效，请重新添加"), //证书已变更
    SUB_APP_CERT_OVERDUE("2903", "订户证书已过期，请重新激活"),
    SUB_APP_COMPANY_COMPLAINT("2904", "当前企业已禁用"),
    SUB_APP_COMPANY_EXPIRED("2905", "企业认证已过期，请尽快完成续期"),
    SUB_APP_ERROR("2910", "加载失败，请重试（2910）"), //异常状态
    // 订户app end  --------------

    //支付相关错误信息
    CREATE_ORDER_ERROR("9001", "下单接口发生错误"),
    NOTIFY_ORDER_ERROR("9002", "异步回调通知接口失败"),
    //特定返回码,不能修改
    PERSONAL_AUTH_CHANGED("9003", "证书已发生变更,请重新下载身份"),
    PERSONAL_PAY_ORDER_NOT_EXISTS("9004", "交易不存在");
    public String code;
    public String msg;

    ExtraCodeEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static String findMsgByCode(String code) {
        for (ExtraCodeEnum enumObj : ExtraCodeEnum.values()) {
            if (enumObj.getCode().equals(code)) {
                return enumObj.getMsg();
            }
        }
        return "";
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
