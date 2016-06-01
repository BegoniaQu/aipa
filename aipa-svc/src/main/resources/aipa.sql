
-- 用户表
-- drop table if exists tb_user;
-- 账号，密码，用户名，头像，性别，年龄，性取向，婚姻状况，所在地，注册时间，关注的社区，收藏的帖子。
create table tb_user(
	id int(11)  UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
	username varchar(50) NOT NULL,
	password varchar(30) NOT NULL,
	nickname varchar(50),		-- 昵称
	head_picture varchar(100),	-- 用户头像
	sex tinyint,			-- 性别：1-男，2-女
	sex_switch bit(1),		-- 开关：保密、公开
	age tinyint,			-- 年龄
	age_switch bit(1),		-- 开关：保密、公开
	sex_orient tinyint,			-- 性取向：1-好男，2-好女，3-双杀，4-无性恋
	sex_orient_switch bit(1),	-- 开关：保密、公开
	marital_status tinyint,			-- 婚姻情况：1-未婚，2-已婚
	marital_status_switch bit(1),	-- 开关：保密、公开
	location varchar(25),		-- 所在地
	create_time datetime NOT NULL,
	update_time datetime,
	latest_login_time datetime,			-- 最近登录时间
	enabled bit(1) NOT NULL DEFAULT 1,	-- 0：不可用，1：可用
	deleted bit(1) NOT NULL DEFAULT 1	-- 0：正常，1:删除
);
-- 创建索引
CREATE INDEX i_user_username ON tb_user (username);





-- 社区分类
-- drop table if exists tb_community_category;
create table tb_community_category(
	id int(11)  UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
	name varchar(25),		-- 分类名称
	descr varchar(100),		-- 描述
	parent_id int,			-- 父分类ID
	user_interest_cnt int	-- 用户关注数量
);


-- 用户收藏表
-- drop table if exists tb_user_note_collect;
create table tb_user_note_collect(
	id int(11)  UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
	user_id int(11),	-- 用户ID
	note_id bigint(20),	-- 帖子ID
	category_id int(11),-- 帖子种类（根据种类找到帖子在哪张帖子表中）
	create_time datetime NOT NULL
);

-- 用户关注社区分类表
-- drop table if exists tb_user_category_interest;
create table tb_user_category_interest(
	id int(11)  UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
	user_id int(11),	-- 用户ID
	category_id int(11)	-- 种类ID
);


-- 帖子表,帖子数量剧增时分表，根据帖子ID除余法分100张表
-- drop table if exists tb_community_note;
create table tb_community_note(
	id bigint(20) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
	title varchar(255),		-- 标题
	content text,			-- 内容
	pictures varchar(255),	-- 图片,逗号分隔
	user_id int(11),		-- 发布人ID
	create_time datetime,   -- 发布时间
	category_id int(11),	-- 种类ID
	deleted bit(1),			-- 是否删除
	anonymous bit(1)		-- 是否匿名发布
	
);

-- 帖子统计表
-- drop table if exists tb_community_note_counter;
create table tb_community_note_counter(
	note_id bigint(20) primary key,  -- 帖子ID
	scan_count tinyint		-- 浏览次数
);
	
	
-- 点赞表
-- drop table if exists tb_good_click;
create table tb_good_click(
	id bigint(20) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
	user_id int(11), 	-- 用户ID
	object_id bigint(20), -- 点赞对象ID
	good_type tinyint   -- 针对：1-帖子，2-评论 --当有索引表后，此字段其实没什么用了
);


-- 帖子评论回复表
-- drop table if exists tb_community_note_comment;
create table tb_community_note_comment(
	id bigint(20) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
	note_id bigint(20),		-- 帖子ID
	root_comment_id bigint(20),	-- 评论根ID
	commented_id bigint(20),	-- 被回复的记录ID
	content varchar(255),	-- 评论内容
	content_user_id int(11),-- 评论人ID
	create_time datetime,	-- 评论时间
	deleted bit(1) 			-- 是否删除
);

-- 帖子举报投诉表
-- drop table if exists tb_note_report;
create table tb_note_report(
	id bigint(20) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
	note_id bigint(20),		-- 帖子ID
	report_type tinyint,	-- 举报分类
	user_id int(11),		-- 举报人
	create_time datetime	-- 举报时间
);


-- ---------------- 所有索引表都是基于redis存在时才建立的 ----------------------------

-- 用户量大时，添加帖子评论索引表
-- drop table if exists tb_community_note_comment_index;
create table tb_community_note_comment_index(
	id bigint(20) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
	indexId varchar(30) NOT NULL, -- noteId、评论ID
	indexType tinyint NOT NULL, -- 1-帖子ID索引评论ID,2-评论ID索引回复ID
	commentId varchar(30) NOT NULL, -- 评论ID、回复ID
	subScore bigint(20),  -- 打分
	createTime datetime   -- subScore is null时，根据createTime打分排序
);

-- 用户量大时，添加帖子索引表，tb_user_note_index 要分表，根据indexId hash 分表
-- drop table if exists tb_community_note_index;
create table tb_community_note_index(
	id bigint(20) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
	indexId varchar(30) NOT NULL,
	indexType tinyint NOT NULL, -- 根据用户id索引帖子：user2Note(1),根据分类id索引帖子：category2Note(2)
	noteId varchar(30) NOT NULL,
	subScore bigint(20),  -- 打分
	createTime datetime   -- subScore is null时，根据createTime打分排序
);

-- 用户量大时，添加点赞索引表
-- drop table if exists tb_good_click_index;
create table tb_good_click_index(
	id bigint(20) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
	indexId varchar(30) NOT NULL, -- 帖子ID、评论ID
	indexType tinyint NOT NULL, -- 1-帖子，2-评论
	goodId varchar(30) NOT NULL,-- 点赞表主键ID
	subScore bigint(20),  		-- 打分
	createTime datetime   		-- subScore is null时，根据createTime打分排序
);

-- 用户收藏索引表
-- drop table if exists tb_user_note_collect_index;
create table tb_user_note_collect_index(
	id bigint(20) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
	indexId varchar(30) NOT NULL, -- 帖子ID、用户ID
	indexType tinyint NOT NULL, -- 1-帖子索引被收藏的用户，2-用户索引搜藏的帖子
	collectId varchar(30) NOT NULL,-- 收藏表主键ID
	subScore bigint(20),  		-- 打分
	createTime datetime   		-- subScore is null时，根据createTime打分排序
);

-- 根据帖子ID获取一级评论列表（分页），评论下的二级回复列表





