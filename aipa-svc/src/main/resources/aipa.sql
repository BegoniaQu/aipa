
-- ��������
drop table if exists tb_community_category;
create table tb_community_category(
	id int(11)  UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
	name varchar(25),		-- ��������
	descr varchar(100),		-- ����
	parent_id int,			-- ������ID
	parent_name varchar(25),-- ����������
	user_interest_cnt int	-- �û���ע����
);

-- �û���:aipa_user
drop table if exists tb_user;
-- �˺ţ����룬�û�����ͷ���Ա����䣬��ȡ�򣬻���״�������ڵأ�ע��ʱ�䣬��ע���������ղص����ӡ�
create table tb_user(
	id int(11)  UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
	username varchar(25) NOT NULL,
	password varchar(30) NOT NULL,
	nickname varchar(25),		-- �ǳ�
	head_picture varchar(100),	-- �û�ͷ��
	sex tinyint,			-- �Ա�1-�У�2-Ů
	sex_switch bit(1),		-- ���أ����ܡ�����
	age tinyint,			-- ����
	age_switch bit(1),		-- ���أ����ܡ�����
	sex_orient tinyint,			--��ȡ��1-���У�2-��Ů��3-˫ɱ��4-������
	sex_orient_switch bit(1),	-- ���أ����ܡ�����
	marital_status tinyint,			--���������1-δ�飬2-�ѻ�
	marital_status_switch bit(1),	-- ���أ����ܡ�����
	location varchar(25),		--���ڵ�
	create_time datetime NOT NULL,
	update_time datetime,
	latest_login_time datetime,			-- �����¼ʱ��
	enabled bit(1) NOT NULL DEFAULT 1,	-- 0�������ã�1������
	deleted bit(1) NOT NULL DEFAULT 1	-- 0��������1:ɾ��
);
-- ��������
CREATE INDEX i_user_username ON tb_user (username);

-- �û��ղر�
drop table if exists tb_user_note_collect;
create table tb_user_note_collect(
	id int(11)  UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
	user_id int(11),	-- �û�ID
	note_id int(11),	-- ����ID
	category_id int(11),--�������ࣨ���������ҵ��������������ӱ��У�
);

-- �û���ע���������
drop table if exists tb_user_category_interest;
create table tb_user_category_interest(
	id int(11)  UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
	user_id int(11),	-- �û�ID
	category_id int(11)	-- ����ID
);

-- ��ҩдһ���㷨�����ӱ���зֱ�����������ֱ�
-- ÿ��������һ�����ӱ�

-- ���ӱ�������ʽ�����㷨������
drop table if exists tb_community_note;
create table tb_community_note(
	id bigint(20)  UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
	title varchar(255),	-- ����
	content text,		-- ����
	pictures varchar(255),	-- ͼƬ,���ŷָ�
	user_id int(11),		-- ������ID
	create_time datetime,   -- ����ʱ��
	category_id int(11),	-- ����ID
	scan_count int,		-- �������
	comment_count int,	-- ������
	collect_count int,	-- �ղ���
	good_count int		-- ������
);


-- �������۱�Ҳ�ǻ�������������зֱ��ֱ����ʹ���ӱ�ķֱ����һ����
drop table if exists aipa_note_comment;
create table aipa_note_comment(
	id int  UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
	note_id int,			-- ����ID
	commented_id int		-- ���ظ��ļ�¼ID
	commented_user_id int,		-- ���ظ����û�ID
	comment varchar(255),		-- ��������
	comment_user_id int,		-- �����û�ID
	comment_time datetime,		-- ����ʱ��
	good_count int			-- ������
	
);

###############################��Ҫ��#######################################
-- ��������
CREATE INDEX i_note_comment ON aipa_note_comment (note_id); -- ��������

-- ���ۻظ���
drop table if exists aipa_note_comment_reply;
create table aipa_note_comment_reply(
	id int  UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
	comment_id int,			-- ����ID
	reply_comment varchar(255),	-- �ظ�����
	reply_user_id int,		-- �ظ���ID
	reply_comment_time datetime,	-- �ظ�ʱ��
	is_boss bit(1)			-- �Ƿ������ظ�
);
############################################################################