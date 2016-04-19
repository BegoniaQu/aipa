package com.aipa.svc.common.util;

/**
 * Created by ZENGFANGUI on 15/4/20.
 * 对于服务端的log，我们可以花一些时间来格式化log。所以要求具体内容都是json
 * 不用打印timestamp。
 */
public enum LogCategory {
    access,
    comment,//大的类别-评论
    comment_add,//添加评论。请吧输出的格式定义在下面。比如：添加的json内容。
    comment_del,//删除评论，请把输出的格式定义在下面，比如：“{\"uid\":\"%\",\"commentid\":\"%s\"}”%（uid，commentid,new data()）

    comment_update,//没有请删除
    comment_up,//评论点赞。谁在什么时候点赞了哪个评论。如果可以，能否给出这个评论属于的帖子，圈子，uid？如果不行，
    comment_unup,
    topic,
    topic_add,//添加话题。
    topic_del,//删除话题
    topic_update,
    topic_up,
    topic_un_up,
    topic_fav_add,
    topic_fav_del,
    circle,
    circle_info_add,
    circle_info_del,
    circle_info_update,
    circle_follow,
    circle_un_follow,
    circle_user_sign,
    circle_user_forbid,//禁言
    circle_un_user_forbid,//解除禁言
    user,
    user_register,
    user_un_register,
    seckill,
    seckill_ticket_add,
    seckill_ticket_update,
    seckill_ticket_del,
    seckill_order_add,
    seckill_order_update,
    seckill_order_del,
    seckill_ticket_try_buy,
    image,
    image_upload, circle_user_fovorite, comment_recover, friend, friend_follow, rmd, rmd_add, rmd_update, rmd_delete, topic_recover, alert,
    //how to stat the user visit? by the access log. who,when,url.url parameters, maybe from

}
