
-- 社区分类
drop table if exists tb_community_category;
create table tb_community_category(
	id int(11)  UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
	name varchar(25),		-- 分类名称
	descr varchar(100),		-- 描述
	parent_id int,			-- 父分类ID
	parent_name varchar(25),-- 父分类名称
	user_interest_cnt int	-- 用户关注数量
);

-- 用户表:aipa_user
drop table if exists tb_user;
-- 账号，密码，用户名，头像，性别，年龄，性取向，婚姻状况，所在地，注册时间，关注的社区，收藏的帖子。
create table tb_user(
	id int(11)  UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
	username varchar(25) NOT NULL,
	password varchar(30) NOT NULL,
	nickname varchar(25),		-- 昵称
	head_picture varchar(100),	-- 用户头像
	sex tinyint,			-- 性别：1-男，2-女
	sex_switch bit(1),		-- 开关：保密、公开
	age tinyint,			-- 年龄
	age_switch bit(1),		-- 开关：保密、公开
	sex_orient tinyint,			--性取向：1-好男，2-好女，3-双杀，4-无性恋
	sex_orient_switch bit(1),	-- 开关：保密、公开
	marital_status tinyint,			--婚姻情况：1-未婚，2-已婚
	marital_status_switch bit(1),	-- 开关：保密、公开
	location varchar(25),		--所在地
	create_time datetime NOT NULL,
	update_time datetime,
	latest_login_time datetime,			-- 最近登录时间
	enabled bit(1) NOT NULL DEFAULT 1,	-- 0：不可用，1：可用
	deleted bit(1) NOT NULL DEFAULT 1	-- 0：正常，1:删除
);
-- 创建索引
CREATE INDEX i_user_username ON tb_user (username);

-- 用户收藏表
drop table if exists tb_user_note_collect;
create table tb_user_note_collect(
	id int(11)  UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
	user_id int(11),	-- 用户ID
	note_id int(11),	-- 帖子ID
	category_id int(11),--帖子种类（根据种类找到帖子在哪张帖子表中）
);

-- 用户关注社区分类表
drop table if exists tb_user_category_interest;
create table tb_user_category_interest(
	id int(11)  UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
	user_id int(11),	-- 用户ID
	category_id int(11)	-- 种类ID
);

-- 需药写一个算法对帖子表进行分表，根据子种类分表
-- 每个子种类一张帖子表

-- 帖子表（命名方式根据算法得来）
drop table if exists tb_community_note;
create table tb_community_note(
	id bigint(20)  UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
	title varchar(255),	-- 标题
	content text,		-- 内容
	pictures varchar(255),	-- 图片,逗号分隔
	user_id int(11),		-- 发布人ID
	create_time datetime,   -- 发布时间
	category_id int(11),	-- 种类ID
	scan_count int,		-- 浏览人数
	comment_count int,	-- 评论数
	collect_count int,	-- 收藏数
	good_count int		-- 点赞数
);


-- 帖子评论表（也是基于帖子种类进行分表，分表规则痛帖子表的分表规则一样）
drop table if exists aipa_note_comment;
create table aipa_note_comment(
	id int  UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
	note_id int,			-- 帖子ID
	commented_id int		-- 被回复的记录ID
	commented_user_id int,		-- 被回复的用户ID
	comment varchar(255),		-- 评论内容
	comment_user_id int,		-- 评论用户ID
	comment_time datetime,		-- 评论时间
	good_count int			-- 点赞数
	
);

###############################不要了#######################################
-- 创建索引
CREATE INDEX i_note_comment ON aipa_note_comment (note_id); -- 快速搜索

-- 评论回复表
drop table if exists aipa_note_comment_reply;
create table aipa_note_comment_reply(
	id int  UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
	comment_id int,			-- 评论ID
	reply_comment varchar(255),	-- 回复评论
	reply_user_id int,		-- 回复人ID
	reply_comment_time datetime,	-- 回复时间
	is_boss bit(1)			-- 是否帖主回复
);
############################################################################