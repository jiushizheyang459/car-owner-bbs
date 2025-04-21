package com.ori.enums;

public enum AppHttpCodeEnum { // 成功
    SUCCESS(200, "操作成功"),
    // 登录
    NEED_LOGIN(401,"需要登录后操作"),
    NO_OPERATOR_AUTH(403,"无权限操作"),
    SYSTEM_ERROR(500,"出现错误"),
    USERNAME_EXIST(501,"用户名已存在"),
    PHONENUMBER_EXIST(502, "手机号已存在"),
    EMAIL_EXIST(503, "邮箱已存在"),
    REQUIRE_USERNAME(504, "必需填写用户名"),
    LOGIN_ERROR(505, "用户名或密码错误"),
    CONTENT_NOT_NULL(506, "评论内容不为空"),
    FILE_TYPE_ERROR(507, "请上传.jpg或.jpeg或.png或.icon格式的图片"),
    USERNAME_NOT_NULL(508, "用户名不能为空"),
    NICKNAME_NOT_NULL(509, "昵称不能为空"),
    PSSWORD_NOT_NULL(510, "密码不能为空"),
    EMAIL_NOT_NULL(511, "邮箱不能为空"),
    NICKNAME_EXIST(512, "昵称已存在"),
    FOLLOWED_EXIST(513, "不可以重复关注同一用户"),
    FOLLOWER_EXIST(514, "关注不存在"),
    INFORMATION_NOT_NULL(515, "资讯内容不为空"),
    INFORMATION_NOT_FOUND(516, "未查询到资讯"),
    INFORMATION_IDS_NOT_NULL(517, "要删除的资讯ID集合为空"),
    ADVERTISEMENT_NOT_NULL(518, "广告内容不为空"),
    ADVERTISEMENT_NOT_FOUND(519, "未查询到广告"),
    ADVERTISEMENT_IDS_NOT_NULL(520, "要删除的广告ID集合为空"),
    EVENT_NOT_NULL(521, "广告内容不为空"),
    EVENT_NOT_FOUND(522, "未查询到广告"),
    EVENT_IDS_NOT_NULL(523, "要删除的广告ID集合为空"),
    KNOWLEDGE_NOT_NULL(524, "知识内容不为空"),
    KNOWLEDGE_NOT_FOUND(525, "未查询到知识"),
    KNOWLEDGE_IDS_NOT_NULL(526, "要删除的知识ID集合为空"),
    ARTICLE_NOT_NULL(527, "文章内容不为空"),
    ARTICLE_NOT_FOUND(528, "未查询到文章"),
    ARTICLE_IDS_NOT_NULL(529, "要删除的文章ID集合为空"),
    LINK_NOT_NULL(530, "友情链接网站地址不为空"),
    LINK_NOT_FOUND(531, "未查询到友情链接"),
    LINK_IDS_NOT_NULL(532, "要删除的友情链接ID集合为空"),
    CATEGORY_NOT_NULL(530, "车辆分类名不为空"),
    CATEGORY_NOT_FOUND(531, "未查询到车辆分类"),
    CATEGORY_IDS_NOT_NULL(532, "要删除的车辆分类ID集合为空"),
    COMMENT_IDS_NOT_NULL(533, "要删除的评论ID集合为空"),
    SAVE_EXIST(534, "收藏不存在"),
    ANNOUNCEMENT_NOT_NULL(535, "公告标题不为空"),
    ANNOUNCEMENT_NOT_FOUND(536, "未查询到公告"),
    ANNOUNCEMENT_IDS_NOT_NULL(537, "要删除的公告ID集合为空");
    int code;
    String msg;

    AppHttpCodeEnum(int code, String errorMessage) {
        this.code = code;
        this.msg = errorMessage;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}